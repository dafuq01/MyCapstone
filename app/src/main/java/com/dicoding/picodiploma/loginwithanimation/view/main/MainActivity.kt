package com.dicoding.picodiploma.loginwithanimation.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.model.UserPreference
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.fragment.DeteksiFragment
import com.dicoding.picodiploma.loginwithanimation.view.fragment.HomeFragment
import com.dicoding.picodiploma.loginwithanimation.view.fragment.PembelianFragment
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var logoutButton:Button
    private lateinit var logoutimage:ImageView
    private var homefragment=HomeFragment()
    private var deteksiFragment=DeteksiFragment()
    private var pembelianFragment=PembelianFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()

        logoutimage=findViewById(R.id.logout)

        logoutimage.setOnClickListener{
            val dialog=Dialog(this)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.custom_dialog_layout)

            val btnclose=dialog.findViewById<TextView>(R.id.btn_cancel)
            val btnoke=dialog.findViewById<TextView>(R.id.btn_okay)

            btnclose.setOnClickListener {
                dialog.dismiss()
            }

            btnoke.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                dialog.dismiss()
                mainViewModel.logout()
            }
            dialog.show()
        }
        replaceFragment(homefragment)
        val bottom_navigation=findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottom_navigation.setOnItemSelectedListener{
            when(it.itemId){
                R.id.home->replaceFragment(homefragment)
                R.id.deteksi->replaceFragment(deteksiFragment)
                R.id.buying->replaceFragment(pembelianFragment)
            }
            true
        }
    }

   private fun replaceFragment(fragment: Fragment){
       if (fragment!=null){
           val transaction=supportFragmentManager.beginTransaction()
           transaction.replace(R.id.fragment_container,fragment)
           transaction.commit()

       }


   }



    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
                binding.nameTextView.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        })
    }

    private fun playAnimation() {


        val name = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(name, message)
            startDelay = 500
        }.start()
    }
}