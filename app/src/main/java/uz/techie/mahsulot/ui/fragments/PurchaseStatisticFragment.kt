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
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.android.synthetic.main.fragment_stream_statistic.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.adapter.OrderStatisticAdapter
import uz.techie.mahsulot.adapter.StreamAdapter
import uz.techie.mahsulot.adapter.StreamSoldStatisticAdapter
import uz.techie.mahsulot.adapter.StreamStatisticAdapter
import uz.techie.mahsulot.dialog.ConfirmDialog
import uz.techie.mahsulot.dialog.CustomProgressDialog
import uz.techie.mahsulot.model.Order
import uz.techie.mahsulot.model.Stream
import uz.techie.mahsulot.model.StreamStatistic
import uz.techie.mahsulot.ui.fragments.SearchStreamFragment.Companion.SEARCH_STREAM


@AndroidEntryPoint
class PurchaseStatisticFragment : Fragment(R.layout.fragment_order_statistic) {
    private val viewModel: MahsulotViewModel by viewModels()
    lateinit var statisticAdapter: OrderStatisticAdapter
    var token = ""
    private val TAG = "ProductStreamFragment"
    lateinit var customProgressDialog: CustomProgressDialog


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()

        customProgressDialog = CustomProgressDialog(requireContext())

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            user.token?.let {
                token = "Token $it"

                viewModel.loadOrderStatistics("Token 27e903ecfb160f96ce40b7053d98301e657a8784")
            }
        })

        statisticAdapter = OrderStatisticAdapter(requireContext())

        order_statistic_recyclerview.apply {
//            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = statisticAdapter
        }

        viewModel.orderStatistics.observe(viewLifecycleOwner, Observer { response ->
            Log.d(TAG, "onViewCreated: " + response.data)
            when (response) {
                is Resource.Loading -> {
                    hideErrorText()
                    order_statistic_progressbar.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    hideErrorText()
                    order_statistic_progressbar.visibility = View.GONE
                    response.message?.let {
                        Utils.showMessage(requireView(), it)
                    }
                }
                is Resource.Success -> {
                    order_statistic_progressbar.visibility = View.GONE
                    response.data?.let { orderResponse ->
                        Log.d(TAG, "onViewCreated: streamsize "+orderResponse.message)
                        if (orderResponse.status == 200){
                            orderResponse.data?.let { orders->
                                val list = mutableListOf<Order>()
                                list.addAll(orders)
                                statisticAdapter.differ.submitList(list)

                                if (orders.isEmpty()) {
                                    showErrorText(getString(R.string.statistika_mavjud_emas))
                                } else {
                                    hideErrorText()
                                }

                            }
                        }
                        else{
                            orderResponse.message?.let { Utils.showMessage(requireView(), it) }
                        }

                    }
                }
            }

        })


    }

    private fun initToolbar() {
//        toolbar_constraint.setBackgroundColor(resources.getColor(R.color.white))
        toolbar_title.text = getString(R.string.savdo_tarixi)
        toolbar_title.visibility = View.VISIBLE


        toolbar_btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
        toolbar_btnSearch.visibility = View.INVISIBLE

    }


    private fun showErrorText(text: String) {
        order_statistic_tv.visibility = View.VISIBLE
        order_statistic_tv.text = text
    }

    private fun hideErrorText() {
        order_statistic_tv.visibility = View.GONE
    }


}