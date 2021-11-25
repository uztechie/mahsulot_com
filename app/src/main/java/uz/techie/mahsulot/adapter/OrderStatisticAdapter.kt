package uz.techie.mahsulot.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.techie.mahsulot.R
import uz.techie.mahsulot.databinding.AdapterOrderStatisticsBinding
import uz.techie.mahsulot.databinding.AdapterSoldStreamStatisticsBinding
import uz.techie.mahsulot.databinding.AdapterStreamStatisticsBinding
import uz.techie.mahsulot.model.Order
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic
import uz.techie.mahsulot.util.Utils

class OrderStatisticAdapter(val mContext:Context) : RecyclerView.Adapter<OrderStatisticAdapter.StatisticViewHolder>() {
    var list = mutableListOf<StreamStatistic>()

    inner class StatisticViewHolder(private val binding: AdapterOrderStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Order) {
            val position = absoluteAdapterPosition+1
            binding.adapterStatTartibRaqami.text = position.toString()
            binding.adapterOrderNomi.text = data.product_name
            binding.adapterOrderSoni.text = data.amount.toString()
            binding.adapterOrderNarxi.text = "${Utils.toMoney(data.product_price!!)} ${mContext.getString(R.string.som)}"
            binding.adapterOrderIzoh.text = data.status_desc
            binding.adapterOrderHolat.text = Utils.streamStatus(data.status!!)
            binding.adapterOrderSana.text = Utils.reformatDateFromString(data.updated_at!!)



            if (position%2==0){
                binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_second))
            }
            else{
                binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }


        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Order>(){
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Order,
            newItem: Order
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val binding = AdapterOrderStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        if (position>-1) {
            holder.bind(differ.currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}