package com.rempawl.respolhpl.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rempawl.respolhpl.databinding.FragmentCheckoutBinding
import com.rempawl.respolhpl.utils.autoCleared

class CheckoutFragment : Fragment() {

    private var binding: FragmentCheckoutBinding by autoCleared()
    private val viewModel: CheckoutViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater)
        return binding.root
    }

    companion object {
        fun newInstance() = CheckoutFragment()
    }
}
