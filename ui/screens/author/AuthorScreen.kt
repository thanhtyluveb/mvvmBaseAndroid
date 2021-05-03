package com.example.thefirstprojecttdtdemo.ui.screens.author

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.thefirstprojecttdtdemo.R
import com.example.thefirstprojecttdtdemo.ui.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AuthorFragment : BaseFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tvNextTo2ndFragment.setOnClickListener(this)
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onClick(v: View?) {
        val text = edtHomeFragment?.editableText?.toString() ?: "empty"
        val bundle = bundleOf(ARG_PARAM1 to text.firstOrNull(), ARG_PARAM2 to text.lastOrNull())
        this.findNavController().navigate(R.id.action_home2_to_secondFragment, bundle)
    }
}