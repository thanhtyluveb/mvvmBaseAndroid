package com.example.thefirstprojecttdtdemo.ui.screens.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.thefirstprojecttdtdemo.R
import com.example.thefirstprojecttdtdemo.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_third.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OtherFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tvNextTo4thFragment.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OtherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        this.findNavController().navigate(R.id.action_thirdFragment_to_navigation)
    }
}