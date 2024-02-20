package com.natiqhaciyef.prodocument.ui.view.registration.create_account

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.common.objects.ErrorMessages
import com.natiqhaciyef.prodocument.databinding.FragmentCompleteProfileBinding
import com.natiqhaciyef.prodocument.domain.model.mapped.MappedUserModel
import com.natiqhaciyef.prodocument.ui.base.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkFullNameAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkGenderAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPhoneAcceptanceCondition
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class CompleteProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentCompleteProfileBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private var currentSelectedTime: Long = 0L
    private var imageData: Uri? = null
    private var genderSelection: String = "Not-selected"
    private val genderList = listOf("Male", "Female")
    private val viewModel: CompleteProfileViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            FragmentCompleteProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        registerPermissionForGallery()

        with(binding) {
            datePickerDialog(calendar)
            genderDropDownConfig()

            // validations
            fullNameValidation()
            phoneValidation()
            genderValidation()

            goBackIcon.setOnClickListener { navigateBack() }
            continueButton.setOnClickListener { continueButtonClickAction() }
            completeProfileAccountImageEditIcon.setOnClickListener { selectImage() }
        }
    }

    private fun registerPermissionForGallery() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    // permission granted
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityLauncher.launch(intent)
                } else {
                    // permission denied
                }
            }

        activityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val data = result.data
                    if (data != null) {
                        imageData = data.data
                        imageData?.let {
                            binding.completeProfileAccountImage.setImageURI(it)
                        }
                    }
                }
            }
    }

    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                // request permission
                Snackbar.make(
                    requireView(),
                    "Permission needed for Gallery",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Give permission") {
                    // request permission
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            } else {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityLauncher.launch(intent)
        }
    }

    private fun continueButtonClickAction() {
        binding.apply {
            viewModel.collectDataFromCompleteProfileScreen(
                data = MappedUserModel(
                    name = completeProfileFullNameInput.text.toString(),
                    email = "",
                    phoneNumber = completeProfilePhoneNumberInput.text.toString(),
                    gender = genderSelection,
                    birthDate = completeProfileDOBInput.text.toString(),
                    imageUrl = imageData.toString(),
                    password = ""
                ),
                onSuccess = {
                    navigate(R.id.createAccountFragment)
                },
                onFail = {
                    Toast.makeText(requireContext(), "${it?.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
    }

    private fun genderDropDownConfig() {
        val genderAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down_gender_item, genderList)

        binding.apply {
            completeProfileGenderDropDownItem.hint = getString(R.string.gender)
            completeProfileGenderDropDownItem.setAdapter(genderAdapter)
            completeProfileGenderDropDownItem.setOnItemClickListener { adapterView, _, p, _ ->
                genderList.forEach {
                    if (adapterView.getItemAtPosition(p).toString() == it)
                        genderSelection = it
                }
            }
        }
    }

    private fun datePickerDialog(calendar: Calendar) {
        val listener = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            changeCalendar(calendar)
        }

        binding.apply {
            completeProfileDOBInput.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    listener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                )
                datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
                datePickerDialog.show()
            }
        }
    }

    private fun changeCalendar(calendar: Calendar) {
        currentSelectedTime = calendar.timeInMillis
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.UK)
        val date = sdf.format(calendar.time)
        if (calendar.time.time < System.currentTimeMillis())
            binding.completeProfileDOBInput.text = date
        else
            binding.completeProfileDOBInput.text = ErrorMessages.DATE_OVER_FLOW_ERROR
    }

    private fun fullNameValidation() {
        binding.apply {
            completeProfileFullNameInput.doOnTextChanged { text, start, before, count ->
                continueButton.isEnabled = checkFullNameAcceptanceCondition(text)
                        && checkPhoneAcceptanceCondition(completeProfilePhoneNumberInput.text)
                        && checkGenderAcceptanceCondition(completeProfileGenderDropDownItem.text)
                        && checkDateAcceptanceCondition()
            }
        }
    }

    private fun phoneValidation() {
        binding.apply {
            completeProfilePhoneNumberInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    continueButton.isEnabled = checkPhoneAcceptanceCondition(text)
                            && checkFullNameAcceptanceCondition(completeProfileFullNameInput.text)
                            && checkGenderAcceptanceCondition(completeProfileGenderDropDownItem.text)
                            && checkDateAcceptanceCondition()
                }

                override fun afterTextChanged(text: Editable?) {
                    text?.let {
                        val formattedNumber = viewModel.formatPhoneNumber(completeProfilePhoneNumberInput.editableText.toString())
                        if (formattedNumber != it.toString()) {
                            completeProfilePhoneNumberInput.removeTextChangedListener(this)

                            // Prevent IndexOutOfBoundsException
                            completeProfilePhoneNumberInput.setText(formattedNumber)
                            completeProfilePhoneNumberInput.setSelection(formattedNumber.length)

                            completeProfilePhoneNumberInput.addTextChangedListener(this)
                        }
                    }
                }
            })
        }
    }

    private fun genderValidation() {
        binding.apply {
            completeProfileGenderDropDownItem.setOnItemClickListener { adapterView, _, p, _ ->
                genderSelection = adapterView.getItemAtPosition(p).toString()

                continueButton.isEnabled =
                    checkPhoneAcceptanceCondition(completeProfilePhoneNumberInput.text)
                            && checkFullNameAcceptanceCondition(completeProfileFullNameInput.text)
                            && checkGenderAcceptanceCondition(genderSelection)
                            && checkDateAcceptanceCondition()
            }
        }
    }

    private fun checkDateAcceptanceCondition() =
        currentSelectedTime < Calendar.getInstance().timeInMillis
}