package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_product_details.*
import uz.techie.mahsulot.MainActivity
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.ISliderModel
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.util.IsliderImage
import uz.techie.mahsulot.util.Utils
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

import androidx.annotation.NonNull


class ProductDetailsFragment:Fragment(R.layout.fragment_product_details) {
    var product = Product()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).updateStatusLight()

        product = arguments?.let { ProductDetailsFragmentArgs.fromBundle(it).product }!!


        val sliderList = mutableListOf<SlideUIModel>()
        sliderList.add(SlideUIModel(product.photo!!))
        product.gallary!!.forEach { gallery->
            sliderList.add(SlideUIModel(gallery.image))
        }
        imageSlide.setImageList(sliderList)

        val isliderList = mutableListOf<ISliderModel>()
        isliderList.add(ISliderModel(product.photo!!))
        product.gallary!!.forEach { gallery->
            isliderList.add(ISliderModel(gallery.image))
        }


        val isliderImage = IsliderImage(requireActivity(), requireView())
        isliderImage.autoStart(false)
        isliderImage.setData(isliderList)



        details_title.text = product.name
        details_short_desc.text = product.desc
        details_full_desc_tv.text = Html.fromHtml(product.full_desc)
        details_price.text = "${Utils.toMoney(product.price!!)} So'm"
        details_remained.text = "Qoldi: ${product.remained} ta"

        elegantNumber.setRange(0, 0)
        product.remained?.let {
            if (it>0){
                elegantNumber.setRange(1, product.remained)
                details_buy_btn.isEnabled = true
            }
            else{
                elegantNumber.number = "0"
                elegantNumber.setRange(0,0)
                details_buy_btn.isEnabled = false
            }

        }

        initYoutubePlayer()


        calculatePrice(1)

        elegantNumber.setOnValueChangeListener { view, oldValue, newValue ->
            calculatePrice(newValue)
        }





        details_full_desc_btn.setOnClickListener {
            if (details_full_desc_tv.visibility == View.GONE){
                details_full_desc_tv.visibility = View.VISIBLE
                details_full_desc_btn.icon = resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24)
            }
            else{
                details_full_desc_tv.visibility = View.GONE
                details_full_desc_btn.icon = resources.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24)
            }
        }



        initToolbar()




    }

    private fun initYoutubePlayer() {
        product.youtube_link?.let {
            youtube_player_view.visibility = View.VISIBLE
            viewLifecycleOwner.lifecycle.addObserver(youtube_player_view)
            youtube_player_view.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = it.substringAfterLast('/')
                    youTubePlayer.loadVideo(videoId, 0f)
                    youTubePlayer.play()

                    Log.d("TAG", "onReady:  "+videoId)
                }
            })

        }
    }

    fun calculatePrice(quantity:Int){
        product.price?.let {
            val totalPrice = quantity*it
            details_buy_btn.text = "${getString(R.string.harid_qilish)}  (${Utils.toMoney(totalPrice)} ${getString(R.string.som)})"
        }

    }

    private fun initToolbar(){
//        toolbar_title.text = getString(R.string.kataloglar)
//        toolbar_title.visibility = View.VISIBLE

        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.setOnClickListener {
            findNavController().navigate(ProductDetailsFragmentDirections.actionGlobalSearchFragment())
        }

    }



    override fun onPause() {
        super.onPause()
        (activity as MainActivity).updateStatusBarDark()
    }

}