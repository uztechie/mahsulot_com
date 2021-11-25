package uz.techie.mahsulot.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatisticPagerAdapter(fa:FragmentActivity) : FragmentStateAdapter(fa) {

    var fragmentList = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun submitList(fragments:MutableList<Fragment>){
        this.fragmentList.clear()
        this.fragmentList.addAll(fragments)
        notifyDataSetChanged()
    }


}