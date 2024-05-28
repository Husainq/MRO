package com.example.mro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mro.databinding.FragmentMngProjectBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MngProjectFragment : Fragment() {
    lateinit var binding: FragmentMngProjectBinding
    private lateinit var projectList: MutableList<Project>
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMngProjectBinding.inflate(inflater, container, false)

        binding.backProject.setOnClickListener{
            val profileAdministratorFragment = ProfileAdministratorFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,profileAdministratorFragment)
            transaction.commit()
        }

        binding.addProject.setOnClickListener{
            val addProjectrFragment = AddProjectFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,addProjectrFragment)
            transaction.commit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("Project")
        projectList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) { // Pastikan Fragment terpasang sebelum menggunakan requireActivity()
                    if (snapshot.exists()) {
                        projectList.clear()
                        for (a in snapshot.children) {
                            val project = a.getValue(Project::class.java)
                            project ?.let {
                                projectList.add(it)
                            }
                        }
                        val adapter = ProjectAdapter(
                            requireActivity(),
                            R.layout.detil_project,
                            projectList
                        )
                        binding.hasilProject.adapter = adapter
                        println(projectList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

}