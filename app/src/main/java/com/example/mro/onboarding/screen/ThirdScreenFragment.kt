package com.example.mro.onboarding.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mro.R
import com.example.mro.databinding.FragmentThirdScreenBinding
class ThirdScreenFragment : Fragment() {

    private lateinit var binding: FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third_screen, container, false)

        binding = FragmentThirdScreenBinding.bind(view)

        binding.nextHalamanKetiga.setOnClickListener {
            findNavController().navigate(
                R.id.action_viewPagerFragment_to_signInFragment)
            onboardingFinished()
        }
        return view
    }
    private fun onboardingFinished() {
        val sharedPref = requireActivity()
            .getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Selesai", true)
        editor.apply()
    }
}


