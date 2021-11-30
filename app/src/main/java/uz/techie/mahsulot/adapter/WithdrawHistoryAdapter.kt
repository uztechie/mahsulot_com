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
import uz.techie.mahsulot.databinding.AdapterWithdrawHistoryBinding
import uz.techie.mahsulot.model.Order
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic
import uz.techie.mahsulot.model.Withdraw
import uz.techie.mahsulot.util.Utils

class WithdrawHistoryAdapter(val mContext:Context) : RecyclerView.Adapter<WithdrawHistoryAdapter.StatisticViewHolder>() {
    var list = mutableListOf<StreamStatistic>()

    inner class StatisticViewHolder(private val binding: AdapterWithdrawHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Withdraw) {
            val position = absoluteAdapterPosition+1
            binding.adapterWithdrawTartibRaqami.text = position.toString()
            binding.adapterWithdrawCardnumber.text = data.payment_card
            binding.adapterWithdrawAmount.text = "${Utils.toMoney(data.amount!!)}"
            binding.adapterWithdrawDate.text = Utils.reformatDateFromString(data.updated_at!!)
            binding.adapterWithdrawHolat.text = data.status_name
            binding.adapterWithdrawIzoh.text = data.desc_name



            if (position%2==0){
                binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white_second))
            }
            else{
                binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }


        }

    }

    private val differCallback = object : DiffUtil.ItemCallback<Withdraw>(){
        override fun areItemsTheSame(oldItem: Withdraw, newItem: Withdraw): Boolean {
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Withdraw,
            newItem: Withdraw
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val binding = AdapterWithdrawHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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