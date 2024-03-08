package com.example.mro.onboarding.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.mro.R
import com.example.mro.databinding.FragmentFirstScreenBinding


class FirstScreenFragment : Fragment() {

    private lateinit var binding: FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first_screen, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding = FragmentFirstScreenBinding.bind(view)

        binding.nextHalamanPertama.setOnClickListener {
            viewPager?.currentItem = 1
        }
        return view
    }
}