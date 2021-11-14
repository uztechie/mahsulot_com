package uz.techie.mahsulot.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority

import uz.techie.mahsulot.model.Product
import com.bumptech.glide.load.engine.DiskCacheStrategy


import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.adapter_product.view.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.Banner
import uz.techie.mahsulot.ui.fragments.HomeFragment
import uz.techie.mahsulot.ui.fragments.SliderFragment
import uz.techie.mahsulot.util.Utils
import java.lang.IllegalArgumentException


class ProductAdapter(val onOpenFragment: OnOpenFragment):RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {




    companion object{
        private const val TAG = "ProductAdapter"
        const val HEADER = 1
        const val BODY = 2
        const val TOP_PRODUCT = 3

    }


    inner class ProductViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        fun bindProduct(product: Product){
            itemView.apply {
                adapter_product_price.text = "${product.price?.let { Utils.toMoney(it) }} so'm"
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


        val fragmantContainer = itemView.findViewById<FrameLayout>(R.id.fragment_container)
        val topProductContainer = itemView.findViewById<FrameLayout>(R.id.top_product_container)

        init {
            itemView.setOnClickListener {
                if (differ.currentList[bindingAdapterPosition].id != null){
                    onOpenFragment.onItemClick(differ.currentList[bindingAdapterPosition])
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



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        Log.d(TAG, "onCreateViewHolder: viewtype "+viewType)
        if (viewType == HEADER){
            val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.container_fragment_slider, parent, false)
            return ProductViewHolder(view)
        }
        else if (viewType == TOP_PRODUCT){
            val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.top_product_container, parent, false)
            return ProductViewHolder(view)
        }
        else if (viewType == BODY){
            val view:View  = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product, parent, false)
            return ProductViewHolder(view)
        }
        else{
            return throw IllegalArgumentException("Invalid")
        }


    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: viewtype "+getItemViewType(position))

        if (getItemViewType(position) == HEADER){
            onOpenFragment.openFragment(holder.fragmantContainer.id, HEADER)
        }
        else if (getItemViewType(position) == TOP_PRODUCT){
            onOpenFragment.openFragment(holder.topProductContainer.id, TOP_PRODUCT)
        }

        else{
            val product = differ.currentList[position]
            holder.bindProduct(product)
        }





    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        Log.d(TAG, "getItemViewType: "+position)
        Log.d(TAG, "getItemViewType: type "+differ.currentList[position].viewType)
        if (differ.currentList[position].viewType == HEADER){
            return HEADER
        }
        else if (differ.currentList[position].viewType == TOP_PRODUCT){
            return TOP_PRODUCT
        }
        else {
            return BODY
        }


    }


    interface OnOpenFragment{
        fun openFragment(view:Int, viewType: Int)
        fun onItemClick(product: Product)
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