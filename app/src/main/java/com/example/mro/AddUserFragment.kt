package com.example.mro

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentAddUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class AddUserFragment : Fragment(R.layout.fragment_add_user) {

    lateinit var binding: FragmentAddUserBinding
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Inisialisasi elemen-elemen UI
        val uemail: EditText = view.findViewById(R.id.edt_email_add)
        val uname: EditText = view.findViewById(R.id.edt_username_add)
        val urole: EditText = view.findViewById(R.id.edt_role_add)
        val usandi: EditText = view.findViewById(R.id.edt_password_add)
        val btnDaftar: Button = view.findViewById(R.id.button_tambar_user)


        btnDaftar.setOnClickListener {
            val gmail = uemail.text.toString().trim()
            val username = uname.text.toString().trim()
            val role = urole.text.toString().trim()
            val password = usandi.text.toString().trim()

            // Validasi input sebelum pendaftaran
            if (gmail.isEmpty() || username.isEmpty() || role.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses pendaftaran ke Firebase Authentication
            auth.createUserWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Pendaftaran berhasil
                        val user = auth.currentUser
                        val userId = user?.uid // atau dapatkan ID pengguna dari hasil pendaftaran

                        // Menyimpan informasi pengguna ke Realtime Database
                        val database = FirebaseDatabase.getInstance()
                        val reference = database.getReference("User") // Ganti dengan lokasi yang benar di database Anda

                        val userData = HashMap<String, Any>()
                        userData["email"] = gmail
                        userData["username"] = username
                        userData["role"] = role
                        userData["password"] = password

                        userId?.let {
                            reference.child(it).setValue(userData)
                                .addOnSuccessListener {
                                    // Data pengguna berhasil disimpan ke database
                                    Toast.makeText(requireContext(), "Successfully added user!", Toast.LENGTH_SHORT).show()
                                    // Redirect ke halaman utama setelah pendaftaran berhasil
                                    val addUserFragment = AddUserFragment()
                                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

                                    if (!requireActivity().isFinishing && !requireActivity().isDestroyed) {
                                        transaction.replace(R.id.container, addUserFragment)
                                        transaction.commit()
                                    }

                                }
                                .addOnFailureListener { e ->
                                    // Gagal menyimpan data pengguna ke database
                                    Toast.makeText(requireContext(), "Failed to add user: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // Pendaftaran gagal, tampilkan pesan kesalahan
                        val exception = task.exception
                        Toast.makeText(requireContext(), "Failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        // Listener untuk tombol back (redirect ke halaman sebelumnya)
        binding.back.setOnClickListener{
            val manageUserFragment = ManageUserFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,manageUserFragment)
            transaction.commit()
        }
    }
}