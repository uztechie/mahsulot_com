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
import uz.techie.mahsulot.R


class ProductAdapter:RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {}

    private val differCallback = object: DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.itemView.apply {
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

    override fun getItemCount(): Int {
        return differ.currentList.size
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