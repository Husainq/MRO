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
import com.example.mro.databinding.FragmentAddUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddUserFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentAddUserBinding
    private lateinit var ref: DatabaseReference
    private val roles = arrayOf("Administrator", "Admin Project", "Project Manager", "Procurement", "PIC", "Materials") // Array role
    private var selectedImageUri: Uri? = null // URI foto yang dipilih

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(inflater, container, false)
        binding.backUser.setOnClickListener{
            val mngUserFragment = MngUserFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,mngUserFragment)
            transaction.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("User")
        binding.buttonTambahUser.setOnClickListener(this)
        binding.btnEditLogo.setOnClickListener(this)

        // Set adapter for the Spinner
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtRoleAdd.adapter = adapter
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.button_tambah_user -> saveData()
            R.id.btn_edit_logo -> chooseImage()
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
            binding.fotoAddProfile.setImageURI(selectedImageUri)
        }
    }

    private fun saveData() {
        val username = binding.edtUsernameAdd.text.toString().trim()
        val email = binding.edtEmailAdd.text.toString().trim()
        val role = binding.edtRoleAdd.selectedItem.toString()
        val password = binding.edtPasswordAdd.text.toString().trim()

        if (username.isEmpty() or email.isEmpty() or role.isEmpty() or password.isEmpty()){
            Toast.makeText(
                requireContext(),
                "Isi data secara lengkap tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val userId = ref.push().key


        if (selectedImageUri != null) {
            val imageRef = FirebaseStorage.getInstance().reference.child("user_images/$username").child("${System.currentTimeMillis()}")
            imageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val profileImageUrl = uri.toString()
                        saveUserToDatabase(userId!!, username, email, role, password, profileImageUrl)
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Gagal mengunggah gambar: ${exception.message}", Toast.LENGTH_SHORT).show()

                }
        } else {
            // Jika pengguna tidak memilih gambar, simpan data pengguna tanpa URL gambar profil
            saveUserToDatabase(userId!!, username, email, role, password, "")
        }
    }

    private fun saveUserToDatabase(userId: String, username: String, email: String, role: String, password: String, profileImageUrl: String) {
        val user = User(userId, username, email, role, password, profileImageUrl)

        ref.child(userId).setValue(user).addOnCompleteListener { task ->
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
        binding.edtUsernameAdd.setText("")
        binding.edtEmailAdd.setText("")
        binding.edtPasswordAdd.setText("")
        // Jika Anda ingin menghapus gambar profil yang dipilih
        view?.findViewById<ImageView>(R.id.foto_add_profile)?.setImageResource(R.drawable.empty_photo) // Menghapus gambar dari ImageView

    }
    companion object {
        private const val IMAGE_PICK_REQUEST = 100
    }
}
