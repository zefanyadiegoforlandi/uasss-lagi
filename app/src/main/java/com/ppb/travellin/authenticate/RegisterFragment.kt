package com.ppb.travellin.authenticate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ppb.travellin.databinding.FragmentRegisterBinding
import com.ppb.travellin.dialog.WelcomeSheetFragment.Companion.viewPagerCompanion
import com.ppb.travellin.services.api.FireAuth
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private val binding by lazy { FragmentRegisterBinding.inflate(layoutInflater) }
    private val fireAuth by lazy {
        FireAuth()
    }

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            editTextPasswordRegister.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    containerEditTextPasswordRegister.error = null
                    containerEditTextPasswordRegister.isEndIconVisible = true
                }
            })

        }


        binding.buttonSignup.setOnClickListener {
            with(binding) {
                if(editTextUsernameRegister.text.toString().isEmpty()) {
                    containerEditTextUsernameRegister.error = "Mohon masukkan username"
                    return@setOnClickListener
                }

                if(editTextEmailRegister.text.toString().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmailRegister.text.toString()).matches()) {
                    containerEditTextEmailRegister.error = "Email tidak valid"
                    return@setOnClickListener
                }

                if(editTextPasswordRegister.text.toString().isEmpty()) {
                    containerEditTextPasswordRegister.error = "Mohon masukkan password"
                    return@setOnClickListener
                }

                createUSerAccount(editTextUsernameRegister.text.toString().trim(), editTextEmailRegister.text.toString().trim(), editTextNimRegister.text.toString().trim(), editTextPasswordRegister.text.toString().trim())


            }
        }

    }

    private fun createUSerAccount(username: String, email: String, nim : String, usrpwd: String) {
        lifecycleScope.launch {
            if (fireAuth.registerUser(username, email, nim, usrpwd)) {
                Toast.makeText(requireContext(), "Berhasil membuat akun", Toast.LENGTH_SHORT).show()
                if (viewPagerCompanion != null) {
                    viewPagerCompanion!!.currentItem = 1
                }
            } else {
                Toast.makeText(requireContext(), "Gagal membuat akun : Akun telah terpakai", Toast.LENGTH_SHORT).show()
            }
        }
    }




}