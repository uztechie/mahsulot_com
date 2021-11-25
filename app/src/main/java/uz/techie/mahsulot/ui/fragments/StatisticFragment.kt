package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_statistic.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.StatisticPagerAdapter

class StatisticFragment : Fragment(R.layout.fragment_statistic) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        val pagerAdapter = StatisticPagerAdapter(requireActivity())
        val fragment2 = StreamStatisticFragment()
        val fragment3 = StreamSoldStatisticFragment()
        pagerAdapter.submitList(mutableListOf(fragment2, fragment3))
        view_pager.adapter = pagerAdapter
        view_pager.isUserInputEnabled = false


        TabLayoutMediator(tab_layout, view_pager){tab, position ->
            when(position){
                0->{
                    tab.text = getString(R.string.umumiy_oqimlar)
                }
                1->{
                    tab.text = getString(R.string.oqimlar_savdosi)
                }
            }
        }.attach()


    }

    private fun initToolbar() {
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.savdo_tarixi)
        toolbar_title.visibility = View.GONE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }

}