package com.peakDevCol.gympartner.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.peakDevCol.gympartner.R
import com.peakDevCol.gympartner.databinding.FragmentProfileBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState:Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            datePickerButton.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.birth_day_picker))
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()
                datePicker.show(parentFragmentManager, "datePicker")
                datePicker.addOnPositiveButtonClickListener {
                    val selectDate = datePicker.selection
                    val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectDate)
                    // Example: Display the formatted date in a TextView
                    datePickerButton.text ="Naciste el : $formattedDate"
                }
            }
        }
    }

}