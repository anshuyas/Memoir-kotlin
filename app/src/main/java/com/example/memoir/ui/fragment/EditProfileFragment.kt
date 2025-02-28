package com.example.memoir.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.memoir.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        database = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.uid)

        // Load user data
        loadUserData()

        binding.changeProfilePictureButton.setOnClickListener {
            openImagePicker()
        }

        binding.saveProfileButton.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun loadUserData() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                    return
                }

                val username = snapshot.child("username").getValue(String::class.java)
                val email = snapshot.child("email").getValue(String::class.java)
                val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)

                binding.editTextUsername.setText(username)
                binding.editTextEmail.setText(email)
                binding.editTextEmail.isEnabled = false

                if (!profileImageUrl.isNullOrEmpty()) {
                    Glide.with(this@EditProfileFragment)
                        .load(profileImageUrl)
                        .into(binding.profileImageView)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.profileImageView.setImageURI(uri)
            }
        }

    private fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    private fun saveProfileChanges() {
        val newUsername = binding.editTextUsername.text.toString().trim()
        if (newUsername.isEmpty()) {
            binding.editTextUsername.error = "Username cannot be empty"
            return
        }

        val updates = hashMapOf<String, Any>("username" to newUsername)

        // If a new image is selected, upload it first
        if (imageUri != null) {
            val storageRef = storage.reference.child("profile_pictures/${auth.currentUser!!.uid}.jpg")
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        updates["profileImageUrl"] = uri.toString()
                        updateUserData(updates)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
        } else {
            updateUserData(updates)
        }
    }

    private fun updateUserData(updates: HashMap<String, Any>) {
        database.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

