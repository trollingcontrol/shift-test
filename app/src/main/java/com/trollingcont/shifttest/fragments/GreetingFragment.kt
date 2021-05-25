package com.trollingcont.shifttest.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.trollingcont.shifttest.R
import com.trollingcont.shifttest.databinding.FragmentGreetingBinding
import com.trollingcont.shifttest.usermanager.UserEntity

class GreetingFragment(private val userEntity: UserEntity) : Fragment() {
    lateinit var binding: FragmentGreetingBinding
    lateinit var builder: AlertDialog.Builder

    companion object {
        fun newInstance(userEntity: UserEntity) = GreetingFragment(userEntity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGreetingBinding.inflate(inflater, container, false)

        builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.alert_title)
        builder.setMessage(getString(R.string.alert_message, userEntity.name, userEntity.surname))
        builder.setPositiveButton(R.string.ok_text) { dialog, _ -> dialog.dismiss() }
        builder.create()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.greetingButton.setOnClickListener { builder.show() }
    }
}