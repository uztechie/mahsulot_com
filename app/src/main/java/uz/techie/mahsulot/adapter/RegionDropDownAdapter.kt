package uz.techie.mahsulot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import uz.techie.mahsulot.R
import uz.techie.mahsulot.model.Region

class RegionDropDownAdapter(context: Context, resource:Int, regions:List<Region>)
    :ArrayAdapter<Region>(context, 0, regions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup):View{
        var view = convertView
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.adapter_dropdown, parent, false)
        }

        val tvName = view!!.findViewById<TextView>(R.id.adapter_region_name)
        tvName.text = getItem(position)?.name
        return view
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults? {
            return null
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {}
        override fun convertResultToString(resultValue: Any): CharSequence {
            return (resultValue as Region).name

        }
    }



}