package uz.techie.mahsulot.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.techie.mahsulot.R
import uz.techie.mahsulot.databinding.AdapterSoldStreamStatisticsBinding
import uz.techie.mahsulot.databinding.AdapterStreamStatisticsBinding
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic
import uz.techie.mahsulot.util.Utils

class StreamSoldStatisticAdapter(val mContext: Context) :
    RecyclerView.Adapter<StreamSoldStatisticAdapter.StatisticViewHolder>() {
    var list = mutableListOf<StreamStatistic>()

    inner class StatisticViewHolder(private val binding: AdapterSoldStreamStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StreamStatistic) {
            val position = absoluteAdapterPosition
            binding.adapterStatTartibRaqami.text = "№ ${data.id}"
            binding.adapterStatOperator.text = data.operator.toString()
            binding.adapterStatNomi.text = data.stream_name
            binding.adapterStatBuyurtmachi.text =
                "${data.customer_name}\n${Utils.hidePhoneNumber(data.customer_phone!!)}"
            binding.adapterStatManzil.text = Utils.selectRegion(data.customer_region!!)
            binding.adapterStatHolat.text = Utils.streamStatus(data.status!!)
            binding.adapterStatIzoh.text = data.desc
            binding.adapterStatSana.text = Utils.reformatDateFromString(data.updated_at!!)

            if (position % 2 == 0) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.white_second
                    )
                )
            } else {
                binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }


        }

        fun bindHeader() {
            binding.adapterStatTartibRaqami.text = "#"
            binding.adapterStatNomi.text = "Nomi"
            binding.adapterStatBuyurtmachi.text = "Buyurtmachi"
            binding.adapterStatManzil.text = "Manzil"
            binding.adapterStatHolat.text = "Holat"
            binding.adapterStatIzoh.text = "Izoh"
            binding.adapterStatSana.text = "Sana"
            binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_blue))

            binding.adapterStatTartibRaqami.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatNomi.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatBuyurtmachi.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatManzil.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatHolat.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatIzoh.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatSana.setTextColor(ContextCompat.getColor(mContext, R.color.white))


        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<StreamStatistic>() {
        override fun areItemsTheSame(oldItem: StreamStatistic, newItem: StreamStatistic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StreamStatistic,
            newItem: StreamStatistic
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val binding = AdapterSoldStreamStatisticsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StatisticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {

        holder.bind(differ.currentList[position])

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}