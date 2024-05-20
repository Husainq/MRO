package com.example.mro

import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter

data class RoleAdapter(
    val userList: MutableList<String>,
    val userAdapter: ArrayAdapter<String>
)
