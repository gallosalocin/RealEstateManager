package com.openclassrooms.realestatemanager.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.maps.model.LatLng
import com.openclassrooms.realestatemanager.R
import pub.devrel.easypermissions.EasyPermissions
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils {

    /**
     *  Convert dollars to euros
     */
    fun convertDollarToEuro(dollar: Int): Int {
        return (dollar * 0.843).roundToInt()
    }

    /**
     *  Convert euros to dollars
     */
    fun convertEuroToDollar(euro: Int): Int {
        return (euro * 1.186).roundToInt()
    }

    /**
     *  Convert today's date to a more suitable format
     */
    fun getTodayDate(): String {
        val currentDate = Calendar.getInstance()
        return SimpleDateFormat("dd-MM-yyyy", Locale.FRANCE).format(currentDate.time)
    }

    /**
     *  Convert address to LatLng
     */
    fun getLocationFromAddress(context: Context, address: String): LatLng {
        val coder = Geocoder(context)
        val currentAddress = coder.getFromLocationName(address, 1)

        val location = currentAddress[0]
        val latitude = location.latitude
        val longitude = location.longitude

        return LatLng(latitude, longitude)
    }

    /**
     *  Check if GPS enabled
     */
    fun isGPSEnabled(context: Context): Boolean {
        val locationManager = (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     *  Setup Alert Dialog to Activate GPS
     */
    fun setupAlertDialogToActivateGPS(context: Context, intent: Intent, option: Bundle?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialog_title_gps).setMessage(R.string.dialog_message_gps)
                .setPositiveButton(R.string.dialog_positive) { _, _ -> startActivity(context, intent, option) }
                .setNegativeButton(R.string.dialog_negative) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    }

    /**
     *  Setup Alert Dialog to Activate Internet
     */
    fun setupAlertDialogToActivateInternet(context: Context, intent1: Intent, intent2: Intent, option: Bundle?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.dialog_title_internet).setMessage(R.string.dialog_message_internet)
                .setPositiveButton(R.string.dialog_button_wifi) { _, _ -> startActivity(context, intent1, option) }
                .setNegativeButton(R.string.dialog_button_data) { _, _ -> startActivity(context, intent2, option) }
        val alert = builder.create()
        alert.show()
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
    }

    /**
     *  Check internet connection
     */
    fun isInternetConnected(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

    /**
     *  Check if input is not null or empty
     */
    fun validateInputFieldIfNullOrEmpty(field: EditText, errorText: String): Boolean {
        return if (field.text.isNullOrEmpty()) {
            field.error = errorText
            false
        } else {
            field.error = null
            true
        }
    }

    /**
     *  Check if input is greater than the number of chars specified
     */
    fun validateInputFieldIfIsGreaterThan(field: EditText, errorText: String, chars: Int): Boolean {
        return if (field.length() < chars) {
            field.error = errorText
            false
        } else {
            field.error = null
            true
        }
    }

    /**
     *  Number format in dollar
     */
    fun formatInDollar(number: Number, maxDecimal: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        numberFormat.maximumFractionDigits = maxDecimal
        return numberFormat.format(number)
    }

    /**
     *  Number format in euro
     */
    fun formatInEuro(number: Number, maxDecimal: Int): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE)
        numberFormat.maximumFractionDigits = maxDecimal
        return numberFormat.format(number)
    }

    /**
     *  Hide Details Container in Tablet Landscape Mode
     */
    fun hideDetailsContainerTabletLandscape(activity: Activity) {
        val guideline: Guideline? = activity.findViewById(R.id.guideline)
        val scrollView: ScrollView? = activity.findViewById(R.id.sv_right)
        val divider: View? = activity.findViewById(R.id.divider)
        guideline?.setGuidelinePercent(1F)
        scrollView?.visibility = View.GONE
        divider?.visibility = View.GONE
    }

    /**
     *  Show Details Container in Tablet Landscape Mode
     */
    fun showDetailsContainerTabletLandscape(activity: Activity) {
        val guideline: Guideline? = activity.findViewById(R.id.guideline)
        val scrollView: ScrollView? = activity.findViewById(R.id.sv_right)
        val divider: View? = activity.findViewById(R.id.divider)
        guideline?.setGuidelinePercent(0.33F)
        scrollView?.visibility = View.VISIBLE
        divider?.visibility = View.VISIBLE
    }

    /**
     *  Hide Soft Keyboard
     */
    fun hideKeyboard(activity: Activity) {
        val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = activity.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     *  Check if device has location permissions
     */
    fun hasLocationPermissions(context: Context) =
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                EasyPermissions.hasPermissions(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            }
}