package com.pedro.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pedro.task.R
import com.pedro.task.databinding.FragmentRecoverAccountFregmentBinding

class RecoverAccountFregment : Fragment() {

    private var _binding: FragmentRecoverAccountFregmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountFregmentBinding.inflate(inflater, container, false)
        return binding.root
    }

}