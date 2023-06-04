package com.reihan.finalawalstory.ui.story

import android.Manifest.permission.CAMERA
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.reihan.finalawalstory.R
import com.reihan.finalawalstory.databinding.ActivityNewStoryBinding
import com.reihan.finalawalstory.remote.module.createCustomTempFile
import com.reihan.finalawalstory.remote.module.reduceFileImage
import com.reihan.finalawalstory.remote.module.uriToFile
import com.reihan.finalawalstory.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class NewStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewStoryBinding
    private val viewModel: NewStoryViewModel by viewModel()
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.title = getString(R.string.UI_action_bar_new_story)

        binding.buttonCamera.setOnClickListener{
            val cameraPermission = ContextCompat.checkSelfPermission(this, CAMERA)
            if (cameraPermission != PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(CAMERA), REQUEST_CAMERA_PERMISSION)
                }
            }else{
                takePhoto()
            }
        }

        binding.buttonGallery.setOnClickListener{
            startGallery()
        }
        binding.buttonUpload.setOnClickListener{
            if (getFile != null){
                addStory()
            }else{
                Toast.makeText(this, R.string.UI_info_pick_information, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                Toast.makeText(this, getString(R.string.UI_denied_camera_permission), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addStory() {
        val description = binding.etStoryDescription.text.toString().toRequestBody("text/plain".toMediaType())
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart : MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        viewModel.addStoryResult.observe(this){
            setStateAdd()
        }
        viewModel.isLoading.observe(this){
            showLoading(it)
        }
        viewModel.addStory(imageMultipart, description)
    }

    private fun setStateAdd() {
        val build : AlertDialog.Builder = AlertDialog.Builder(this)
        build.setTitle("Message")
        build.setMessage(getString(R.string.UI_success_upload_story))
        build.setPositiveButton("OK"){ dialog, _ ->
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            dialog.dismiss()
        }
        val dialog : AlertDialog = build.create()
        dialog.show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.UI_choose_image))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImg : Uri = it.data?.data as Uri
            selectedImg.let {Uri->
                val myFile = uriToFile(Uri, this)
                getFile = myFile
                binding.ivStoryImagePreview.setImageURI(Uri)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoUri: Uri = FileProvider.getUriForFile(
                this,
                AUTHOR,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private lateinit var currentPhotoPath:String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile
            binding.ivStoryImagePreview.setImageBitmap(result)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.isVisible = state
    }

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1
        const val AUTHOR = "com.reihan.finalawalstory"
    }
}