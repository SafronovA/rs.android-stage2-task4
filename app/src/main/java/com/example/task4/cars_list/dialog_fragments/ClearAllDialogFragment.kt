package com.example.task4.cars_list.dialog_fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.task4.R
import com.example.task4.cars_list.CarListViewModel

class ClearAllDialogFragment(private val viewModel: CarListViewModel) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(getString(R.string.clear_all_dialog_message))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.clearDataBase()
                }
                .setNegativeButton(getString(R.string.no)) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}