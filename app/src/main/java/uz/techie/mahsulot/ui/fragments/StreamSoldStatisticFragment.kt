package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_stream_product.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.android.synthetic.main.fragment_stream_statistic.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.adapter.StreamAdapter
import uz.techie.mahsulot.adapter.StreamSoldStatisticAdapter
import uz.techie.mahsulot.adapter.StreamStatisticAdapter
import uz.techie.mahsulot.dialog.ConfirmDialog
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.dialog.InfoDialog
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_STREAM


@AndroidEntryPoint
class StreamSoldStatisticFragment : Fragment(R.layout.fragment_stream_statistic) {
    private val viewModel: MahsulotViewModel by viewModels()
    lateinit var statisticAdapter: StreamSoldStatisticAdapter
    var token = ""
    private val TAG = "StreamSoldStatisticFragment3"
    lateinit var customProgressDialog: CustomProgressDialog
    lateinit var infoDialog:InfoDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customProgressDialog = CustomProgressDialog(requireContext())
        infoDialog = InfoDialog(requireContext())

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            user?.token?.let {
                token = "Token $it"

//                viewModel.loadSreamsStatistics("Token fa3e2a6c69c061df8cab7ba1edfe621e9f4df993")
                viewModel.loadSreamsStatistics(token)
            }
        })

        statisticAdapter = StreamSoldStatisticAdapter(requireContext())

        stream_statistic_recyclerview.apply {
//            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = statisticAdapter
        }

        viewModel.streamStatistics.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: " + response.data)
            when (response) {
                is Resource.Loading -> {
                    hideErrorText()
                    stream_statistic_progressbar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    hideErrorText()
                    stream_statistic_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.toastIconError(requireActivity(), it)
                    }
                }
                is Resource.Success -> {
                    stream_statistic_progressbar.visibility = View.GONE
                    response.data?.let { streamResponse ->
                        Log.d(TAG, "onViewCreated: streamsize "+streamResponse.message)
                        if (streamResponse.status == 200){
                            streamResponse.statistic?.let { statisicResponse->
                                val list = mutableListOf<StreamStatistic>()
                                list.add(StreamStatistic(id = -2))
                                list.addAll(statisicResponse)
                                statisticAdapter.differ.submitList(list)

                                if (statisicResponse.isEmpty()) {
                                    showErrorText(getString(R.string.statistika_mavjud_emas))
                                } else {
                                    hideErrorText()
                                }

                            }
                        }
                        else{
                            hideErrorText()
                            streamResponse.message?.let { Utils.toastIconError(requireActivity(), it) }
                        }

                    }
                }
            }

        })


    }

    private fun initToolbar() {
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.oqimlar_statistikasi)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }



    private fun showErrorText(message: String) {
        infoDialog.show()
        infoDialog.submitData(message)
    }

    private fun hideErrorText() {
        infoDialog.dismiss()
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }



}