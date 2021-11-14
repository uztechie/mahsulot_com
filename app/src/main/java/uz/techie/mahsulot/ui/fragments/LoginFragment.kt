package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cabinet.*
import kotlinx.android.synthetic.main.fragment_login.*
import okio.Utf8
import uz.techie.mahsulot.R
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.util.Utils

class LoginFragment :Fragment(R.layout.fragment_login) {

    lateinit var customProgressDialog: CustomProgressDialog
    private var phoneNumber = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customProgressDialog = CustomProgressDialog(requireContext())
        login_btn_takecode.setOnClickListener {

            val sPhone = "+998" + phone_input_phone_edittext.unmaskedText
            if (sPhone.length <= 12) {
                phone_input_phone_container.error = getString(R.string.mavjud_telefon_raqami)
                return@setOnClickListener
            }
            else{
                phone_input_phone_container.error = null
            }

            if (!Utils.isNetworkAvailable(requireContext())){
                Snackbar.make(
                    requireView(),
                    getString(R.string.internetga_ulanmagan),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }





            customProgressDialog.show()

        }

    }
}