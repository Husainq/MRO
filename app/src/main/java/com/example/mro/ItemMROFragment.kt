//package com.example.mro
//
//import android.content.ContentValues.TAG
//import android.os.Bundle
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.mro.data_class.Product
//import com.google.firebase.database.*
//
//class ItemMROFragment : Fragment() {
//
//    private lateinit var productSpinner: Spinner
//    private lateinit var quantityEditText: EditText
//    private val productsList = mutableListOf<Product>()
//    private val selectedProductsList = mutableListOf<Pair<Product, Int>>()
//
//    private val productsRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("products")
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val rootView = inflater.inflate(R.layout.item_mro, container, false)
//
//        productSpinner = rootView.findViewById(R.id.SpinnerProduct)
//        quantityEditText = rootView.findViewById(R.id.edtQuantityProduct)
//
//        fetchProducts()
//
//        productSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
//                // Handle when a product is selected
//                val selectedProduct = productsList[position]
//                // Perform actions with the selected product
//            }
//
//            override fun onNothingSelected(parentView: AdapterView<*>?) {
//                // Handle when nothing is selected
//            }
//        }
//
//        quantityEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val quantity = s?.toString()?.toIntOrNull() ?: 0
//                // Perform actions with the entered quantity
//            }
//        })
//
//        val addButton: Button = rootView.findViewById(R.id.)
//        addButton.setOnClickListener {
//            val selectedProduct = productSpinner.selectedItem as Product
//            val quantity = quantityEditText.text.toString().toIntOrNull() ?: 0
//            if (quantity > 0) {
//                selectedProductsList.add(Pair(selectedProduct, quantity))
//                // Update RecyclerView adapter with selectedProductsList
//                updateRecyclerView()
//            }
//        }
//
//        return rootView
//    }
//
//    private fun fetchProducts() {
//        productsRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (productSnapshot in dataSnapshot.children) {
//                        val product = productSnapshot.getValue(Product::class.java)
//                        product?.let {
//                            productsList.add(it)
//                        }
//                    }
//                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, productsList.map { it.productName })
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//                    productSpinner.adapter = adapter
//                } else {
//                    // Tampilkan pesan bahwa tidak ada produk yang ditemukan
//                    Toast.makeText(requireContext(), "Tidak ada produk yang ditemukan", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Handle database error
//                Log.e(TAG, "Error fetching products: ${databaseError.message}")
//                // Tampilkan pesan kesalahan atau lakukan penanganan lainnya
//                Toast.makeText(requireContext(), "Terjadi kesalahan saat mengambil data produk", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//
//    private fun updateRecyclerView() {
//        val recyclerView: RecyclerView = requireActivity().findViewById(R.id.rv_list_product)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = ProductAdapter(selectedProductsList.map { it.first })
//
//        // Simpan informasi produk ke dalam Firebase Realtime Database
//        val mrosRef = FirebaseDatabase.getInstance().reference.child("MROs")
//        val mroId = mrosRef.push().key ?: ""
//
//        val userId = "ID_pengguna" // Ganti dengan ID pengguna yang sesuai
//        val orderDate = System.currentTimeMillis() // Gunakan tanggal saat ini sebagai tanggal pemesanan
//
//        val mroProducts = mutableMapOf<String, Int>()
//        for ((product, quantity) in selectedProductsList) {
//            mroProducts[product.id] = quantity
//        }
//
//        val newMro = MRO(mroId, userId, orderDate, mroProducts.toList())
//
//        mrosRef.child(mroId).setValue(newMro)
//    }
//}
//
//
