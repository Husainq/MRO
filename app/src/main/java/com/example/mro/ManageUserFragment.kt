package com.example.mro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mro.databinding.FragmentManageUserBinding
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth

class ManageUserFragment : Fragment() {

    lateinit var binding: FragmentManageUserBinding
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backUser.setOnClickListener {
            val profilAdministratorFragment = ProfilAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, profilAdministratorFragment)
            transaction.commit()
        }

        binding.addUser.setOnClickListener {
            val addUserFragment = AddUserFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, addUserFragment)
            transaction.commit()
        }
    }
}