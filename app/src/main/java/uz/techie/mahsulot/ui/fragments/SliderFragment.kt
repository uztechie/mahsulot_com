package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.afdhal_fa.imageslider.model.SlideUIModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_slider.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.SliderAdapter
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.ISliderModel
import uz.techie.mahsulot.util.IsliderImage

class SliderFragment:Fragment(R.layout.fragment_slider) {

    lateinit var sliderAdapter: SliderAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        sliderAdapter = SliderAdapter(sliderList())
//        cardSlider.adapter = sliderAdapter

        var isliderImage = IsliderImage(requireActivity(),requireView())
        isliderImage.sliderDelay(200)
        isliderImage.autoStart(false)
        isliderImage.setData(isliderList())



        imageSlider.setImageList(slider2List())

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


    private fun slider2List(): MutableList<SlideUIModel> {
        val list = mutableListOf<SlideUIModel>()
        val img1 =
            "https://images.pexels.com/photos/2101137/pexels-photo-2101137.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img2 =
            "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        val img3 =
            "https://images.pexels.com/photos/699122/pexels-photo-699122.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img4 =
            "https://images.pexels.com/photos/1194760/pexels-photo-1194760.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"

        list.add(SlideUIModel(img1, "title"))
        list.add(SlideUIModel(img2, "title"))
        list.add(SlideUIModel(img3, "title"))
        list.add(SlideUIModel(img4, "title"))


        return list
    }

    private fun isliderList(): MutableList<ISliderModel> {
        val list = mutableListOf<ISliderModel>()
        val img1 =
            "https://images.pexels.com/photos/2101137/pexels-photo-2101137.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img2 =
            "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        val img3 =
            "https://images.pexels.com/photos/699122/pexels-photo-699122.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"
        val img4 =
            "https://images.pexels.com/photos/1194760/pexels-photo-1194760.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"

        list.add(ISliderModel(img1, "title"))
        list.add(ISliderModel(img2, "title"))
        list.add(ISliderModel(img3, "title"))
        list.add(ISliderModel(img4, "title"))


        return list
    }


}