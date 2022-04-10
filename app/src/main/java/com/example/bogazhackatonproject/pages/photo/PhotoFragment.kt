package com.example.bogazhackatonproject.pages.photo

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.bogazhackatonproject.R
import com.example.bogazhackatonproject.databinding.FragmentPhotoBinding
import com.example.bogazhackatonproject.pages.chat.ChatViewModel
import com.example.bogazhackatonproject.pages.login.ApiStatus
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap: Bitmap? = null
    var secilenGorsel: Uri? = null
    private val photoViewModel by lazy { ChatViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgPhoto.setOnClickListener {
            gorselSec()
        }
        registerLauncher()
        observeLiveData()
    }


    private fun gorselSec() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
            ) {
                Snackbar.make(requireView(), "İzin verilmedi", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin verin") {
                        permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            } else {
                permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun observeLiveData() {
        val imageUrl="http://farmai.pagekite.me/Blur.png"
        with(binding) {
            photoViewModel.status.observe(viewLifecycleOwner) {
                when (it) {
                    ApiStatus.DONE -> {
                        progressBarPhoto.visibility = View.GONE
                        Toast.makeText(requireContext(), "Fotoğraf gönderildi", Toast.LENGTH_SHORT)
                            .show()
                        Thread.sleep(2000)
                        Glide.with(requireContext()).load(imageUrl).into(binding.imageViewNew)
                    }
                    ApiStatus.LOADING -> progressBarPhoto.visibility = View.VISIBLE
                    else -> {
                        progressBarPhoto.visibility = View.GONE
                        Toast.makeText(requireContext(), "Bir hata başverdi", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        secilenGorsel = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source =
                                    ImageDecoder.createSource(requireActivity().contentResolver,
                                        secilenGorsel!!)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                photoViewModel.loadImage(secilenGorsel!!)
                                binding.imgPhoto.setImageBitmap(selectedBitmap)
                            } else {
                                selectedBitmap =
                                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver,
                                        secilenGorsel!!)
                                photoViewModel.loadImage(secilenGorsel!!)
                                binding.imgPhoto.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(requireContext(), "Permisson needed!", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}