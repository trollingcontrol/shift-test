package com.trollingcont.shifttest.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import com.trollingcont.shifttest.R
import com.trollingcont.shifttest.databinding.ActivityMainBinding
import com.trollingcont.shifttest.fragments.GreetingFragment
import com.trollingcont.shifttest.fragments.RegisterFragment
import com.trollingcont.shifttest.usermanager.AppUserManager

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appUserManager = AppUserManager(this)
        val userEntity = appUserManager.getUser()

        if (savedInstanceState == null) {
            val fragmentInstance = if (userEntity == null)
                RegisterFragment.newInstance(appUserManager)
            else
                GreetingFragment.newInstance(userEntity)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentInstance)
                .commit()
        }
    }
}