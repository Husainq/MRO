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
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(
    val productContext: Context,
    val layoutResId: Int,
    val productList: List<Product>
) : ArrayAdapter<Product>(productContext, layoutResId, productList) {

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 100
    }

    private var selectedProduct: Product? = null
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
//        imgEdit.setOnClickListener {
//            updateDialog(anggota)
//        }
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


}

