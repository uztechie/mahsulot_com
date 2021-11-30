package uz.techie.mahsulot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.techie.mahsulot.R
import uz.techie.mahsulot.databinding.AdapterCompetitionBinding
import uz.techie.mahsulot.model.Competition

class CompetitionAdapter(val mContext:Context): ListAdapter<Competition, CompetitionAdapter.CompetitionViewHolder>(ItemComparator()) {

    private class ItemComparator : DiffUtil.ItemCallback<Competition>() {
        override fun areItemsTheSame(oldItem: Competition, newItem: Competition): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Competition, newItem: Competition): Boolean {
            return oldItem == newItem
        }

    }

    inner class CompetitionViewHolder(private val binding: AdapterCompetitionBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bindHeader(){
                binding.adapterCompetitionPlace.text = mContext.getString(R.string.orin)
                binding.adapterCompetitionPhone.text = mContext.getString(R.string.sotuvchi)
                binding.adapterCompetitionSell.text = mContext.getString(R.string.sotildi)
            }
            fun bindBody(competition: Competition){
                binding.adapterCompetitionPlace.text = competition.count.toString()
                binding.adapterCompetitionPhone.text = competition.phone
                binding.adapterCompetitionSell.text = competition.sell.toString()
            }

        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompetitionViewHolder {
        val binding = AdapterCompetitionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CompetitionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CompetitionViewHolder, position: Int) {
        if (position == 0){
            holder.bindHeader()
        }
        else{
            holder.bindBody(getItem(position))
        }
    }

}