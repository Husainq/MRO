package com.example.mro

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

class ForgotPassFragment : Fragment(R.layout.fragment_forgot_pass) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText: EditText = view.findViewById(R.id.edt_email_forgotpass)
        val resetPasswordButton: AppCompatButton = view.findViewById(R.id.button_next_forgotpass)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(requireContext(),"Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.error = "Email is invalid"
                emailEditText.requestFocus()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(requireContext(), "Check your email to reset your password", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_forgotPassFragment_to_signInFragment)

                }else {
                    Toast.makeText(requireContext(),"${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}