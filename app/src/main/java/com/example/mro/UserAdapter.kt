package com.example.mro

<<<<<<< HEAD
import android.content.Context
=======
import android.app.Activity
import android.content.Context
import android.content.Intent
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
=======
import android.app.AlertDialog
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
<<<<<<< HEAD
import androidx.appcompat.app.AlertDialog
=======
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class UserAdapter(
<<<<<<< HEAD
    private val userContext: Context,
    private val layoutResId: Int,
    private val userList: List<User>,
    private val fragment: MngUserFragment
=======
    val userContext: Context,
    val layoutResId: Int,
    val userList: List<User>
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
) : ArrayAdapter<User>(userContext, layoutResId, userList) {

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 100
    }

<<<<<<< HEAD
    private var selectedUser: User? = null
=======
    var selectedUser: User? = null
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
    private var selectedImageView: ImageView? = null
    private var newProfilePictureUri: Uri? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(userContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val o_username: TextView = view.findViewById(R.id.ou_username)
        val o_email: TextView = view.findViewById(R.id.ou_email)
        val o_role: TextView = view.findViewById(R.id.ou_role)
        val imgPhoto: ImageView = view.findViewById(R.id.gambar_user)
        val imgEdit: ImageView = view.findViewById(R.id.icn_edit_user)
        val anggota = userList[position]

<<<<<<< HEAD
        imgEdit.setOnClickListener {
            selectedUser = anggota
=======
        selectedUser = anggota
        val currentUser = userList[position]
        imgEdit.setOnClickListener {
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
            updateDialog(anggota)
        }
        o_username.text = anggota.username
        o_email.text = anggota.email
        o_role.text = anggota.role

<<<<<<< HEAD
        Glide.with(userContext)
            .load(anggota.profilePictureUri)
=======
        // Load gambar profil menggunakan Glide
        Glide.with(userContext)
            .load(currentUser.profilePictureUri) // Menggunakan URI gambar dari user
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
            .placeholder(R.drawable.empty_photo)
            .error(R.drawable.empty_photo)
            .into(imgPhoto)

        return view
    }

    private fun updateDialog(anggota: User) {
        val builder = AlertDialog.Builder(userContext)
        builder.setTitle("Update Data " + anggota.username)
        val inflater = LayoutInflater.from(userContext)
        val view = inflater.inflate(R.layout.update_user, null)

        val edtUsername = view.findViewById<EditText>(R.id.upUsername)
        val edtEmail = view.findViewById<EditText>(R.id.upEmail)
        val edtPassword = view.findViewById<EditText>(R.id.upPassword)
        val edtRole = view.findViewById<Spinner>(R.id.upRole)
        val imgProfile = view.findViewById<ImageView>(R.id.foto)
        val btnChangePhoto = view.findViewById<Button>(R.id.btn_edit_logo)

        edtUsername.setText(anggota.username)
        edtEmail.setText(anggota.email)
        edtPassword.setText(anggota.password)

        val roleArray = arrayOf("Administrator", "Admin Project", "Project Manager", "Procurement", "PIC", "Materials")
        val adapter = ArrayAdapter(userContext, android.R.layout.simple_spinner_item, roleArray)
        edtRole.adapter = adapter
        edtRole.setSelection(roleArray.indexOf(anggota.role))

        Glide.with(userContext)
            .load(anggota.profilePictureUri)
            .placeholder(R.drawable.empty_photo)
            .error(R.drawable.empty_photo)
            .into(imgProfile)

        btnChangePhoto.setOnClickListener {
            selectedImageView = imgProfile
<<<<<<< HEAD
            fragment.startImagePicker()
=======
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            (userContext as Activity).startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
        }

        builder.setView(view)

        builder.setPositiveButton("Change") { _, _ ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("User")
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val role = edtRole.selectedItem.toString()

<<<<<<< HEAD
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(userContext, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val updatedUser = mutableMapOf<String, Any>(
                "username" to username,
                "email" to email,
                "role" to role,
                "password" to password
            )
            newProfilePictureUri?.let {
                updatedUser["profilePictureUri"] = it.toString()
            }

            dbAnggota.child(anggota.userId).updateChildren(updatedUser)
                .addOnSuccessListener {
                    Toast.makeText(userContext, "Successfully updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(userContext, "Failed to update: ${e.message}", Toast.LENGTH_SHORT).show()
                }
=======
            if (username.isEmpty() or email.isEmpty() or password.isEmpty() or role.isEmpty()) {
                Toast.makeText(
                    userContext, "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()

                return@setPositiveButton
            }

            val updatedUser = User(
                anggota.userId,
                username,
                email,
                role,
                password,
                newProfilePictureUri?.toString() ?: anggota.profilePictureUri
            )

            dbAnggota.child(anggota.userId).setValue(updatedUser)
            Toast.makeText(userContext, "Successfully updated", Toast.LENGTH_SHORT).show()
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
        }

        builder.setNeutralButton("Cancel") { _, _ -> }

        builder.setNegativeButton("Remove") { _, _ ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("User").child(anggota.userId)
            dbAnggota.removeValue()
            Toast.makeText(userContext, "Data successfully deleted", Toast.LENGTH_SHORT).show()
        }
        val alert = builder.create()
        alert.show()
    }

    fun updateProfilePicture(uri: Uri) {
        newProfilePictureUri = uri
        selectedImageView?.let {
            Glide.with(userContext).load(uri).into(it)
        }
        notifyDataSetChanged()
    }
<<<<<<< HEAD

    fun getSelectedUser(): User? {
        return selectedUser
    }
=======
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
}
