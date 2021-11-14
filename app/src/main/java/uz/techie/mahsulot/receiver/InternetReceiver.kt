package uz.techie.mahsulot.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import okio.Utf8
import uz.techie.mahsulot.dialog.InternetDialog
import uz.techie.mahsulot.util.Constants
import uz.techie.mahsulot.util.Utils


class InternetReceiver:BroadcastReceiver() {
    lateinit var  internetDialog:InternetDialog
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            internetDialog = InternetDialog(it)
            if (Utils.isNetworkAvailable(it)){
                Log.d("TAG", "onReceive: should hide ")
                if (Constants.internetDialog != null){
                    Constants.internetDialog!!.dismiss()
                }
            }
            else{
                internetDialog.show()
                Constants.internetDialog = internetDialog
            }
        }

    }
}