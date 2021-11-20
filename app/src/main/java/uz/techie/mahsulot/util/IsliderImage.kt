package uz.techie.mahsulot.util

import android.app.Activity
import android.graphics.PorterDuff
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import uz.techie.mahsulot.R
import uz.techie.mahsulot.adapter.ISliderAdapter
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.ISliderModel

class IsliderImage(val activity: Activity, val view:View) {
    private var viewPager:ViewPager
    private  var tvTitle:TextView
    private  var linearDots:LinearLayout
    private  var sliderAdapter: ISliderAdapter
    private  var handler: Handler
    private val TAG = "IsliderImage"
    private val sliderDelay:MutableLiveData<Long> = MutableLiveData(3000)
    private val autoStartLive:MutableLiveData<Boolean> = MutableLiveData(true)

    private var sliderList = mutableListOf<Banner>()


    init {
        viewPager = view.findViewById(R.id.iSlider_view_pager)
        tvTitle = view.findViewById(R.id.iSlider_title)
        linearDots = view.findViewById(R.id.iSlider_linear_dots)

        sliderAdapter = ISliderAdapter()
        viewPager.adapter = sliderAdapter


        handler = Handler(Looper.getMainLooper())

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                addBottomDots(sliderList.size, position)

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == 1 && autoStartLive.value!!){
                        stopSlider()
                        startSlider()
                }
            }

        })


    }

    fun setData(list:MutableList<Banner>){
        sliderAdapter.setData(list)
        sliderList.clear()
        sliderList.addAll(list)

        if (sliderList.isNotEmpty()){
            addBottomDots(sliderList.size, 0)
        }


    }
    fun autoStart(autoStart:Boolean){
        autoStartLive.postValue(autoStart)
        if (autoStart){
            startSlider()
        }
    }
    fun sliderDelay(delay:Long){
        sliderDelay.postValue(delay)
    }


    private fun addBottomDots(size:Int, currentPosition:Int){
        val dots = arrayOfNulls<ImageView>(size)

        linearDots.removeAllViews()
        for (i in dots.indices){
            dots[i] = ImageView(activity)
            val widthHeight = 18
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
            params.setMargins(10,0,10,0)
            dots[i]?.layoutParams = params
            dots[i]?.setImageResource(R.drawable.dots_bg)

            linearDots.addView(dots[i])
        }

        if (dots.isNotEmpty()){
            dots[currentPosition]?.setColorFilter(ContextCompat.getColor(activity, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun startSlider(){
        Log.d(TAG, "startSlider: ")
        handler.postDelayed(runnable, sliderDelay.value!!)
    }

    private fun stopSlider(){
        Log.d(TAG, "stopSlider: ")
        handler.removeCallbacks(runnable)
    }


    private val runnable:Runnable = object :Runnable{
        override fun run() {
            var currentPosition = viewPager.currentItem?:0
            val totalItems = sliderList.size

            currentPosition +=1

            if (currentPosition >= totalItems){
                currentPosition = 0
            }
            viewPager.currentItem = currentPosition
            handler.postDelayed(this, sliderDelay.value!!)


        }

    }





}