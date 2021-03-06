package ch.bbbaden.choreapp.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ch.bbbaden.choreapp.R

class ConfirmationDialogFragment(private val title: String) : DialogFragment() {

    private var positiveButtonCallback: (() -> Unit)? = null
    private var negativeButtonCallback: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(title)
                .setPositiveButton(R.string.yes
                ) { _, _ ->
                    positiveButtonCallback?.invoke()
                }
                .setNegativeButton(
                    R.string.fui_cancel
                ) { dialog, _ ->
                    negativeButtonCallback?.invoke()
                    dialog.cancel()
                }
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun setPositiveButtonListener(callback: () -> Unit) : ConfirmationDialogFragment {
        positiveButtonCallback = callback
        return this
    }

    fun setNegativeButtonListener(callback: () -> Unit) : ConfirmationDialogFragment {
        negativeButtonCallback = callback
        return this
    }
}