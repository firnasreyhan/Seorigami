package com.project.seorigami.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.seorigami.R
import com.project.seorigami.databinding.ActivityMainBinding
import com.project.seorigami.util.Prefs
import com.project.seorigami.view.fragment.HomeFragment
import com.project.seorigami.view.fragment.HomeMitraFragment
import com.project.seorigami.view.fragment.OrderFragment
import com.project.seorigami.view.fragment.ProfileFragment
import com.project.seorigami.view.fragment.ProfileMitraFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentActive: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Prefs(this).user?.`as`.equals("penjahit")) {
            loadFragment(HomeMitraFragment())
        } else {
            loadFragment(HomeFragment())
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.page_home -> {
                if (Prefs(this).user?.`as`.equals("penjahit")) {
                    loadFragment(HomeMitraFragment())
                } else {
                    loadFragment(HomeFragment())
                }
                return true
            }
            R.id.page_order -> {
                loadFragment(OrderFragment())
                return true
            }
            R.id.page_profile -> {
                if (Prefs(this).user?.`as`.equals("penjahit")) {
                    loadFragment(ProfileMitraFragment())
                } else {
                    loadFragment(ProfileFragment())
                }
                return true
            }
        }
        return false
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        if (fragment != null) {
            fragmentActive = fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
            return true
        }
        return false
    }
}