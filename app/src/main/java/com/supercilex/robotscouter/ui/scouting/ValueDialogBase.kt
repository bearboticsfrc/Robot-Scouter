package com.supercilex.robotscouter.ui.scouting

import android.content.DialogInterface
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText
import com.supercilex.robotscouter.R
import com.supercilex.robotscouter.util.ui.KeyboardDialogBase
import kotterknife.bindView

abstract class ValueDialogBase<out T> : KeyboardDialogBase() {
    protected val inputLayout: TextInputLayout by bindView(R.id.value_layout)
    override val lastEditText: EditText by bindView(R.id.value)

    protected abstract val value: T?
    @get:StringRes protected abstract val title: Int
    @get:StringRes protected abstract val hint: Int

    override fun onCreateDialog(savedInstanceState: Bundle?) = createDialog(
            View.inflate(context, R.layout.dialog_value, null),
            title,
            savedInstanceState
    )

    override fun onShow(dialog: DialogInterface, savedInstanceState: Bundle?) {
        super.onShow(dialog, savedInstanceState)
        inputLayout.hint = getString(hint)
        lastEditText.apply {
            setText(arguments!!.getString(CURRENT_VALUE))
            if (savedInstanceState == null) post { selectAll() }
        }
    }

    protected companion object {
        const val CURRENT_VALUE = "current_value"
    }
}
