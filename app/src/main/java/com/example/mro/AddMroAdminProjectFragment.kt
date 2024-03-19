package com.example.mro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mro.R
import com.example.mro.adapter.ProductAdapter
import com.example.mro.data_class.Product
import com.google.firebase.database.*

class AddMROAdminProjectFragment : Fragment() {

    private lateinit var mroRecyclerView: RecyclerView
    private lateinit var mroAdapter: MROAdapter
    private val mroList = mutableListOf<MRO>()

    private val mrosRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("MROs")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_mro_admin_project, container, false)

        // Inisialisasi RecyclerView
        mroRecyclerView = rootView.findViewById(R.id.rv_mro_list)
        mroAdapter = MROAdapter(mroList)
        mroRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mroRecyclerView.adapter = mroAdapter

        // Tambahkan listener untuk mendapatkan daftar MRO dari Firebase Realtime Database
        mrosRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    mroList.clear()
                    for (mroSnapshot in snapshot.children) {
                        val mro = mroSnapshot.getValue(MRO::class.java)
                        mro?.let { mroList.add(it) }
                    }
                    // Perbarui dataset saat ada perubahan
                    mroAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e(TAG, "Error fetching MROs: ${error.message}")
            }
        })

        return rootView
    }
}
