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
import com.google.android.material.snackbar.Snackbar
import com.natiqhaciyef.common.constants.DATE_OVER_FLOW_ERROR
import com.natiqhaciyef.common.constants.EMPTY_STRING
import com.natiqhaciyef.common.constants.FORMATTED_DATE
import com.natiqhaciyef.common.constants.THIRTEEN
import com.natiqhaciyef.prodocument.databinding.FragmentCompleteProfileBinding
import com.natiqhaciyef.common.model.mapped.MappedUserModel
import com.natiqhaciyef.core.base.ui.BaseFragment
import com.natiqhaciyef.prodocument.ui.util.DefaultImplModels
import com.natiqhaciyef.uikit.manager.PermissionManager.Permission.Companion.PERMISSION_REQUEST
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkFullNameAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkGenderAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.checkPhoneAcceptanceCondition
import com.natiqhaciyef.prodocument.ui.util.InputAcceptanceConditions.formatPhoneNumber
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.contract.CompleteProfileContract
import com.natiqhaciyef.prodocument.ui.view.registration.create_account.viewmodel.CompleteProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.reflect.KClass


@AndroidEntryPoint
class CompleteProfileFragment(
    override val bindInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCompleteProfileBinding = FragmentCompleteProfileBinding::inflate,
    override val viewModelClass: KClass<CompleteProfileViewModel> = CompleteProfileViewModel::class
) : BaseFragment<FragmentCompleteProfileBinding, CompleteProfileViewModel, CompleteProfileContract.CompleteUiState, CompleteProfileContract.CompleteUiEvent, CompleteProfileContract.CompleteUiEffect>() {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityLauncher: ActivityResultLauncher<Intent>
    private var currentSelectedTime: Long = 0L
    private var imageData: Uri? = null
    private var genderSelection: String = "Not-selected"
    private var genderList = arrayOf<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        registerPermissionForGallery()
        genderList = resources.getStringArray(com.natiqhaciyef.common.R.array.genders)

        binding.apply {
            datePickerDialog(calendar)
            genderDropDownConfig()
            changeVisibilityOfProgressBar(false)

            // validations
            fullNameValidation()
            phoneValidation()
            genderValidation()

            goBackIcon.setOnClickListener { navigateBack() }
            continueButton.setOnClickListener { continueButtonClickEvent() }
            completeProfileAccountImageEditIcon.setOnClickListener { selectImage() }
        }
    }

    override fun onStateChange(state: CompleteProfileContract.CompleteUiState) {
        when {
            state.isLoading -> {
                changeVisibilityOfProgressBar(true)
            }

            else -> {
                changeVisibilityOfProgressBar(false)
                continueButtonClickAction(state.user)
            }
        }
    }

    override fun onEffectUpdate(effect: CompleteProfileContract.CompleteUiEffect) {
        when (effect) {
            is CompleteProfileContract.CompleteUiEffect.FieldNotCorrectlyFilledEffect -> {
                Toast.makeText(requireContext(), "${effect.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun changeVisibilityOfProgressBar(isVisible: Boolean) {
        if (isVisible) {
            binding.apply {
                uiLayout.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = true
            }
        } else {
            binding.apply {
                uiLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                progressBar.isIndeterminate = false
            }
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
                        imageData.let {
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
                    PERMISSION_REQUEST,
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(requireContext().getString(com.natiqhaciyef.common.R.string.give_permission)) {
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

    private fun continueButtonClickEvent() {
        binding.apply {
            viewModel.postEvent(
                CompleteProfileContract.CompleteUiEvent.CollectUserData(
                    DefaultImplModels.mappedUserModel.copy(
                        name = completeProfileFullNameInput.getInputResult(),
                        phoneNumber = completeProfilePhoneNumberInput.getInputResult(),
                        gender = genderSelection,
                        birthDate = completeProfileDOBInput.text.toString(),
                        imageUrl = imageData.toString()
                    )
                )
            )
        }
    }

    private fun continueButtonClickAction(userModel: MappedUserModel?) {
        binding.continueButton.setOnClickListener {
            userModel?.let {
                val action =
                    CompleteProfileFragmentDirections
                        .actionCompleteProfileFragmentToCreateAccountFragment(userModel)
                navigate(action)
            }
        }
    }

    private fun genderDropDownConfig() {
        val genderAdapter =
            ArrayAdapter(requireContext(), com.natiqhaciyef.uikit.R.layout.drop_down_gender_item, genderList)

        binding.apply {
            completeProfileGenderDropDownItem.hint =
                getString(com.natiqhaciyef.common.R.string.gender)
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
        val format = FORMATTED_DATE
        val sdf = SimpleDateFormat(format, Locale.UK)
        val date = sdf.format(calendar.time)
        if (calendar.time.time < System.currentTimeMillis())
            binding.completeProfileDOBInput.text = date
        else
            binding.completeProfileDOBInput.text = DATE_OVER_FLOW_ERROR
    }

    private fun fullNameValidation() {
        binding.apply {
            completeProfileFullNameInput.listenUserInput { text, start, before, count ->
                continueButton.isEnabled = checkFullNameAcceptanceCondition(text)
                        && checkPhoneAcceptanceCondition(completeProfilePhoneNumberInput.getInputResult())
                        && checkGenderAcceptanceCondition(completeProfileGenderDropDownItem.text)
                        && checkDateAcceptanceCondition()
            }
        }
    }

    private fun phoneValidation() {
        binding.apply {
            completeProfilePhoneNumberInput.setMaxLength(THIRTEEN)
            completeProfilePhoneNumberInput.listenUserInputWithAddTextWatcher(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    continueButton.isEnabled = checkPhoneAcceptanceCondition(text)
                            && checkFullNameAcceptanceCondition(completeProfileFullNameInput.getInputResult())
                            && checkGenderAcceptanceCondition(completeProfileGenderDropDownItem.text)
                            && checkDateAcceptanceCondition()
                }

                override fun afterTextChanged(text: Editable?) {
                    text?.let {
                        val formattedNumber =
                            formatPhoneNumber(completeProfilePhoneNumberInput.getEditableText().toString())
                        if (formattedNumber != it.toString()) {
                            completeProfilePhoneNumberInput.listenUserInputWithRemoveTextWatcher(this)

                            // Prevent IndexOutOfBoundsException
                            completeProfilePhoneNumberInput.insertInput(formattedNumber)
                            completeProfilePhoneNumberInput.parseSelection(formattedNumber.length)

                            completeProfilePhoneNumberInput.listenUserInputWithAddTextWatcher(this)
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
                    checkPhoneAcceptanceCondition(completeProfilePhoneNumberInput.getInputResult())
                            && checkFullNameAcceptanceCondition(completeProfileFullNameInput.getInputResult())
                            && checkGenderAcceptanceCondition(genderSelection)
                            && checkDateAcceptanceCondition()
            }
        }
    }

    private fun checkDateAcceptanceCondition() =
        currentSelectedTime < Calendar.getInstance().timeInMillis

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}