package com.example.mro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentAddProjectBinding
import com.google.firebase.database.*

class AddProjectFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddProjectBinding
    private lateinit var ref: DatabaseReference
    private lateinit var userRef: DatabaseReference

    private lateinit var adminProjectAdapter: ArrayAdapter<String>
    private lateinit var projectManagerAdapter: ArrayAdapter<String>
    private lateinit var procurementAdapter: ArrayAdapter<String>
    private lateinit var picAdapter: ArrayAdapter<String>
    private lateinit var materialAdapter: ArrayAdapter<String>

    private val areas = arrayOf("Duri", "Pekanbaru") // Array role

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProjectBinding.inflate(inflater, container, false)
        binding.backProject.setOnClickListener {
            val mngProjectFragment = MngProjectFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container, mngProjectFragment)
            transaction.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("Project")
        userRef = FirebaseDatabase.getInstance().getReference("User")

        binding.buttonTambahProject.setOnClickListener(this)

        // Set adapter for the Area Spinner
        val areaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, areas)
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.edtAreaAdd.adapter = areaAdapter

        // Initialize lists and adapters for role-based Spinners
        adminProjectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        projectManagerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        procurementAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        picAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())
        materialAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf())

        // Set adapters to spinners
        binding.edtAdminprojectAdd.adapter = adminProjectAdapter
        binding.edtPjAdd.adapter = projectManagerAdapter
        binding.edtProcurementAdd.adapter = procurementAdapter
        binding.edtPicAdd.adapter = picAdapter
        binding.edtMaterialAdd.adapter = materialAdapter

        // Fetch data from Firebase to populate the role-based Spinners
        fetchUsersByRole("Admin Project", adminProjectAdapter)
        fetchUsersByRole("Project Manager", projectManagerAdapter)
        fetchUsersByRole("Procurement", procurementAdapter)
        fetchUsersByRole("PIC", picAdapter)
        fetchUsersByRole("Materials", materialAdapter)
    }

    private fun fetchUsersByRole(role: String, roleAdapter: ArrayAdapter<String>) {
        userRef.orderByChild("role").equalTo(role)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userList = mutableListOf<String>()
                    for (snapshot in dataSnapshot.children) {
                        val userId = snapshot.key // Mendapatkan userId
                        val username = snapshot.child("username").getValue(String::class.java)
                        username?.let {
                            Log.d("FetchUsersByRole", "Fetched user: $it for role: $role")
                            userList.add(it)
                        }
                    }
                    roleAdapter.clear()
                    roleAdapter.addAll(userList)
                    roleAdapter.notifyDataSetChanged()
                    Log.d("FetchUsersByRole", "Updated adapter for role: $role with ${roleAdapter.count} users")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("FetchUsersByRole", "Error fetching users for role: $role", databaseError.toException())
                    Toast.makeText(requireContext(), "Failed to load users with role $role", Toast.LENGTH_SHORT).show()
                }
            })
    }



    override fun onClick(v: View?) {
        saveData()
    }

    private fun saveData() {
        val projectname = binding.edtProjectnameAdd.text.toString().trim()
        val areaproject = binding.edtAreaAdd.selectedItem.toString()
        val adminProjectUsername = binding.edtAdminprojectAdd.selectedItem.toString()
        val projectManagerUsername = binding.edtPjAdd.selectedItem.toString()
        val procurementUsername = binding.edtProcurementAdd.selectedItem.toString()
        val picUsername = binding.edtPicAdd.selectedItem.toString()
        val materialUsername = binding.edtMaterialAdd.selectedItem.toString()

        if (projectname.isEmpty() || areaproject.isEmpty() || adminProjectUsername.isEmpty() || projectManagerUsername.isEmpty() || procurementUsername.isEmpty() || picUsername.isEmpty() || materialUsername.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Isi data secara lengkap tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Mengambil ID pengguna terkait dari username yang dipilih untuk setiap peran
        userRef.orderByChild("username").equalTo(adminProjectUsername)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val adminProjectUserId = dataSnapshot.children.first().key

                        userRef.orderByChild("username").equalTo(projectManagerUsername)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(projectManagerDataSnapshot: DataSnapshot) {
                                    if (projectManagerDataSnapshot.exists()) {
                                        val projectManagerUserId = projectManagerDataSnapshot.children.first().key

                                        userRef.orderByChild("username").equalTo(procurementUsername)
                                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(procurementDataSnapshot: DataSnapshot) {
                                                    if (procurementDataSnapshot.exists()) {
                                                        val procurementUserId = procurementDataSnapshot.children.first().key

                                                        userRef.orderByChild("username").equalTo(picUsername)
                                                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                                                override fun onDataChange(picDataSnapshot: DataSnapshot) {
                                                                    if (picDataSnapshot.exists()) {
                                                                        val picUserId = picDataSnapshot.children.first().key

                                                                        userRef.orderByChild("username").equalTo(materialUsername)
                                                                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                                                                override fun onDataChange(materialDataSnapshot: DataSnapshot) {
                                                                                    if (materialDataSnapshot.exists()) {
                                                                                        val materialUserId = materialDataSnapshot.children.first().key

                                                                                        // Setelah mendapatkan semua ID pengguna terkait, buat dan simpan proyek
                                                                                        val projectId = ref.push().key
                                                                                        val project = Project(projectId!!, projectname, areaproject, adminProjectUserId!!, projectManagerUserId!!, procurementUserId!!, picUserId!!, materialUserId!!)

                                                                                        projectId?.let {
                                                                                            ref.child(it).setValue(project).addOnCompleteListener { task ->
                                                                                                if (isAdded) {
                                                                                                    if (task.isSuccessful) {
                                                                                                        Toast.makeText(
                                                                                                            requireContext(),
                                                                                                            "Data berhasil ditambahkan",
                                                                                                            Toast.LENGTH_SHORT
                                                                                                        ).show()
                                                                                                        clearInputFields() // Panggil metode clearInputFields()
                                                                                                    } else {
                                                                                                        Toast.makeText(
                                                                                                            requireContext(),
                                                                                                            "Gagal menambahkan data",
                                                                                                            Toast.LENGTH_SHORT
                                                                                                        ).show()
                                                                                                        clearInputFields() // Panggil metode clearInputFields()
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        showUserNotFoundError(materialUsername)
                                                                                    }
                                                                                }

                                                                                override fun onCancelled(materialDatabaseError: DatabaseError) {
                                                                                    handleDatabaseError(materialDatabaseError)
                                                                                }
                                                                            })
                                                                    } else {
                                                                        showUserNotFoundError(picUsername)
                                                                    }
                                                                }

                                                                override fun onCancelled(picDatabaseError: DatabaseError) {
                                                                    handleDatabaseError(picDatabaseError)
                                                                }
                                                            })
                                                    } else {
                                                        showUserNotFoundError(procurementUsername)
                                                    }
                                                }

                                                override fun onCancelled(procurementDatabaseError: DatabaseError) {
                                                    handleDatabaseError(procurementDatabaseError)
                                                }
                                            })
                                    } else {
                                        showUserNotFoundError(projectManagerUsername)
                                    }
                                }

                                override fun onCancelled(projectManagerDatabaseError: DatabaseError) {
                                    handleDatabaseError(projectManagerDatabaseError)
                                }
                            })
                    } else {
                        showUserNotFoundError(adminProjectUsername)
                    }
                }

                override fun onCancelled(adminProjectDatabaseError: DatabaseError) {
                    handleDatabaseError(adminProjectDatabaseError)
                }
            })
    }

    private fun showUserNotFoundError(username: String) {
        Toast.makeText(
            requireContext(),
            "Pengguna $username tidak ditemukan",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleDatabaseError(databaseError: DatabaseError) {
        Log.e("SaveProject", "Error fetching user for username", databaseError.toException())
        Toast.makeText(requireContext(), "Failed to fetch user", Toast.LENGTH_SHORT).show()
    }

    private fun clearInputFields() {
        binding.edtProjectnameAdd.setText("")

    }


}
