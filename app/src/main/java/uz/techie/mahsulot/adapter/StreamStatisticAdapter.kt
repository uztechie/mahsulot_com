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
import uz.techie.mahsulot.databinding.AdapterStreamStatisticsBinding
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic

class StreamStatisticAdapter(val mContext: Context) :
    RecyclerView.Adapter<StreamStatisticAdapter.StatisticViewHolder>() {
    var list = mutableListOf<Stream>()

    inner class StatisticViewHolder(private val binding: AdapterStreamStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Stream) {
            binding.adapterStatNumbers.text = (absoluteAdapterPosition).toString()
            if (data.id == -5){
                binding.adapterStatNumbers.text = "-"
            }


            binding.adapterStatNomi.text = data.name
            binding.adapterStatTashrif.text = data.click.toString()
            binding.adapterStatYangi.text = data.yangi.toString()
            binding.adapterStatQabulqilindi.text = data.qabul_qilindi.toString()
            binding.adapterStatYetkazilmoqda.text = data.yetkazilmoqda.toString()

            binding.adapterStatYetkazibBerildi.text = data.yetkazilmoqda.toString()
            binding.adapterStatQaytaQongiroq.text = data.qayta_qungiroq.toString()
            binding.adapterStatNosozMahsulot.text = data.nosoz_mahsulot.toString()
            binding.adapterStatBekorQilindi.text = data.muzlatildi.toString()
            binding.adapterStatSpam.text = data.spam.toString()
            binding.adapterStatArxivlanmoqda.text = data.arxivlanmoqda.toString()
            binding.adapterStatArxivlandi.text = data.arxivlandi.toString()

            if (absoluteAdapterPosition % 2 == 0) {
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
            binding.adapterStatNomi.text = "Nomi"
            binding.adapterStatTashrif.text = "Tashrif"
            binding.adapterStatYangi.text = "Yangi"
            binding.adapterStatQabulqilindi.text = "Qabul qilindi"
            binding.adapterStatYetkazilmoqda.text = "Yetkazilmoqda"
            binding.adapterStatYetkazibBerildi.text = "Yetkazib berildi"
            binding.adapterStatQaytaQongiroq.text = "Qayta qo'g'iroq"
            binding.adapterStatNosozMahsulot.text = "Nosoz mahsulot"
            binding.adapterStatBekorQilindi.text = "Bekor qilindi"
            binding.adapterStatSpam.text = "Spam"
            binding.adapterStatArxivlanmoqda.text = "Arxivlanmoqda"
            binding.adapterStatArxivlandi.text = "Arxivlandi"
            binding.root.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_blue))

            binding.adapterStatNomi.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatTashrif.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatYangi.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatQabulqilindi.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatYetkazilmoqda.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatYetkazibBerildi.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatQaytaQongiroq.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatNosozMahsulot.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatBekorQilindi.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatSpam.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            binding.adapterStatArxivlanmoqda.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
            binding.adapterStatArxivlandi.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )

        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Stream>() {
        override fun areItemsTheSame(oldItem: Stream, newItem: Stream): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Stream,
            newItem: Stream
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val binding = AdapterStreamStatisticsBinding.inflate(
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

    fun submitList(streamList: MutableList<Stream>) {
        this.list.clear()
        this.list.addAll(streamList)
        notifyDataSetChanged()
    }


}