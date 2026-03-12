package com.example.eventmos

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerEventType: Spinner
    private lateinit var etEventDate: Button
    private lateinit var rgGender: RadioGroup
    private lateinit var ivProfile: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var cbTerms: CheckBox
    private lateinit var btnSubmit: Button

    private var selectedImageUri: Uri? = null
    private var selectedDate: String = ""

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                ivProfile.setImageURI(it)
                ivProfile.visibility = View.VISIBLE
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        supportActionBar?.apply {
            title = "Event Registration"
            setDisplayHomeAsUpEnabled(true)
        }

        bindViews()
        setupSpinner()
        setupDatePicker()

        btnUploadImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        btnSubmit.setOnClickListener {
            if (validateForm()) {
                showConfirmationDialog()
            }
        }
    }

    private fun bindViews() {
        etFullName       = findViewById(R.id.etFullName)
        etPhone          = findViewById(R.id.etPhone)
        etEmail          = findViewById(R.id.etEmail)
        spinnerEventType = findViewById(R.id.spinnerEventType)
        etEventDate      = findViewById(R.id.etEventDate)
        rgGender         = findViewById(R.id.rgGender)
        ivProfile        = findViewById(R.id.ivProfile)
        btnUploadImage   = findViewById(R.id.btnUploadImage)
        cbTerms          = findViewById(R.id.cbTerms)
        btnSubmit        = findViewById(R.id.btnSubmit)
    }

    private fun setupSpinner() {
        val eventTypes = arrayOf(
            "Select Event Type", "Seminar", "Workshop",
            "Conference", "Webinar", "Cultural Event"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventType.adapter = adapter
    }

    private fun setupDatePicker() {
        etEventDate.setOnClickListener {
            openDatePicker()
        }
    }

    private fun openDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            R.style.DatePickerTheme,
            { _, year, month, day ->
                val picked = Calendar.getInstance().apply { set(year, month, day) }
                if (picked.before(Calendar.getInstance())) {
                    Toast.makeText(this, "Please select a future date", Toast.LENGTH_SHORT).show()
                } else {
                    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    selectedDate = sdf.format(picked.time)
                    etEventDate.setText(selectedDate)
                }
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.minDate = System.currentTimeMillis()
            show()
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        val name = etFullName.text.toString().trim()
        if (name.isEmpty()) {
            etFullName.error = "Full name is required"
            isValid = false
        } else if (name.length < 3) {
            etFullName.error = "Name must be at least 3 characters"
            isValid = false
        } else {
            etFullName.error = null
        }

        val phone = etPhone.text.toString().trim()
        if (phone.isEmpty()) {
            etPhone.error = "Phone number is required"
            isValid = false
        } else if (!phone.matches(Regex("^[+0-9\\-\\s]{10,15}$"))) {
            etPhone.error = "Enter a valid phone number (10-15 digits)"
            isValid = false
        } else {
            etPhone.error = null
        }

        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            etEmail.error = "Email address is required"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Enter a valid email address"
            isValid = false
        } else {
            etEmail.error = null
        }

        if (spinnerEventType.selectedItemPosition == 0) {
            Toast.makeText(this, "Please select an event type", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (selectedDate.isEmpty()) {
            etEventDate.error = "Please select an event date"
            isValid = false
        } else {
            etEventDate.error = null
        }

        if (rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!cbTerms.isChecked) {
            Toast.makeText(this, "Please accept the Terms and Conditions", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this, R.style.AlertDialogTheme)
            .setTitle("Confirm Registration")
            .setMessage("Are you sure you want to submit your registration for ${spinnerEventType.selectedItem}?")
            .setPositiveButton("Confirm") { _, _ -> navigateToConfirmation() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun navigateToConfirmation() {
        val genderId = rgGender.checkedRadioButtonId
        val gender = findViewById<RadioButton>(genderId)?.text?.toString() ?: ""

        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra("fullName",  etFullName.text.toString().trim())
            putExtra("phone",     etPhone.text.toString().trim())
            putExtra("email",     etEmail.text.toString().trim())
            putExtra("eventType", spinnerEventType.selectedItem.toString())
            putExtra("eventDate", selectedDate)
            putExtra("gender",    gender)
            putExtra("imageUri",  selectedImageUri?.toString() ?: "")
        }
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}