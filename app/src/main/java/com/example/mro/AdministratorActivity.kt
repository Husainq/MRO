package com.example.mro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mro.databinding.ActivityAdministratorBinding

class AdministratorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdministratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdministratorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeFragment(HomeAdministratorFragment())
        binding.navbar.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.beranda -> changeFragment(HomeAdministratorFragment())
                R.id.profil -> changeFragment(ProfileAdministratorFragment())
                else -> {}
            }
            true
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

}