package com.souvikmondal01.neutral.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.TimeZone

object Common {
    fun isDarkMode(context: Context): Boolean {
        val darkModeFlag =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    fun View.visible() {
        visibility = View.VISIBLE
    }

    fun View.gone() {
        visibility = View.GONE
    }

    fun View.invisible() {
        visibility = View.INVISIBLE
    }

    fun Fragment.toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    fun EditText.clearEdittext(view: View) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                view.isVisible = this@clearEdittext.text.toString().isNotEmpty()
            }

            override fun afterTextChanged(p0: Editable?) {
                view.setOnClickListener {
                    this@clearEdittext.text.clear()
                }
            }
        })
    }

    fun Fragment.hideKeyboard() {
        val inputMethodManager =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun Fragment.showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = requireActivity().getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun Fragment.setStatusBarColor(color: Int) {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), color)
    }

    fun shareArticle(activity: Activity, link: String, title: String = "Share link") {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            putExtra(Intent.EXTRA_SUBJECT, title)
        }

        // Create chooser with a readable title
        val chooser = Intent.createChooser(sendIntent, title)

        // Ensure there's at least one app to handle this intent
        if (sendIntent.resolveActivity(activity.packageManager) != null) {
            activity.startActivity(chooser)
        } else {
            // Optionally show a toast or fallback
            Toast.makeText(activity, "No app available to share", Toast.LENGTH_SHORT).show()
        }
    }


    fun logD(msg: Any) {
        Log.d("ssss", msg.toString())
    }

    @SuppressLint("SimpleDateFormat")
    fun String.convertUtcToIndianTime(): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a")
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val formattedDate = inputFormat.parse(this)
        return formattedDate?.let { outputFormat.format(it) }
    }

}

