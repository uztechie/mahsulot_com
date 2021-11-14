package uz.techie.mahsulot.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import kotlinx.android.synthetic.main.dialog_info.*
import uz.techie.mahsulot.R

class InfoDialog(context: Context) : Dialog(context) {
    init {
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(0))
        setContentView(R.layout.dialog_info)


        info_btnOk.setOnClickListener {
            dismiss()
        }

    }

    fun submitData(title:String){
        info_message.text = title
    }



}