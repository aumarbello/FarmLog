package com.aumarbello.farmlog.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aumarbello.farmlog.R
import com.aumarbello.farmlog.databinding.FragmentEntryBinding
import com.aumarbello.farmlog.di.FarmLogViewModelFactory
import com.aumarbello.farmlog.models.FarmLocation
import com.aumarbello.farmlog.models.FarmLogEntity
import com.aumarbello.farmlog.utils.EventObserver
import com.aumarbello.farmlog.utils.appComponent
import com.aumarbello.farmlog.utils.fadeView
import com.aumarbello.farmlog.utils.showSnackBar
import com.aumarbello.farmlog.viewmodels.EntrySharedViewModel
import com.aumarbello.farmlog.viewmodels.EntryViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class EntryFragment : Fragment(R.layout.fragment_entry) {
    @Inject
    lateinit var factory: FarmLogViewModelFactory
    private lateinit var viewModel: EntryViewModel
    private lateinit var sharedViewModel: EntrySharedViewModel
    private lateinit var binding: FragmentEntryBinding
    private lateinit var coordinatesAdapter: CoordinatesAdapter

    private lateinit var launchMode: String
    private lateinit var currentImagePath: String
    private val locationReq = 11
    private val imageReq = 21

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appComponent?.inject(this)
        viewModel = ViewModelProvider(this, factory)[EntryViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity(), factory)[EntrySharedViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentEntryBinding.bind(view)

        coordinatesAdapter = CoordinatesAdapter(sharedViewModel::removeCoordinate)
        binding.coordinateList.adapter = coordinatesAdapter

        setObservers()
        setListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == imageReq && resultCode == Activity.RESULT_OK) {
            sharedViewModel.setImagePath(currentImagePath)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == locationReq && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            pickCurrentLocation(launchMode)
        } else {
            showSnackBar("Permission denied, kindly pick location on map")
            openMap(launchMode)
        }
    }

    private fun setObservers() {
        viewModel.response.observe(viewLifecycleOwner, Observer {
            findNavController().popBackStack()
        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            showSnackBar(it)
        })
        viewModel.loader.observe(viewLifecycleOwner, Observer {
            binding.loader.fadeView(it)
        })

        sharedViewModel.imagePath.observe(viewLifecycleOwner, Observer {
            val imageFile = File(it)
            if(imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
                with(binding.profileImage) {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setImageBitmap(bitmap)
                }
            }
        })

        sharedViewModel.locations.observe(viewLifecycleOwner, Observer {
            coordinatesAdapter.submitList(it)

            binding.viewOnMap.isVisible = it.size >= 3
        })

        sharedViewModel.farmLocation.observe(viewLifecycleOwner, Observer {
            binding.farmLocation.text = getString(R.string.format_location, it.latitude, it.longitude)
        })
    }

    private fun setListeners() {
        binding.save.setOnClickListener {
            if (validate()) {
                viewModel.addFarm(createEntry())
            }
        }

        binding.profileImage.setOnClickListener {
            takePicture()
        }

        binding.addCoordinate.setOnClickListener {
            showOptionsDialog(MapFragment.launchMultiple)
        }

        binding.farmLocationLayout.setOnClickListener {
            showOptionsDialog(MapFragment.launchSingle)
        }
    }

    private fun validate(): Boolean {
        if (sharedViewModel.imagePath.value == null) {
            showSnackBar(R.string.error_image_not_selected)

            return false
        }

        val namePattern = Pattern.compile("(\\w{2,})( )(\\w{2,})")
        if (!validateInputLayout(binding.fullName, binding.nameLayout,
                R.string.error_full_name) {
                namePattern.matcher(it).matches()
            }
        ) {
            return false
        }

        val phoneNumberPattern = Pattern.compile("[+]?[(]?\\d{3}[)]?\\d{3}\\d{4,7}")
        if (!validateInputLayout(binding.phoneNumber, binding.phoneNumberLayout,
                R.string.error_phone_number
            ) {
                phoneNumberPattern.matcher(it).matches()
            }
        ) {
            return false
        }

        if (!validateInputLayout(binding.age, binding.ageLayout, R.string.error_age) {
                IntRange(15, 100).contains(it.toInt())
            }
        ) {
            return false
        }

        val selectedGender = binding.genderGroup.checkedRadioButtonId
        if (selectedGender == -1) {
            showSnackBar(R.string.error_gender)
            return false
        }

        if (!validateInputLayout(binding.farmName, binding.farmNameLayout, R.string.error_farm_name) {
                it.length > 2
            }
        ) {
            return false
        }

        if (sharedViewModel.farmLocation.value == null) {
            showSnackBar(R.string.error_farm_location)
            return false
        }

        if (!binding.viewOnMap.isVisible) {
            showSnackBar(R.string.error_coordinates)
            return false
        }

        return true
    }

    private fun validateInputLayout(
        input: TextInputEditText,
        layout: TextInputLayout,
        @StringRes errorMessage: Int,
        validator: (String) -> Boolean
    ): Boolean {
        val text = input.text?.toString()

        if (text.isNullOrEmpty() || !validator(text)) {
            layout.error = getString(errorMessage)
            addTextWatcher(input, layout, validator)
            return false
        }

        return true
    }

    private fun addTextWatcher(
        input: TextInputEditText,
        layout: TextInputLayout,
        validator: (String) -> Boolean
    ) {
        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (validator(s.toString())) {
                    input.removeTextChangedListener(this)

                    layout.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun createEntry(): FarmLogEntity {
        return FarmLogEntity(
            sharedViewModel.imagePath.value!!,
            binding.fullName.text.toString(),
            binding.age.text.toString().toInt(),
            getSelectedGender(),
            binding.farmName.text.toString(),
            sharedViewModel.farmLocation.value!!,
            sharedViewModel.locations.value!!
        )
    }

    private fun getSelectedGender(): String {
        return if (binding.genderGroup.checkedRadioButtonId == R.id.male)
            "Male"
        else
            "Female"
    }

    private fun showOptionsDialog(launchMode: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.label_dialog_title)
            .setPositiveButton(R.string.action_current_location) { dialog, _ ->
                dialog.dismiss()

                if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(requireContext()) == ConnectionResult.SUCCESS) {
                    pickCurrentLocation(launchMode)
                } else {
                    showSnackBar("Locations service is unavailable, kindly pick location on map")

                    openMap(launchMode)
                }
            }
            .setNegativeButton(R.string.action_pick_on_map) { dialog, _ ->
                dialog.dismiss()

                openMap(launchMode)
            }.show()
    }

    private fun pickCurrentLocation(launchMode: String) {
        val locationPerm = Manifest.permission.ACCESS_FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(requireContext(), locationPerm) == PackageManager.PERMISSION_DENIED) {
            this.launchMode = launchMode
            requestPermissions(arrayOf(locationPerm), locationReq)
            return
        }

        val client = LocationServices.getFusedLocationProviderClient(requireContext())
        client.lastLocation.addOnSuccessListener {
            if (launchMode == MapFragment.launchSingle) {
                sharedViewModel.setFarmLocation(FarmLocation(it.latitude, it.longitude))
            } else {
                sharedViewModel.addCoordinate(FarmLocation(it.latitude, it.longitude))
            }
        }.addOnFailureListener {
            showSnackBar("Failed to retrieve current location, kindly pick location on map")
            openMap(launchMode)
        }
    }

    private fun openMap(launchMode: String) {
        val args = Bundle().apply {
            putString(MapFragment.launchMode, launchMode)
        }
        findNavController().navigate(R.id.mapFragment, args)
    }

    private fun takePicture() {
        val dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val timeStamp = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())

        val imageFile = File.createTempFile("Avatar_$timeStamp", ".jpg", dir).apply {
            currentImagePath = absolutePath
        }

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(requireContext().packageManager)?.also {
                val uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.aumarbello.farmlog.fileprovider",
                    imageFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, imageReq)
            }
        }
    }
}
