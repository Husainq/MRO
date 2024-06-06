// src/main/java/com/example/mro/MngUserFragment.kt
package com.example.mro

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentMngProductBinding
import com.google.firebase.database.*

class MngProductFragment : Fragment() {

    private lateinit var binding: FragmentMngProductBinding
    private lateinit var productList: MutableList<Product>
    private lateinit var ref: DatabaseReference
    private lateinit var productAdapter: ProductAdapter

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            productAdapter.updateProfilePicture(it)

            // Optionally, update the user's profile picture URI in Firebase
            val dbAnggota = FirebaseDatabase.getInstance().getReference("Product")
            val selectedProduct = productAdapter.getSelectedProduct()
            selectedProduct?.let {
                it.profilePictureUri = uri.toString()
                dbAnggota.child(it.productId).setValue(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMngProductBinding.inflate(inflater, container, false)

        binding.backProduct.setOnClickListener {
            val profileAdministratorFragment = ProfileAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, profileAdministratorFragment)
            transaction.commit()
        }

        binding.addProduct.setOnClickListener {
            val addProductFragment = AddProductFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, addProductFragment)
            transaction.commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("Product")
        productList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) {
                    if (snapshot.exists()) {
                        productList.clear()
                        for (a in snapshot.children) {
                            val product = a.getValue(Product::class.java)
                            product?.let {
                                productList.add(it)
                            }
                        }
                        productAdapter = ProductAdapter(requireActivity(), R.layout.detil_product, productList, this@MngProductFragment)
                        binding.hasilProduct.adapter = productAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })

        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchProduct.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                productAdapter.filter.filter(newText)
                return false
            }
        })
    }

    fun startImagePicker() {
        pickImageLauncher.launch("image/*")
    }
}
