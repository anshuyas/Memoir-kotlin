package com.example.memoir.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.memoir.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        val currentUser = auth.currentUser
        if (currentUser == null) {
            showToast("User not logged in")
            return
        }

        database = FirebaseDatabase.getInstance().reference.child("Users").child(currentUser.uid)

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
                    showToast("User data not found")
                    return
                }

                binding.apply {
                    editTextUsername.setText(snapshot.child("username").getValue(String::class.java))
                    editTextEmail.setText(snapshot.child("email").getValue(String::class.java))
                    editTextEmail.isEnabled = false

                    val profileImageUrl = snapshot.child("profileImageUrl").getValue(String::class.java)
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Glide.with(this@EditProfileFragment)
                            .load(profileImageUrl)
                            .into(profileImageView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed to load user data: ${error.message}")
            }
        })
    }

    private val imagePickerLauncher =
        registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                imageUri = it
                binding.profileImageView.setImageURI(it)
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

        imageUri?.let { uri ->
            val storageRef = storage.reference.child("profile_pictures/${auth.currentUser!!.uid}.jpg")
            storageRef.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        throw task.exception ?: Exception("Image upload failed")
                    }
                    storageRef.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    updates["profileImageUrl"] = downloadUri.toString()
                    updateUserData(updates)
                }
                .addOnFailureListener { e ->
                    showToast("Failed to upload image: ${e.message}")
                }
        } ?: updateUserData(updates)
    }

    private fun updateUserData(updates: HashMap<String, Any>) {
        database.updateChildren(updates)
            .addOnSuccessListener {
                showToast("Profile updated successfully")
            }
            .addOnFailureListener { e ->
                showToast("Failed to update profile: ${e.message}")
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
