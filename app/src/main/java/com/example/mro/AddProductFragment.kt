package com.example.mro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentAddProductBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddProductFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddProductBinding
    private lateinit var ref: DatabaseReference
    private val categories = arrayOf("A","B","C","D") // Array role
    private val units = arrayOf("Ton","Kg","Gram","Sak","Pcs")
    private var selectedImageUri: Uri? = null // URI foto yang dipilih

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        binding.backProduct.setOnClickListener{
            val mngProductFragment = MngProductFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,mngProductFragment)
            transaction.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Product")
        binding.buttonTambahProduct.setOnClickListener(this)
        binding.btnEditLogoProduct.setOnClickListener(this)

        // Set adapter for the Spinner categories
        val adapterCategories = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtCategoriesAdd.adapter = adapterCategories

        // Set adapter for the Spinner units
        val adapterUnits = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, units)
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtUnitAdd.adapter = adapterUnits
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button_tambah_product -> saveData()
            R.id.btn_edit_logo_product -> chooseImage()
        }
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            binding.fotoProduct.setImageURI(selectedImageUri)
        }
    }

    private fun saveData() {
        val productname = binding.edtProductnameAdd.text.toString().trim()
        val category = binding.edtCategoriesAdd.selectedItem.toString()
        val units = binding.edtUnitAdd.selectedItem.toString()

        if (productname.isEmpty() or category.isEmpty() or units.isEmpty()){
            Toast.makeText(
                requireContext(),
                "Isi data secara lengkap tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val productId = ref.push().key


        if (selectedImageUri != null) {
            val imageRef = FirebaseStorage.getInstance().reference.child("product_images/$productname").child("${System.currentTimeMillis()}")
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val profileImageUrl = uri.toString()
                        saveUserToDatabase(productId!!, productname, category, units, profileImageUrl)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Gagal mengunggah gambar: ${exception.message}", Toast.LENGTH_SHORT).show()

                }
        } else {
            // Jika pengguna tidak memilih gambar, simpan data pengguna tanpa URL gambar profil
            saveUserToDatabase(productId!!, productname, category, units, "")
        }
    }

    private fun saveUserToDatabase(productId: String, productname: String, category: String, units: String, profileImageUrl: String) {
        val product = Product(productId, productname, category, units, profileImageUrl)

        ref.child(productId).setValue(product).addOnCompleteListener { task ->
            if (isAdded) {
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    clearInputFields() // Panggil metode clearInputFields()
                } else {
                    Toast.makeText(requireContext(), "Gagal menambahkan data", Toast.LENGTH_SHORT).show()
                    clearInputFields() // Panggil metode clearInputFields()
                }
            }
        }
    }

    private fun clearInputFields() {
        binding.edtProductnameAdd.setText("")
        // Jika Anda ingin menghapus gambar profil yang dipilih
        view?.findViewById<ImageView>(R.id.fotoProduct)?.setImageResource(R.drawable.product) // Menghapus gambar dari ImageView

    }
    companion object {
        private const val IMAGE_PICK_REQUEST = 100
    }
}
