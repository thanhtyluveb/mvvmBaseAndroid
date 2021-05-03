package com.example.thefirstprojecttdtdemo.ui.screens.settings

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.thefirstprojecttdtdemo.R
import com.example.thefirstprojecttdtdemo.model.BitcoinTicker
import com.example.thefirstprojecttdtdemo.network.WebSocketNetWork
import com.example.thefirstprojecttdtdemo.ui.common.BaseFragment
import com.example.thefirstprojecttdtdemo.viewmodels.BaseViewModel
import com.example.thefirstprojecttdtdemo.viewmodels.settings.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_second.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mWebSocketNetWork: WebSocketNetWork
    var mViewModel = SettingsViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getChar(ARG_PARAM1).toString()
            param2 = it.getChar(ARG_PARAM2).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mWebSocketNetWork = WebSocketNetWork()
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    private fun setUpBtcPriceText(bitcoin: BitcoinTicker?) {
        activity?.runOnUiThread {
            try {
                val color = if (tvPriceBitCoin?.text?.replace("[^\\d]".toRegex(), "")
                        ?.toLong() ?: 0 > bitcoin?.price?.replace(".", "")?.toLong() ?: 0
                ) Color.RED else Color.BLUE
                tvPriceBitCoin?.setTextColor(color)
            } catch (ex: Exception) {
            }
            tvPriceBitCoin?.text = "${bitcoin?.price} €"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        tvNextTo3rdFragment.setOnClickListener(this)
        tvTestView.setOnClickListener(this)
        edt2ndFragment.setText(param1 + param2)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        edt2ndFragment.setText(param1 + param2)
        mViewModel.liveData.observe(viewLifecycleOwner, Observer {
            tvTestView.text = it
        })
        mWebSocketNetWork.apply {
            setUpCallBack {
                setUpBtcPriceText(it)
            }
            connect()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        const val TAG = "SecondFragment"
    }

    override fun onPause() {
        super.onPause()
        mWebSocketNetWork.close()
    }

    override fun onClick(v: View?) {
        when (v) {
            tvTestView -> {
                mViewModel.changeText()
            }
            else -> {
                this.findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)
            }
        }
    }
}