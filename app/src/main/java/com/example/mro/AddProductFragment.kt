package com.example.mro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.mro.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddProductBinding
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Product")
        binding.buttonTambahProduct.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        simpanData()
        val produk = AddProductFragment()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.container, AddProductFragment())
        transaction.commit()
    }

    private fun simpanData() {
        val product = binding.edtProductnameAdd.text.toString().trim()
        val categories = binding.edtCategoriesAdd.text.toString().trim()
        val unit =  binding.edtUnitAdd.text.toString().trim()

        if (product.isEmpty() or categories.isEmpty() or unit.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Please fill in all fields",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val produkId = ref.push().key
        val produk = Product(produkId!!, product, categories, unit)

        produkId?.let {
            ref.child(it).setValue(produk).addOnCompleteListener { task ->
                if(isAdded) {
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Successfully added product!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to add product",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.back.setOnClickListener{
            val manageProductFragment = ManageProductFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,manageProductFragment)
            transaction.commit()
        }
    }
}