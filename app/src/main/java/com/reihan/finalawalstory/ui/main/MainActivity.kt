package com.reihan.finalawalstory.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.reihan.finalawalstory.R
import com.reihan.finalawalstory.databinding.ActivityMainBinding
import com.reihan.finalawalstory.remote.data.ListStory
import com.reihan.finalawalstory.remote.preferences.ViewModelPreferences
import com.reihan.finalawalstory.ui.detail.DetailActivity
import com.reihan.finalawalstory.ui.launcher.LauncherActivity
import com.reihan.finalawalstory.ui.story.NewStoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private val viewModelPreferences: ViewModelPreferences by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.UI_action_bar)

        binding.fabAddNewStory.setOnClickListener {
            startActivity(Intent(this, NewStoryActivity::class.java))
        }

        viewModel.fetchStory.observe(this) {
            if (it != null) {
                setViewData(it)
            }
        }

        viewModel.fetchStory()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        setActionBarTitle()
    }

    private fun setActionBarTitle() {
        val userModel = viewModelPreferences.loadUserData()
        val username = userModel?.name

        supportActionBar?.title = getString(R.string.label_greeting_user, username)
    }

    private fun setViewData(userStory: List<ListStory>) {
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.layoutManager = LinearLayoutManager(this@MainActivity)

        val adapter = MainAdapter(userStory)
        adapter.setOnItemClickListener {
            val adapterToDetail = Intent(this, DetailActivity::class.java)
            adapterToDetail.putExtra("id", userStory[it].id)
            startActivity(adapterToDetail)
        }
        binding.rvStory.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val logoutMenu = menu?.findItem(R.id.logout)
        logoutMenu?.setOnMenuItemClickListener {
            viewModelPreferences.clearUserData()
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
            true
        }
        return true
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }
}
