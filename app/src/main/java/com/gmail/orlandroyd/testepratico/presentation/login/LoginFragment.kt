package com.gmail.orlandroyd.testepratico.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.orlandroyd.testepratico.R
import com.gmail.orlandroyd.testepratico.databinding.FragmentHomeBinding
import com.gmail.orlandroyd.testepratico.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    // BINDING
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init binding
        _binding = FragmentLoginBinding.bind(view)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}