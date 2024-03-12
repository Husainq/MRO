package com.example.mro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Inisialisasi elemen-elemen UI
        val emailEditText: EditText = view.findViewById(R.id.edt_email_signin)
        val passwordEditText: EditText = view.findViewById(R.id.edt_pass_signin)
        val btnMasuk: Button = view.findViewById(R.id.button_login_signin)
        val forgotPass: TextView = view.findViewById(R.id.forgotpass_signin)

        // Set listener untuk tombol masuk
        btnMasuk.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Validasi input sebelum masuk
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // ...
                        val user = auth.currentUser
                        val userId = user?.uid

                        val database = FirebaseDatabase.getInstance()
                        val reference = database.getReference("User")

                        userId?.let {
                            reference.child(it).get().addOnSuccessListener { snapshot ->
                                val role = snapshot.child("Role").value.toString()
                                if (role == "Administrator"){
                                val intent = Intent(requireContext(), AdministratorActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                                }
                                else if (role == "Material"){
                                    val intent = Intent(requireContext(), MaterialActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else if (role == "PIC"){
                                    val intent = Intent(requireContext(), PICActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else if (role == "Procurement"){
                                    val intent = Intent(requireContext(), ProcurementActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else if (role == "Project Manager"){
                                    val intent = Intent(requireContext(), ProjectManagerActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else{
                                    val intent = Intent(requireContext(), AdminProjectActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                            }.addOnFailureListener { e ->
                                // Gagal mendapatkan informasi role pengguna
                                Toast.makeText(requireContext(), "Failed to sign in: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // Masuk gagal, tampilkan pesan kesalahan
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException || exception is FirebaseAuthInvalidCredentialsException) {
                            // Pesan untuk email atau password salah
                            Toast.makeText(requireContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show()
                        } else {
                            // Pesan kesalahan lainnya
                            Toast.makeText(requireContext(), "Please fill in all fields: ${exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

        }
        forgotPass.setOnClickListener{
            val forgot = ForgotPassFragment()
            val transaction : FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragmentContainerView, forgot)
            transaction.commit()
        }
    }
}