package com.example.mro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mro.databinding.FragmentManageProductBinding
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentAddUserBinding
import com.google.firebase.auth.FirebaseAuth

class ManageProductFragment : Fragment() {

    lateinit var binding: FragmentManageProductBinding
    private lateinit var auth: FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backProduct.setOnClickListener {
            val profilAdministratorFragment = ProfilAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, profilAdministratorFragment)
            transaction.commit()
        }

        binding.addProduct.setOnClickListener {
            val addProductFragment = AddProductFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, addProductFragment)
            transaction.commit()
        }
    }
}