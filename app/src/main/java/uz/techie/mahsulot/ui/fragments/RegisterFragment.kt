package uz.techie.mahsulot.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.progress_button.*
import uz.techie.mahsulot.MainActivity

import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils
import java.util.*

@AndroidEntryPoint
class RegisterFragment:Fragment(R.layout.fragment_register) {
    private  val TAG = "RegisterFragment"

    val viewModel:MahsulotViewModel by viewModels()
    private var phone = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        arguments?.let {
            phone = RegisterFragmentArgs.fromBundle(it).phone
        }


        progress_button_layout.setOnClickListener {
            registerUser()
        }

        register_birhday.setOnClickListener {
            showDateDialog()
        }



    }

    private fun registerUser() {
        val name = register_name.text.toString().trim()
        val lastname = register_lastname.text.toString().trim()
        val birhday = register_birhday.text.toString()
        var gender = "True"
        if (register_radiogroup.checkedRadioButtonId == R.id.register_radio_male){
            gender = "True"
        }
        else{
            gender = "False"
        }


        if (name.isEmpty()){
            register_name_layout.error = getString(R.string.ismingizni_kiriting)
            return
        }
        else{
            register_name_layout.error = null
        }

        if (lastname.isEmpty()){
            register_lastname_layout.error = getString(R.string.familiyangizni_kiriting)
            return
        }
        else{
            register_lastname_layout.error = null
        }

        if (birhday.isEmpty()){
            register_birhday_layout.error = getString(R.string.tugulgan_kuningizni_kiriting)
            return
        }
        else{
            register_lastname_layout.error = null
        }


        viewModel.registration.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "registerUser: ")
            when(response){
                is Resource.Success ->{
                    response.data?.let { smsResponse ->
                        if (smsResponse.status == 200 || smsResponse.status == 201){
                            registrationSuccess(smsResponse.user)
                        }
                        else{
                            showMessage(smsResponse.message!!)
                            saveBtnReset()
                        }

                    }

                }
                is Resource.Error ->{
                    response.message?.let {
                        showMessage(it)
                        saveBtnReset()
                    }
                }
                is Resource.Loading ->{
                    saveBtnProgress()
                }


            }


        })

        viewModel.registerUser(phone, name, lastname, birhday, gender)


    }

    private fun showMessage(message: String) {
        Utils.toastIconError(requireActivity(), message)
    }

    private fun registrationSuccess(user: User?) {
        saveBtnSuccess()
        user?.let {
            viewModel.deleteUser()
            viewModel.insertUser(user)
        }


        Handler().postDelayed(Runnable {
           navigateToCabinet()
        },600)


    }

    private fun navigateToCabinet() {
        if (findNavController().currentDestination?.id == R.id.registerFragment) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToCabinetFragment())
        }


        val bottomMenu = (activity as MainActivity).bottomNavigationView
        val menuItem = bottomMenu.menu.findItem(R.id.cabinetFragment)
        bottomMenu.selectedItemId = menuItem.itemId
    }

    private fun saveBtnProgress(){
        progress_button_title.text = getString(R.string.saqlanmoqda)
        progress_button_progressbar.visibility = View.VISIBLE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.colorAccent))
    }

    private fun saveBtnSuccess(){
        progress_button_title.text = getString(R.string.saqlandi)
        progress_button_progressbar.visibility = View.GONE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.light_blue))
    }
    private fun saveBtnReset(){
        progress_button_title.text = getString(R.string.saqlash)
        progress_button_progressbar.visibility = View.GONE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.colorAccent))
    }

    private fun showDateDialog(){
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)


        val dateDialog = DatePickerDialog(requireContext(),R.style.customDatePickerStyle, DatePickerDialog.OnDateSetListener{view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            val date = calendar.time

            Log.d(TAG, "showDateDialog: ${Utils.reformatDate(date)}")
            register_birhday.setText(Utils.reformatDate(date))

        }, currentYear, currentMonth, currentDay)
        dateDialog.show()
    }


    private fun initToolbar(){
        toolbar_layout.setBackgroundColor(resources.getColor(R.color.background_color))
        toolbar_title.text = getString(R.string.royxatdan_otish)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.visibility = View.INVISIBLE
        toolbar_btnSearch.visibility = View.INVISIBLE

    }



}