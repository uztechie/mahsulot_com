package uz.techie.mahsulot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.adapter_category.view.*
import kotlinx.android.synthetic.main.adapter_product.view.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.Category

class CategoryAdapter:ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(CategoryComparator()) {

    private class CategoryComparator:DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.adapter_category,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.itemView.apply {
            adapter_category_title.text = getItem(position).title

            Glide.with(holder.itemView)
                .load(getItem(position).image)
                .apply(options)
                .into(adapter_category_image)

        }
    }

    inner class CategoryViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){}

    private var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

}