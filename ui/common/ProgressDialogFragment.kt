package com.example.thefirstprojecttdtdemo.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.thefirstprojecttdtdemo.R

class ProgressDialogFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProgressDialogFragment()
        const val TAG = "ProgressDialogFragment"
    }
}