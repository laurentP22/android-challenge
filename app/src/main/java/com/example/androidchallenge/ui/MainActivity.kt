package com.example.androidchallenge.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.androidchallenge.R
import com.example.androidchallenge.constant.Constants.Companion.BIOMETRIC
import com.example.androidchallenge.constant.Constants.Companion.PIN
import com.example.androidchallenge.databinding.ActivityMainBinding
import com.example.androidchallenge.ui.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleIntent(intent)

        // NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val fullScreenFragments = listOf(
                R.id.seriesDetailsFragment,
                R.id.searchesFragment,
                R.id.authenticationFragment
            )
            binding.bottomNav.visibility = if (fullScreenFragments.contains(destination.id))
                View.GONE else View.VISIBLE
        }

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.seriesFragment,
                R.id.favoritesFragment,
                R.id.personsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        viewModel.auth.observe(this, {
            if (it == BIOMETRIC) {
                val biometricManager = BiometricManager.from(this)
                val biometricAvailable =
                    biometricManager.canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
                if(biometricAvailable){
                    authenticateWithBiometric()
                }else{
                    navController.navigate(R.id.authenticationFragment)
                }
            } else if (it == PIN) {
                navController.navigate(R.id.authenticationFragment)
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.getUserAuth()
    }

    private fun authenticateWithBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        navController.navigate(R.id.authenticationFragment)
                    } else {
                        finish()
                    }
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.login_biometric_title))
            .setSubtitle(getString(R.string.login_biometric_subtitle))
            .setNegativeButtonText(getString(R.string.login_pin_subtitle))
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        menuInflater.inflate(R.menu.menu_search, menu)

        // Associate searchable configuration with the SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            navController.navigate(R.id.addAuthenticationFragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val bundle = bundleOf("query" to query)
            navController.navigate(R.id.searchesFragment, bundle)
        }
    }
}