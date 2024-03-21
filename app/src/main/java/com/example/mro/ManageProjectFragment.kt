package com.example.mro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentManageProjectBinding
import com.google.firebase.auth.FirebaseAuth

class ManageProjectFragment : Fragment() {

    lateinit var binding: FragmentManageProjectBinding
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backProject.setOnClickListener {
            val profilAdministratorFragment = ProfilAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, profilAdministratorFragment)
            transaction.commit()
        }

        binding.addProject.setOnClickListener {
            val addProjectFragment = AddProjectFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, addProjectFragment)
            transaction.commit()
        }
    }
}