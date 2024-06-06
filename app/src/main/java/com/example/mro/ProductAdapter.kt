package com.example.mro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(
    private val productContext: Context,
    private val layoutResId: Int,
    private var productList: MutableList<Product>,
    private val fragment: MngProductFragment
) : ArrayAdapter<Product>(productContext, layoutResId, productList), Filterable {

    private var originalProductList: MutableList<Product> = ArrayList(productList)
    private var filteredProductList: MutableList<Product> = ArrayList(productList)
    private var selectedProduct: Product? = null
    private var selectedImageView: ImageView? = null
    private var newProfilePictureUri: Uri? = null

    override fun getCount(): Int {
        return filteredProductList.size
    }

    override fun getItem(position: Int): Product? {
        return filteredProductList[position]
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(productContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val o_productname: TextView = view.findViewById(R.id.nama_product)
        val o_categories: TextView = view.findViewById(R.id.product_categories)
        val o_unit: TextView = view.findViewById(R.id.product_unit)
        val imgPhoto: ImageView = view.findViewById(R.id.gambar_product)
        val imgEdit: ImageView = view.findViewById(R.id.icn_edit_user)
        val anggota = productList[position]

        selectedProduct = anggota
        val currentUser = productList[position]
        imgEdit.setOnClickListener {
            updateDialog(anggota)
        }
        o_productname.text = anggota.product
        o_categories.text = anggota.category
        o_unit.text = anggota.units

        // Load gambar profil menggunakan Glide
        Glide.with(productContext)
            .load(currentUser.profilePictureUri) // Menggunakan URI gambar dari product
            .placeholder(R.drawable.product)
            .error(R.drawable.product)
            .into(imgPhoto)

        return view
    }

    private fun updateDialog(anggota: Product) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(productContext)
        builder.setTitle("Update Data " + anggota.product)
        val inflater = LayoutInflater.from(productContext)
        val view = inflater.inflate(R.layout.update_product, null)

        val edtProduct = view.findViewById<EditText>(R.id.upProductName)
        val edtCategories = view.findViewById<Spinner>(R.id.upCategoriesProduct)
        val edtUnit = view.findViewById<Spinner>(R.id.upUnitProduct)
        val imgProfile = view.findViewById<ImageView>(R.id.gambarProduct)
        val btnChangePhoto = view.findViewById<Button>(R.id.btn_edit_logo_product)

        edtProduct.setText(anggota.product)

        val roleArray = arrayOf("Administrator", "Admin Project", "Project Manager", "Procurement", "PIC", "Materials")
        val adapter = ArrayAdapter(productContext, android.R.layout.simple_spinner_item, roleArray)
        edtCategories.adapter = adapter
        edtCategories.setSelection(roleArray.indexOf(anggota.category))

        val roleArray1 = arrayOf("Ton","Kg","Gram","Sak","Pcs")
        edtUnit.adapter = adapter
        edtUnit.setSelection(roleArray1.indexOf(anggota.units))

        Glide.with(productContext)
            .load(anggota.profilePictureUri)
            .placeholder(R.drawable.empty_photo)
            .error(R.drawable.empty_photo)
            .into(imgProfile)

        btnChangePhoto.setOnClickListener {
            selectedImageView = imgProfile
            fragment.startImagePicker()
        }

        builder.setView(view)

        builder.setPositiveButton("Change") { _, _ ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("Product")
            val product = edtProduct.text.toString().trim()
            val categories = edtCategories.selectedItem.toString()
            val unit = edtUnit.selectedItem.toString()

            if (product.isEmpty() || categories.isEmpty() || unit.isEmpty()) {
                Toast.makeText(productContext, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val updatedProduct = mutableMapOf<String, Any>(
                "product" to product,
                "categories" to categories,
                "unit" to unit,
            )
            newProfilePictureUri?.let {
                updatedProduct["profilePictureUri"] = it.toString()
            }

            dbAnggota.child(anggota.productId).updateChildren(updatedProduct)
                .addOnSuccessListener {
                    Toast.makeText(productContext, "Successfully updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(productContext, "Failed to update: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        builder.setNeutralButton("Cancel") { _, _ -> }

        builder.setNegativeButton("Remove") { _, _ ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("Product").child(anggota.productId)
            dbAnggota.removeValue()
            Toast.makeText(productContext, "Data successfully deleted", Toast.LENGTH_SHORT).show()
        }
        val alert = builder.create()
        alert.show()
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    originalProductList
                } else {
                    val filterPattern = constraint.toString().lowercase().trim()
                    originalProductList.filter {
                        it.product.lowercase().contains(filterPattern) ||
                                it.category.lowercase().contains(filterPattern) ||
                                it.units.lowercase().contains(filterPattern)
                    }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredProductList = if (results?.values == null) {
                    mutableListOf()
                } else {
                    results.values as MutableList<Product>
                }
                notifyDataSetChanged()
            }
        }
    }
    fun updateProfilePicture(uri: Uri) {
        newProfilePictureUri = uri
        selectedImageView?.let {
            Glide.with(productContext).load(uri).into(it)
        }
        notifyDataSetChanged()
    }

    fun getSelectedProduct(): Product? {
        return selectedProduct
    }


}
