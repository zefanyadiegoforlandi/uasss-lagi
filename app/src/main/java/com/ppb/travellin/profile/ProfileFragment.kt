package com.ppb.travellin.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ppb.travellin.R
import com.ppb.travellin.SystemThemeViewModel
import com.ppb.travellin.TravellinApps
import com.ppb.travellin.databinding.FragmentProfileBinding
import com.ppb.travellin.services.ApplicationPreferencesManager
import com.ppb.travellin.services.api.FireAuth
import com.ppb.travellin.services.database.AppDatabaseViewModel
import com.ppb.travellin.services.database.AppDatabaseViewModelFactory
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val systemThemeViewModel: SystemThemeViewModel by activityViewModels()
    private lateinit var appViewModel : AppDatabaseViewModel

    private var imageUri: Uri? = null
    private val launcherPhoto = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data

            val newFile = File(requireContext().filesDir, "profile_image.jpg")

            val inputStream = requireContext().contentResolver.openInputStream(imageUri!!)

            val outputStream = FileOutputStream(newFile)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            val newUri = Uri.fromFile(newFile)

            val preferences = ApplicationPreferencesManager(requireContext())
            preferences.saveProfileImage(newUri.toString())

            loadImage(newUri)

            Toast.makeText(requireContext(), "Berhasil memilih gambar", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = AppDatabaseViewModelFactory((requireActivity().application as TravellinApps).appRepository)
        appViewModel = ViewModelProvider(requireActivity(), factory)[AppDatabaseViewModel::class.java]

        checkProfileImage()

        setUsernameEmail()


        darkModeComponent()

        adminManager()

        binding.buttonImageViewUnggahFotoProfilBaru.setOnClickListener {
            pickPhoto()
        }


        binding.buttonImageViewDeleteAccount.setOnClickListener {
            alertConfirmation(true, "menghapus akun")
        }

        binding.buttonLogOut.setOnClickListener {
            alertConfirmation()

        }
    }

    private fun checkProfileImage() {
        val preferences = ApplicationPreferencesManager(requireContext())
        val localPath = preferences.profileImage
        Log.d("ProfileFragment", "checkProfileImage: $localPath")

        if (localPath != null) {
            imageUri = Uri.parse(localPath)
            imageUri?.let {
                loadImage(it)
            }
        }

    }

    private fun alertConfirmation(isDeleteAccountToo : Boolean = false, msg : String = "keluar") {
        MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
            .setTitle("Keluar")
            .setMessage("Apakah Anda yakin ingin $msg?")
            .setPositiveButton("Batal", null)
            .setNegativeButton("Ya") { _, _ ->
                if (isDeleteAccountToo) {
                    lifecycleScope.launch {
                        val fireAuth = FireAuth()
                        fireAuth.deleteUser(ApplicationPreferencesManager(requireContext()).usernameId!!)
                        Toast.makeText(requireContext(), "Akun berhasil dihapus", Toast.LENGTH_SHORT).show()
                    }
                }
                logout()
            }
            .show()
    }

    private fun adminManager() {
        binding.containerKelolaAccount.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToManagerFragment()
            findNavController().navigate(action)
        }
        binding.buttonImageViewManagerAccount.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToManagerFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUsernameEmail() {
        val preferences = ApplicationPreferencesManager(requireContext())
        binding.textViewUsername.text = preferences.usernameName
        binding.textViewNim.text = preferences.usernameNim

        if (preferences.isUserAdmin) {
            binding.containerKelolaAccount.visibility = View.VISIBLE
        } else {
            binding.containerKelolaAccount.visibility = View.GONE
        }
    }


    private fun darkModeComponent() {

        // Observe the theme mode
        systemThemeViewModel.switchState.value?.let { binding.switchThemeMode.isChecked = it }
        systemThemeViewModel.switchLabel.value?.let { binding.switchThemeMode.text = it }

        binding.switchThemeMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The switch is checked, switch to dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchThemeMode.text = "Dark Mode"
            } else {
                // The switch is not checked, switch to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchThemeMode.text = "Light Mode"
            }
            systemThemeViewModel.switchState.value = isChecked
            systemThemeViewModel.switchLabel.value = binding.switchThemeMode.text.toString()

        }
    }

    private fun pickPhoto() {
        val mediaStoreIntent = Intent(Intent.ACTION_PICK).apply {
            setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        }
        launcherPhoto.launch(mediaStoreIntent)
    }

    private fun loadImage(localPath: Uri) {
        // load image
        binding.imageViewProfile.load(localPath) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.person_fill1_wght400_grad0_opsz24)
            error(R.drawable.person_fill1_wght400_grad0_opsz24)
        }
        Log.d("ProfileFragment", "loadImage: $localPath")
    }



    private fun logout() {
        lifecycleScope.launch {
            val fireAuth = FireAuth()
            fireAuth.logout(ApplicationPreferencesManager(requireContext()).usernameId!!)
            ApplicationPreferencesManager(requireContext()).deleteUsername()
            appViewModel.deleteAllTicketHistory()
            appViewModel.deleteAllQueues()
            requireActivity().finish()
        }

    }


}