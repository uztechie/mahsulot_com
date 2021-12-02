package uz.techie.mahsulot.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Priority

import com.bumptech.glide.load.engine.DiskCacheStrategy


import com.bumptech.glide.request.RequestOptions
import uz.techie.mahsulot.R
import uz.techie.mahsulot.databinding.AdapterStreamBinding
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.util.Utils


class StreamAdapter(val mContext: Context, val listener: StreamListener) :
    RecyclerView.Adapter<StreamAdapter.StreamHolder>() {

    companion object {
        private const val TAG = "StreamAdapter"

    }


    inner class StreamHolder(private val binding: AdapterStreamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindStream(stream: Stream) {
            binding.apply {
                adapterStreamPrice.text =
                    "${mContext.getString(R.string.narx)}: ${stream.price?.let { Utils.toMoney(it) }} so'm"
                adapterStreamBonus.text =
                    "${mContext.getString(R.string.bonus)}: ${stream.bonus?.let { Utils.toMoney(it) }} so'm"
                adapterStreamTitle.text = stream.name


                if (stream.reklama_bot == "0"){
                    adapterStreamLinkSpecialTg.visibility = View.GONE
                    adapterStreamLinkSimpleTg.visibility = View.GONE
                }


                var simpleLink = ""
                var specialLink = ""
                var simpleTgLink = ""
                var specialTgLink = ""

                if (stream.url.isNullOrEmpty()) {
                    simpleLink = "https://mahsulot.com/oqim/${stream.id}"
                    specialLink = "https://mahsulot.com/n/${stream.id}"
                    simpleTgLink = "tg://resolve?domain=mahsulot_com_rasmiy_bot&start=oqim_"+stream.id+"_"+stream.reklama_bot
                    specialTgLink = "tg://resolve?domain=mahsulot_com_rasmiy_bot&start=n_"+stream.id+"_"+stream.reklama_bot



                } else {
                    simpleLink = "https://mahsulot.com/m/${stream.url}"
                    specialLink = "https://mahsulot.com/l/${stream.url}"

                    simpleTgLink = "tg://resolve?domain=mahsulot_com_rasmiy_bot&start=m_"+stream.url+"_"+stream.reklama_bot
                    specialTgLink = "tg://resolve?domain=mahsulot_com_rasmiy_bot&start=l_"+stream.url+"_"+stream.reklama_bot
                }

                adapterStreamLinkSimple.text = simpleLink
                adapterStreamLinkSpecial.text = specialLink
                adapterStreamLinkSimpleCopy.text = simpleLink
                adapterStreamLinkSpecialCopy.text = specialLink


                adapterStreamLinkSimpleBtn.setOnClickListener {
                    Utils.copyTextToClipboard(mContext, simpleLink)
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.sahifa_nusxalandi),
                        Toast.LENGTH_SHORT
                    ).show()


                    animateText(adapterStreamLinkSimpleCopy)


                }

                adapterStreamLinkSpecialBtn.setOnClickListener {
                    Utils.copyTextToClipboard(mContext, specialLink)
                    animateText(adapterStreamLinkSpecialCopy)
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.sahifa_nusxalandi),
                        Toast.LENGTH_SHORT
                    ).show()
                }





                adapterStreamAdTg.setOnClickListener {
                    stream.reklama?.let {
                        listener.onClickAdLink(it)
                    }
                }

                adapterStreamDeleteBtn.setOnClickListener {
                    stream.id?.let {
                        listener.onClickDelete(it)
                    }
                }

                adapterStreamLinkSimpleTg.setOnClickListener {
                    listener.onSimpleTgLinkClick(simpleTgLink)
                }

                adapterStreamLinkSpecialTg.setOnClickListener {
                    listener.onSpecialTgLinkClick(specialTgLink)
                }



            }
        }
    }


    private val differCallback = object : DiffUtil.ItemCallback<Stream>() {
        override fun areItemsTheSame(oldItem: Stream, newItem: Stream): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Stream, newItem: Stream): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamHolder {
        val binding =
            AdapterStreamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StreamHolder(binding)
    }

    override fun onBindViewHolder(holder: StreamHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: viewtype " + getItemViewType(position))


        val stream = differ.currentList[position]
        if (stream.status!! == "true") {
            holder.bindStream(stream)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    interface StreamListener {
        fun onClickAdLink(link: String)
        fun onClickDelete(id: Int)
        fun onSimpleTgLinkClick(link:String)
        fun onSpecialTgLinkClick(link:String)
    }


    private var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.progress_animation)
        .error(R.drawable.no_image)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)
        .dontAnimate()
        .override(800, 300)
        .fitCenter()
        .dontTransform()


    private fun animateX(view: View) {
        view.animate()
            .translationX(0f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun animateText(view: View) {

        view.translationY = 0f
        view.alpha = 1f
        view.scaleX = 1f
        view.scaleY = 1f

        view.animate()
            .translationY(-50f)
            .setDuration(600)
            .scaleX(1.1f)
            .scaleY(1.1f)
            .alphaBy(1f)
            .alpha(0f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun reAnimateX(view: View) {
        view.animate()
            .translationX(1000f)
            .setDuration(300)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }


}