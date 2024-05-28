package com.example.mro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentMngProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference


class MngProductFragment : Fragment() {
    private lateinit var binding: FragmentMngProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMngProductBinding.inflate(inflater, container, false)
        binding.addProduct.setOnClickListener{
            val addProductFragment = AddProductFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,addProductFragment)
            transaction.commit()
        }
        binding.backProduct.setOnClickListener{
            val profileAdministratorFragment = ProfileAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,profileAdministratorFragment)
            transaction.commit()
        }

        return binding.root
    }
}