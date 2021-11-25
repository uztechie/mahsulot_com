package uz.techie.mahsulot.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority

import uz.techie.mahsulot.model.Product
import com.bumptech.glide.load.engine.DiskCacheStrategy


import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.adapter_product.view.*
import kotlinx.android.synthetic.main.adapter_stream_product.view.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.util.Utils
import java.lang.IllegalArgumentException


class ProductStreamAdapter(val mContext: Context, val listener: ProductStreamListener):RecyclerView.Adapter<ProductStreamAdapter.ProductStreamHolder>() {

    companion object{
        private const val TAG = "ProductAdapter"

    }


    inner class ProductStreamHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        fun bindProduct(product: Product){
            itemView.apply {
                adapter_stream_product_price.text = "${mContext.getString(R.string.narx)}: ${product.price?.let { Utils.toMoney(it) }} so'm"
                adapter_stream_product_bonus.text = "${mContext.getString(R.string.bonus)}: ${product.bonus?.let { Utils.toMoney(it) }} so'm"
                adapter_stream_product_title.text = product.name
                adapter_stream_product_remained.text = "${mContext.getString(R.string.qoldi)}: ${product.remained}"
                adapter_stream_product_marja.text = "${mContext.getString(R.string.marja)}: ${product.marja_amount}"

                Glide.with(this)
                    .load(product.photo)
                    .apply(options)
                    .into(adapter_stream_product_image)


                adapter_stream_product_ad_tg.setOnClickListener {
                    product.reklama_link?.let {
                        listener.onAdLink(it)
                    }
                }

                adapter_stream_product_stream.setOnClickListener {
                    listener.onCreateStream(product)
                }

            }
        }
    }


    private val differCallback = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductStreamHolder {
        Log.d(TAG, "onCreateViewHolder: viewtype "+viewType)
        val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.adapter_stream_product, parent, false)
        return ProductStreamHolder(view)
    }

    override fun onBindViewHolder(holder: ProductStreamHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: viewtype "+getItemViewType(position))


            val product = differ.currentList[position]
            if (product.status!! == "True"){
                holder.bindProduct(product)
            }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    interface ProductStreamListener{
        fun onAdLink(link: String)
        fun onCreateStream(product: Product)
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