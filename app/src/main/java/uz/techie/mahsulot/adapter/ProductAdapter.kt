package uz.techie.mahsulot.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import kotlinx.android.synthetic.main.adapter_product.view.*

import uz.techie.mahsulot.model.Product
import com.bumptech.glide.load.engine.DiskCacheStrategy


import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.adapter_product_header.view.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.model.MainModel


class ProductAdapter:RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    companion object{
        const val HEADER = 1
        const val BODY = 2
    }



    private val differCallback = object: DiffUtil.ItemCallback<MainModel>(){
        override fun areItemsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
            return oldItem.type == newItem.type && oldItem.list == newItem.list && oldItem.product?.id == newItem.product?.id
        }

        override fun areContentsTheSame(oldItem: MainModel, newItem: MainModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        if (viewType == HEADER){
            val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product_header, parent, false)
            return ProductViewHolder(view)
        }
        else{
            val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product, parent, false)
            return ProductViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val mainModel = differ.currentList[position]
        if (mainModel.type == HEADER){
            holder.bindHeader(mainModel.list as MutableList<Banner>)
        }


        mainModel.product?.let { holder.bindBody(it) }




    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList[position].type == HEADER){
            return HEADER
        }
        else{
            return BODY
        }
    }

    inner class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        fun bindHeader(list: MutableList<Banner>){
            val sliderAdapter = SliderAdapter(list)
            itemView.cardSlider.adapter = sliderAdapter
        }
        fun bindBody(product: Product){
            itemView.apply {
                adapter_product_price.text = "${product.price}"
                adapter_product_title.text = product.name

                Glide.with(this)
                    .load(product.photo)
                    .apply(options)
                    .into(adapter_product_image)

                Log.d("TAG", "onBindViewHolder:  apply "+product.name)
                Log.d("TAG", "onBindViewHolder:  position "+position)
                Log.d("TAG", "onBindViewHolder: size "+itemCount)
            }
        }
    }


    private var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .override(800,300)
        .fitCenter()
        .dontTransform()




}