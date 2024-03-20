package com.example.mro

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class ProductAdapter(
    val productContext: Context,
    val layoutResId: Int,
    val productList: List<Product>
) : ArrayAdapter<Product>(productContext, layoutResId, productList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(productContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val imgView: ImageView = view.findViewById(R.id.gambar_product)
        val product_name: TextView = view.findViewById(R.id.nama_product)
        val product_categories: TextView = view.findViewById(R.id.product_categories)
        val product_unit: TextView = view.findViewById(R.id.product_unit)
        val imgEdit: ImageView = view.findViewById(R.id.icn_edit_product)
        val anggota = productList[position]

        imgEdit.setOnClickListener {
            updateDialog(anggota)
        }


        product_name.text = anggota.product_name
        product_categories.text = anggota.product_categories
        product_unit.text = anggota.product_unit

        return view
    }

    private fun updateDialog(anggota: Product) {
        val builder = AlertDialog.Builder(productContext)
        builder.setTitle("Update Data")
        val inflater = LayoutInflater.from(productContext)
        val view = inflater.inflate(R.layout.update_product, null)

        val edtProductName = view.findViewById<EditText>(R.id.upProductName)
        val edtProductCategories = view.findViewById<EditText>(R.id.upProductCategories)
        val edtProductUnit = view.findViewById<EditText>(R.id.upProductUnit)

        edtProductName.setText(anggota.product_name)
        edtProductCategories.setText(anggota.product_categories)
        edtProductUnit.setText(anggota.product_unit)

        builder.setView(view)

        builder.setPositiveButton("Change") { pe, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("Product")
            val ProductName = edtProductName.text.toString().trim()
            val ProductCategories = edtProductCategories.text.toString().trim()
            val ProductUnit = edtProductUnit.text.toString().trim()

            if (ProductName.isEmpty() or ProductCategories.isEmpty() or ProductUnit.isEmpty()) {
                Toast.makeText(
                    productContext, "Please fill in all fields",
                    Toast.LENGTH_SHORT
                )
                    .show()

                return@setPositiveButton
            }
            /**
            val anggota = Product(anggota.id, product_name, product_categories, product_unit)
            **/
            dbAnggota.child(anggota.id).setValue(anggota)
            Toast.makeText(productContext, "Data successfully updated", Toast.LENGTH_SHORT)
                .show()
        }

        builder.setNeutralButton("Cancel") { po, p1 -> }

        builder.setNegativeButton("Remove") { po, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("product")
                .child(anggota.id)

            dbAnggota.removeValue()

            Toast.makeText(productContext, "Data successfully deleted", Toast.LENGTH_SHORT)
                .show()
        }
        val alert = builder.create()
        alert.show()
    }
}