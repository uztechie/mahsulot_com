package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.adapter_product_header.view.*
import kotlinx.android.synthetic.main.fragment_slider.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.model.Banner

class SliderFragment :Fragment(R.layout.fragment_slider) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sliderAdapter = SliderAdapter(sliderList())
        cardSlider.adapter = sliderAdapter
    }


    private fun sliderList(): List<Banner> {
        val list = mutableListOf<Banner>()
        val img1 =
            "https://images.pexels.com/photos/2101137/pexels-photo-2101137.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img2 =
            "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        val img3 =
            "https://images.pexels.com/photos/699122/pexels-photo-699122.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img4 =
            "https://images.pexels.com/photos/1194760/pexels-photo-1194760.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        list.add(Banner(1, img1, "title"))
        list.add(Banner(1, img2, "title"))
        list.add(Banner(1, img3, "title"))
        list.add(Banner(1, img4, "title"))

        return list
    }

}