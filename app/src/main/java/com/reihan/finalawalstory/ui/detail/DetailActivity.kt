package com.reihan.finalawalstory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.reihan.finalawalstory.databinding.ActivityDetailBinding
import com.reihan.finalawalstory.model.DetailViewModel
import com.reihan.finalawalstory.remote.data.Story
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel : DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
    }

    private fun setData(it: Story) {
        Glide.with(this)
            .load(it.photoUrl)
            .into(binding.ivDetailStory)

        binding.tvTitleStory.text = it.name
        binding.tvStoryDescription.text = it.description
    }

    private fun getData() {
        val id = intent.getStringExtra(ID)
        viewModel.detailResult.observe(this) {
            setData(it)
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
        viewModel.fetchDetail(id.toString())
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    companion object {
        const val ID = "id"
    }
}
