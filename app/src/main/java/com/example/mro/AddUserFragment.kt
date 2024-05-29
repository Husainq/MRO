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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentAddUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class AddUserFragment : Fragment(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentAddUserBinding
    private val roles = arrayOf("Administrator", "Admin Project", "Project Manager", "Procurement", "PIC", "Materials")
    private var selectedImageUri: Uri? = null
    private lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserBinding.inflate(inflater, container, false)
        binding.backUser.setOnClickListener {
            val mngUserFragment = MngUserFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, mngUserFragment)
            transaction.commit()
        }

        binding.fotoAddProfile.setOnClickListener(this)

        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        storage = Firebase.storage

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtRoleAdd.adapter = adapter

        binding.buttonTambahUser.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.foto_add_profile -> chooseImage()
            R.id.button_tambah_user -> registerUser()
        }
    }

    private fun registerUser() {
        val username = binding.edtUsernameAdd.text.toString().trim()
        val email = binding.edtEmailAdd.text.toString().trim()
        val role = binding.edtRoleAdd.selectedItem.toString()
        val password = binding.edtPasswordAdd.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || role.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Isi data secara lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid

                    if (selectedImageUri != null) {
                        val storageRef = storage.reference
                        val profileImageRef = storageRef.child("profile_images/${UUID.randomUUID()}/${UUID.randomUUID()}.jpg")

                        profileImageRef.putFile(selectedImageUri!!)
                            .addOnSuccessListener {
                                profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                                    val imageUrl = uri.toString()
                                    saveUserToDatabase(userId, username, email, role, password, imageUrl)
                                }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(requireContext(), "Gagal mengunggah gambar: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        saveUserToDatabase(userId, username, email, role, password, null)
                    }
                } else {
                    val exception = task.exception
                    Toast.makeText(requireContext(), "Pendaftaran gagal: ${exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToDatabase(userId: String?, username: String, email: String, role: String, password: String, imageUrl: String?) {
        val database = FirebaseDatabase.getInstance()
        val reference = database.getReference("User")

        val userData = HashMap<String, Any>()
        userData["userId"] = userId ?: ""
        userData["email"] = email
        userData["username"] = username
        userData["role"] = role
        userData["password"] = password
        userData["profileImageUri"] = imageUrl ?: ""

        userId?.let {
            // Menggunakan userId sebagai kunci untuk setiap entri pengguna
            reference.child(it).setValue(userData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                    val mngUserFragment = MngUserFragment()
                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
                    transaction.replace(R.id.container, mngUserFragment)
                    transaction.commit()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Gagal menyimpan data pengguna: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val IMAGE_PICK_REQUEST = 100
    }
}
