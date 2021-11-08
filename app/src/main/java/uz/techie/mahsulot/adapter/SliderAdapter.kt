package uz.techie.mahsulot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.adapter_slider_card.view.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.Banner

class SliderAdapter(val list:List<Banner>):CardSliderAdapter<SliderAdapter.SliderViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_slider_card, parent, false)
        return SliderViewHolder(view)
    }

    override fun bindVH(holder: SliderViewHolder, position: Int) {
        val banner = list[position]
        holder.itemView.apply {
            Glide.with(holder.itemView)
                .load(banner.url)
                .apply(options)
                .into(adapter_slider_card_image)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class SliderViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    private var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()



}