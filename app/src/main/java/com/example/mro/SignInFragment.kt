package com.example.mro

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("User")

        val emailEditText: EditText = view.findViewById(R.id.edt_email_signin)
        val passwordEditText: EditText = view.findViewById(R.id.edt_pass_signin)
<<<<<<< HEAD
        val btnMasuk: AppCompatButton = view.findViewById(R.id.button_login_signin)
=======
        val btnMasuk: Button = view.findViewById(R.id.button_login_signin)
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
        val forgotPass: TextView = view.findViewById(R.id.forgotpass_signin)

        btnMasuk.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authenticateUser(email, password)
        }

        forgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPassFragment)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userFound = false

                for (userSnapshot in snapshot.children) {
                    val userEmail = userSnapshot.child("email").value.toString()
                    val userPassword = userSnapshot.child("password").value.toString()

                    if (email == userEmail && password == userPassword) {
                        userFound = true
                        val role = userSnapshot.child("role").value.toString()

                        when (role) {
                            "Administrator" -> {
                                val intent = Intent(requireContext(), AdministratorActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            // Uncomment and define other roles as needed
                            // "Material" -> {
                            //     val intent = Intent(requireContext(), MaterialActivity::class.java)
                            //     startActivity(intent)
                            //     requireActivity().finish()
                            // }
                            // Add other roles here...
                            else -> {
                                Toast.makeText(requireContext(), "Role not recognized", Toast.LENGTH_SHORT).show()
                            }
                        }
                        break
                    }
                }

                if (!userFound) {
                    Toast.makeText(requireContext(), "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to sign in: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
