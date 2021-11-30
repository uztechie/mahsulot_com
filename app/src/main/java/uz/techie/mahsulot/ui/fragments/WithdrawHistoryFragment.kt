package uz.techie.mahsulot.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.custom_toolbar.view.*
import kotlinx.android.synthetic.main.fragment_stream_product.*
import uz.techie.mahsulot.R
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.util.Resource
import uz.techie.mahsulot.util.Utils
import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_order_statistic.*
import kotlinx.android.synthetic.main.fragment_order_statistic.order_statistic_progressbar
import kotlinx.android.synthetic.main.fragment_order_statistic.order_statistic_tv
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.android.synthetic.main.fragment_stream_statistic.*
import kotlinx.android.synthetic.main.fragment_withdraw_history.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.adapter.*
import uz.techie.mahsulot.dialog.ConfirmDialog
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.Order
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_STREAM
import uz.techie.mahsulot.util.Constants


@AndroidEntryPoint
class WithdrawHistoryFragment : Fragment(R.layout.fragment_withdraw_history) {
    private val viewModel: MahsulotViewModel by viewModels()
    lateinit var statisticAdapter: WithdrawHistoryAdapter
    var token = ""
    private val TAG = "WithdrawHistoryFragment"
    lateinit var customProgressDialog: CustomProgressDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        customProgressDialog = CustomProgressDialog(requireContext())

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            user?.token?.let {
                token = "Token $it"
                viewModel.loadWithdrawHistory(token)
            }
        })

        statisticAdapter = WithdrawHistoryAdapter(requireContext())

        withdraw_history_recyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = statisticAdapter
        }

        viewModel.withdrawHistory.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: " + response.data)
            when (response) {
                is Resource.Loading -> {
                    hideErrorText()
                    withdraw_history_progressbar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    hideErrorText()
                    withdraw_history_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.toastIconError(requireActivity(), it)
                    }
                }
                is Resource.Success -> {
                    withdraw_history_progressbar.visibility = View.GONE
                    response.data?.let { withdrawResponse ->
                        Log.d(TAG, "onViewCreated: history "+withdrawResponse.message)
                        if (withdrawResponse.status == 200){
                            withdrawResponse.payments?.let { list->
                                statisticAdapter.differ.submitList(list)

                                if (list.isEmpty()) {
                                    showErrorText(getString(R.string.tolovlar_amalga_oshirilmagan))
                                } else {
                                    hideErrorText()
                                }

                            }
                        }
                        else{
                            withdrawResponse.message?.let { Utils.toastIconError(requireActivity(), it) }
                        }

                    }
                }
            }

        })


    }

    private fun initToolbar() {
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.to_lovlar_tarixi)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }


    private fun showErrorText(text: String) {
        withdraw_history_tv.visibility = View.VISIBLE
        withdraw_history_tv.text = text
    }

    private fun hideErrorText() {
        withdraw_history_tv.visibility = View.GONE
    }


}