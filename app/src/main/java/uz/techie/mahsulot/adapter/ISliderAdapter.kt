package uz.techie.mahsulot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.Banner

class ISliderAdapter:PagerAdapter() {

    private var sliderList = mutableListOf<Banner>()
    fun setData(sliderList: MutableList<Banner>){
        this.sliderList = sliderList
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageUrl = sliderList[position].photo
        val view = LayoutInflater.from(container.context).inflate(R.layout.islider_item, container, false)
        val imageView = view.findViewById<ImageView>(R.id.islider_item_imageview)

        Glide.with(container.context)
            .load(imageUrl)
            .apply(options)
            .into(imageView)

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    private var options: RequestOptions = RequestOptions()
        .placeholder(R.drawable.loading)
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()


}