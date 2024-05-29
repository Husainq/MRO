package com.example.mro

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentMngUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
<<<<<<< HEAD
import androidx.activity.result.contract.ActivityResultContracts

class MngUserFragment : Fragment() {
    private lateinit var binding: FragmentMngUserBinding
=======

class MngUserFragment : Fragment() {
    lateinit var binding: FragmentMngUserBinding
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
    private lateinit var userList: MutableList<User>
    private lateinit var ref: DatabaseReference
    private lateinit var userAdapter: UserAdapter

<<<<<<< HEAD
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            userAdapter.updateProfilePicture(it)

            // Optionally, update the user's profile picture URI in Firebase
            val dbAnggota = FirebaseDatabase.getInstance().getReference("User")
            val selectedUser = userAdapter.getSelectedUser()
            selectedUser?.let {
                it.profilePictureUri = uri.toString()
                dbAnggota.child(it.userId).setValue(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
=======
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
        binding = FragmentMngUserBinding.inflate(inflater, container, false)

        binding.backUser.setOnClickListener {
            val profileAdministratorFragment = ProfileAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, profileAdministratorFragment)
            transaction.commit()
        }

        binding.addUser.setOnClickListener {
            val addUserFragment = AddUserFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, addUserFragment)
            transaction.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("User")
        userList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
<<<<<<< HEAD
                if (isAdded) {
=======
                if (isAdded) { // Pastikan Fragment terpasang sebelum menggunakan requireActivity()
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
                    if (snapshot.exists()) {
                        userList.clear()
                        for (a in snapshot.children) {
                            val user = a.getValue(User::class.java)
                            user?.let {
                                userList.add(it)
                            }
                        }
<<<<<<< HEAD
                        userAdapter = UserAdapter(requireActivity(), R.layout.detil_user, userList, this@MngUserFragment)
=======
                        userAdapter = UserAdapter(
                            requireActivity(),
                            R.layout.detil_user,
                            userList
                        )
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
                        binding.hasilUser.adapter = userAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

<<<<<<< HEAD
    fun startImagePicker() {
        pickImageLauncher.launch("image/*")
=======
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UserAdapter.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                userAdapter.updateProfilePicture(selectedImageUri)

                // Optionally, update the user's profile picture URI in Firebase
                val dbAnggota = FirebaseDatabase.getInstance().getReference("User")
                userAdapter.selectedUser?.let {
                    it.profilePictureUri = selectedImageUri.toString()
                    dbAnggota.child(it.userId).setValue(it)
                }
            }
        }
>>>>>>> 98c38e7a3aa61f28927d30455ced2a8286c30239
    }
}
