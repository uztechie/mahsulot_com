package uz.techie.mahsulot.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.PieChart
import uz.techie.mahsulot.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData

import com.github.mikephil.charting.utils.ColorTemplate

import com.github.mikephil.charting.data.PieDataSet

import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_profile.*
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.ProfileResponse
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.model.UserData
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils


@AndroidEntryPoint
class ProfileFragment:Fragment(R.layout.fragment_profile) {
    lateinit var chart:PieChart
    val viewModel: MahsulotViewModel by viewModels()
    private val TAG = "ProfileFragment"
    lateinit var customProgressDialog: CustomProgressDialog
    private var user:User? = null
    private var token:String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        customProgressDialog = CustomProgressDialog(requireContext())

        initPieChart(view)

        viewModel.profile.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: "+response.data)
            when(response){
                is Resource.Loading->{
                    customProgressDialog.show()
                }
                is Resource.Error ->{
                    customProgressDialog.dismiss()
                    showMessage("Error: " + response.message)
                }
                is Resource.Success->{
                    customProgressDialog.dismiss()
                    response.data?.let {
                        showProfileData(it)
                    }

                }
            }


        })
        viewModel.getUser().observe(viewLifecycleOwner, Observer { mUser->
            mUser.token?.let {
                token = "Token $it"

                Log.d(TAG, "onViewCreated: token "+token)
                viewModel.loadProfile(token)
            }
        })







    }

    private fun showProfileData(profile: ProfileResponse) {
        profile.data?.let { data->
            profile_main_balance_tv.text = "${Utils.toMoney(data.hisobi)} ${getString(R.string.som)}"
            profile_hold_balance_tv.text = "${Utils.toMoney(data.hold)} ${getString(R.string.som)}"
            profile_sold_tv.text = "${Utils.toMoney(data.muzlatildi)} ${getString(R.string.ta)}"
            profile_marja_balance_tv.text = "${Utils.toMoney(data.marja)} ${getString(R.string.ta)}"

            submitPieChartData(data)

        }
    }

    private fun submitPieChartData(data: UserData) {
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(data.yangi.toFloat(), getString(R.string.yangi)))
        entries.add(PieEntry(data.qabul_qilindi.toFloat(), getString(R.string.qabul_qilindi)))
        entries.add(PieEntry(data.yetkazib_berildi.toFloat(), getString(R.string.yetkazib_berildi)))
        entries.add(PieEntry(data.yetkazilmoqda.toFloat(), getString(R.string.yetkazilmoqda)))
        entries.add(PieEntry(data.spam.toFloat(), getString(R.string.spam)))
        entries.add(PieEntry(data.arxiv.toFloat(), getString(R.string.arxiv)))
        entries.add(PieEntry(data.arxivlanmoqda.toFloat(), getString(R.string.arxivlanmoqda)))
        entries.add(PieEntry(data.bekor_qilindi.toFloat(), getString(R.string.bekor_qilindi)))
        entries.add(PieEntry(data.qayta_qungiroq.toFloat(), getString(R.string.qayta_qongiroq)))

        val colors = ArrayList<Int>()
        colors.add(resources.getColor(R.color.color1))
        colors.add(resources.getColor(R.color.color2))
        colors.add(resources.getColor(R.color.color3))
        colors.add(resources.getColor(R.color.color4))
        colors.add(resources.getColor(R.color.color5))
        colors.add(resources.getColor(R.color.color6))
        colors.add(resources.getColor(R.color.color7))
        colors.add(resources.getColor(R.color.color8))
        colors.add(resources.getColor(R.color.color9))


        val ds1 = PieDataSet(entries,null)
        ds1.colors = colors
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 10f
        val d = PieData(ds1)

        chart.data = d
        chart.invalidate()

    }


    private fun showMessage(message: String?) {
        message?.let {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
        }
    }


    private fun initPieChart(view: View){
        chart = view.findViewById(R.id.profile_pieChart)

        chart.description.isEnabled = false
        chart.centerText = "Mahsulot.com"
        chart.setCenterTextSize(10f)
        chart.holeRadius = 45f
        chart.transparentCircleRadius = 50f


        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)

        chart.data = generatePieData()
        chart.invalidate()
    }

    protected fun generatePieData(): PieData {

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(0f, getString(R.string.yangi)))
        entries.add(PieEntry(0f, getString(R.string.qabul_qilindi)))
        entries.add(PieEntry(0f, getString(R.string.yetkazib_berildi)))
        entries.add(PieEntry(0f, getString(R.string.yetkazilmoqda)))
        entries.add(PieEntry(0f, getString(R.string.spam)))
        entries.add(PieEntry(0f, getString(R.string.arxiv)))
        entries.add(PieEntry(0f, getString(R.string.arxivlanmoqda)))
        entries.add(PieEntry(0f, getString(R.string.bekor_qilindi)))
        entries.add(PieEntry(0f, getString(R.string.qayta_qongiroq)))

        val colors = ArrayList<Int>()
        colors.add(resources.getColor(R.color.color1))
        colors.add(resources.getColor(R.color.color2))
        colors.add(resources.getColor(R.color.color3))
        colors.add(resources.getColor(R.color.color4))
        colors.add(resources.getColor(R.color.color5))
        colors.add(resources.getColor(R.color.color6))
        colors.add(resources.getColor(R.color.color7))
        colors.add(resources.getColor(R.color.color8))
        colors.add(resources.getColor(R.color.color9))


        val ds1 = PieDataSet(entries,null)
        ds1.colors = colors
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 12f
        val d = PieData(ds1)
        return d
    }




    private fun initToolbar(){
        toolbar_title.text = getString(R.string.xisobotlar)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }



}