package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_competition.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.CompetitionAdapter
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.Competition
import uz.techie.mahsulot.model.Concourse
import uz.techie.mahsulot.util.Constants
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class CompetitionFragment :Fragment(R.layout.fragment_competition) {

    private val viewModel:MahsulotViewModel by viewModels()
    private lateinit var customProgressDialog: CustomProgressDialog
    private val TAG = "CompetitionFragment"
    private lateinit var competitionAdapter: CompetitionAdapter
    var token = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        customProgressDialog = CustomProgressDialog(requireContext())

        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            it?.token?.let { mToken->
                token = "Token $mToken"
                viewModel.loadCompetition(token)
            }
        })





        competitionAdapter = CompetitionAdapter(requireContext())

        competition_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = competitionAdapter
        }


        viewModel.competition.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: competition "+response)
            when(response){
                is Resource.Loading->{
                    customProgressDialog.show()
                    hideNoteText()
                }
                is Resource.Error->{
                    customProgressDialog.dismiss()
                    Utils.toastIconError(requireActivity(), response.message)
                    hideNoteText()
                }
                is Resource.Success->{
                    customProgressDialog.dismiss()
                    response.data?.let { competitionResponse ->
                        if (competitionResponse.status == 200){
                            if (competitionResponse.is_active == "true"){
                                hideNoteText()
                                showData(competitionResponse.concource)

                                val list = mutableListOf<Competition>()
                                list.add(Competition())
                                competitionResponse.statistic?.let { list.addAll(it) }


                                competitionAdapter.submitList(list)
                            }
                            else{
                                showNoteText()
                            }
                        }
                        else{
                            Utils.toastIconError(requireActivity(), getString(R.string.xatolik))
                            hideNoteText()
                        }
                    }
                }
            }
        })




    }


    private fun showData(concource: Concourse?) {
        concource?.let {
            Glide.with(requireView())
                .load(Constants.BASE_URL+concource.photo)
                .apply(options)
                .into(competition_image)

            competition_title.text = Html.fromHtml(concource.name)
            competition_text.text = Html.fromHtml(concource.full_desc)
            competition_start_time.text = concource.start?.let { Utils.reformatDateOnlyFromString(it) }
            competition_end_time.text = concource.finish?.let { it1 -> Utils.reformatDateOnlyFromString(it1)}
        }



    }

    private fun showNoteText() {
        competition_scrollview.visibility = View.GONE
        competition_note_tv.visibility = View.VISIBLE
        competition_note_tv.text = getString(R.string.hozircha_konkurslar_mavjud_emas)
    }

    private fun hideNoteText() {
        competition_scrollview.visibility = View.VISIBLE
        competition_note_tv.visibility = View.GONE
        competition_note_tv.text = getString(R.string.hozircha_konkurslar_mavjud_emas)
    }


    private var options: RequestOptions = RequestOptions()
        .placeholder(R.drawable.progress_animation)
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .fitCenter()
        .dontTransform()


    private fun initToolbar(){
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.konkurs)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }

}