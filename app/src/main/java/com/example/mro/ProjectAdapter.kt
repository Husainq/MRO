package com.example.mro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProjectAdapter(
    val projectContext: Context,
    val layoutResId: Int,
    val projectList: List<Project>
) : ArrayAdapter<Project>(projectContext, layoutResId, projectList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(projectContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val o_project: TextView = view.findViewById(R.id.nama_project)
        val o_area: TextView = view.findViewById(R.id.area_project)
        val o_pj: TextView = view.findViewById(R.id.pj_project)
        val imgEdit: ImageView = view.findViewById(R.id.icn_edit)
        val anggota = projectList[position]

        // Mendapatkan nama pengguna (username) dari ID pengguna (project_manager) dan menampilkannya
        getUsernameFromUserId(anggota.project_manager) { username ->
            o_pj.text = username
        }

        o_project.text = anggota.project
        o_area.text = anggota.area

        return view
    }

    private fun getUsernameFromUserId(userId: String, callback: (String) -> Unit) {
        // Mengambil data pengguna dari Firebase Realtime Database berdasarkan ID pengguna
        // Implementasikan logika ini menggunakan DatabaseReference atau Query sesuai kebutuhan aplikasi Anda
        val userRef = FirebaseDatabase.getInstance().getReference("User").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val username = dataSnapshot.child("username").getValue(String::class.java)
                if (username != null) {
                    callback(username)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Penanganan kesalahan jika diperlukan
            }
        })
    }
}
