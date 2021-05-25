package com.trollingcont.shifttest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.trollingcont.shifttest.R
import com.trollingcont.shifttest.databinding.FragmentRegisterBinding
import com.trollingcont.shifttest.usermanager.AppUserManager
import com.trollingcont.shifttest.usermanager.UserEntity

class RegisterFragment(private val appUserManager: AppUserManager) : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private var year: Int = -1
    private var monthOfYear: Int = -1
    private var dayOfMonth: Int = -1

    companion object {
        enum class UserInputErrors {
            IS_CORRECT,
            DATE_NOT_SET,
            NAME_TOO_SHORT,
            SURNAME_TOO_SHORT,
            PASSWORD_TOO_SHORT,
            PASSWORDS_NOT_EQUAL
        }

        fun newInstance(userManager: AppUserManager) = RegisterFragment(userManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.datePicker.setOnDateChangedListener {
                _, year, monthOfYear, dayOfMonth -> onDateSelected(year, monthOfYear, dayOfMonth)
        }

        binding.registerButton.setOnClickListener { onClickRegisterButton() }

        return binding.root
    }

    private fun onDateSelected(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        this.year = year
        this.monthOfYear = monthOfYear
        this.dayOfMonth = dayOfMonth
    }

    private fun onClickRegisterButton() {
        val errorStringId: Int? = when (validateInput()) {
            UserInputErrors.DATE_NOT_SET -> R.string.date_not_set
            UserInputErrors.NAME_TOO_SHORT -> R.string.name_too_short
            UserInputErrors.SURNAME_TOO_SHORT -> R.string.surname_too_short
            UserInputErrors.PASSWORD_TOO_SHORT -> R.string.password_too_short
            UserInputErrors.PASSWORDS_NOT_EQUAL -> R.string.passwords_not_equal
            UserInputErrors.IS_CORRECT -> null
        }

        if (errorStringId == null) {
            val userEntity = UserEntity(
                binding.nameEdit.text.toString(),
                binding.surnameEdit.text.toString(),
                dayOfMonth,
                monthOfYear,
                year
            )

            appUserManager.setUser(userEntity, binding.passwordEdit.text.toString())

            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, GreetingFragment(userEntity))
                .commit()
        }
        else
            Toast.makeText(requireContext(), errorStringId, Toast.LENGTH_SHORT).show()
    }

    private fun validateInput(): UserInputErrors {
        if (year == -1)
            return UserInputErrors.DATE_NOT_SET

        if (binding.nameEdit.text.length < 2)
            return UserInputErrors.NAME_TOO_SHORT

        if (binding.surnameEdit.text.length < 2)
            return UserInputErrors.SURNAME_TOO_SHORT

        if (binding.passwordEdit.text.length < 6)
            return UserInputErrors.PASSWORD_TOO_SHORT

        if (binding.passwordEdit.text.toString() != binding.passwordRepeatEdit.text.toString())
            return  UserInputErrors.PASSWORDS_NOT_EQUAL

        return UserInputErrors.IS_CORRECT
    }
}