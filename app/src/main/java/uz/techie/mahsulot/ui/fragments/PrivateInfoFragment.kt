package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.dialog_info.*
import kotlinx.android.synthetic.main.fragment_private_info.*
import kotlinx.android.synthetic.main.progress_button.*
import kotlinx.coroutines.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils

@AndroidEntryPoint
class PrivateInfoFragment:Fragment(R.layout.fragment_private_info) {
    private val viewModel:MahsulotViewModel by viewModels()
    private var token = ""
    private lateinit var customProgressDialog: CustomProgressDialog
    private val TAG = "PrivateInfoFragment"
    private var mainUser = User(id = -1)
    private var job:Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        saveBtnReset()


        customProgressDialog = CustomProgressDialog(requireContext())

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user->
            mainUser = user

            info_phone.text = user.phone
            info_name.setText(user.first_name)
            info_lastname.setText(user.last_name)
            info_birhday.setText(user.birthday)

            user?.token?.let {
                token = "Token $it"
            }

        })


        viewModel.update.observe(viewLifecycleOwner, Observer { response->
            Log.d(TAG, "onViewCreated: update "+response)
            when(response){
                is Resource.Loading->{
                    saveBtnProgress()
                }
                is Resource.Error->{
                    saveBtnReset()
                    Utils.toastIconError(requireActivity(), response.message)
                }
                is Resource.Success->{
                    response.data?.let { updateResponse ->
                        if (updateResponse.status == 200){
                            saveBtnSuccess()
                            updateUserLocally()
                            Utils.toastIconSuccess(requireActivity(), getString(R.string.malumotlar_yangilandi))

                            job = MainScope().launch(Dispatchers.Main) {
                                delay(3000)
                                saveBtnReset()
                            }


                        }
                        else{
                            saveBtnReset()
                            Utils.toastIconError(requireActivity(), updateResponse.message)
                        }
                    }
                }

            }
        })





        progress_button_layout.setOnClickListener {
            val name = info_name.text.toString()
            val lastname = info_lastname.text.toString()


            if (name.isEmpty()){
                info_name_layout.error = getString(R.string.ismingizni_kiriting)
                return@setOnClickListener
            }
            else{
                info_name_layout.error = null
            }

            if (lastname.isEmpty()){
                info_lastname_layout.error = getString(R.string.familiyangizni_kiriting)
                return@setOnClickListener
            }
            else{
                info_lastname_layout.error = null
            }

            val map = HashMap<String, String>()
            map["first_name"] = name
            map["last_name"] = lastname

            mainUser.first_name = name
            mainUser.last_name = lastname

            viewModel.updateProfile(token, map)

        }

    }

    private fun updateUserLocally() {
        viewModel.insertUser(mainUser)
    }


    private fun initToolbar(){
        toolbar_layout.setBackgroundColor(resources.getColor(R.color.background_color))
        toolbar_title.text = getString(R.string.shaxsiy_ma_lumotlar)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }

    private fun saveBtnProgress(){
        progress_button_title.text = getString(R.string.saqlanmoqda)
        progress_button_progressbar.visibility = View.VISIBLE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.light_blue))
        progress_button_layout.isEnabled = false
    }

    private fun saveBtnSuccess(){
        progress_button_title.text = getString(R.string.saqlandi)
        progress_button_progressbar.visibility = View.GONE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.green))
    }
    private fun saveBtnReset(){
        progress_button_layout.isEnabled = true
        progress_button_title.text = getString(R.string.saqlash)
        progress_button_progressbar.visibility = View.GONE
        progress_button_layout.setBackgroundColor(resources.getColor(R.color.light_blue))
    }


    override fun onStop() {
        super.onStop()
        job?.cancel()
    }






}