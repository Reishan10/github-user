package com.dicoding.github.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.github.R
import com.dicoding.github.data.model.User
import com.dicoding.github.databinding.ActivityMainBinding
import com.dicoding.github.ui.detail.DetailUserActivity
import com.dicoding.github.ui.favorite.FavoriteActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: UserViewModel
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        setContentView(binding.root)

        val sharedPrefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
        val isDarkModeEnabled = sharedPrefs.getBoolean("isDarkModeEnabled", false)

        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }


        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity,DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME,data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID,data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR,data.avatar_url)
                    startActivity(it)
                }
            }
        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserViewModel::class.java)
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            etQuery.setOnKeyListener{v, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUser().observe(this, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    private fun searchUser() {
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) {
                showLoading(false)
                adapter.setList(ArrayList())
                return
            }
            showLoading(true)
            viewModel.setSearchUser(query)
        }
    }


    private fun showLoading(state: Boolean) {
        binding.apply {
            progressBar.visibility = if (state) View.VISIBLE else View.GONE
            if (state) {
                rvUser.visibility = View.GONE
                binding.tvEmptyMessage.visibility = View.GONE
            } else {
                if (adapter.itemCount == 0) {
                    rvUser.visibility = View.GONE
                    binding.tvEmptyMessage.visibility = View.VISIBLE
                } else {
                    rvUser.visibility = View.VISIBLE
                    binding.tvEmptyMessage.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_favorite -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }

            R.id.menu_theme -> {
                val sharedPrefs = getSharedPreferences("theme_prefs", MODE_PRIVATE)
                val isDarkModeEnabled = !sharedPrefs.getBoolean("isDarkModeEnabled", false)
                sharedPrefs.edit().putBoolean("isDarkModeEnabled", isDarkModeEnabled).apply()

                if (isDarkModeEnabled) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                recreate() // Merestart activity untuk menerapkan perubahan tema secara dinamis

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}