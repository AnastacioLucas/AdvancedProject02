package com.udacity.project4.locationreminders

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.BuildConfig
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver
import com.udacity.project4.locationreminders.reminderslist.*
import com.udacity.project4.locationreminders.savereminder.selectreminderlocation.REQUEST_LOCATION_PERMISSION_SELECT_LOCATION
import com.udacity.project4.utils.sendNotification
import kotlinx.android.synthetic.main.activity_reminders.*
import kotlinx.android.synthetic.main.fragment_select_location.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

/**
 * The RemindersActivity that holds the reminders fragments
 */

const val ACTION_GEOFENCE_EVENT =
    "SaveReminderFragment.Remainder.action.ACTION_GEOFENCE_EVENT"

class RemindersActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

//        val remindersLocalRepository: RemindersLocalRepository by inject()
//        Interaction to the repository has to be through a coroutine scope
//        CoroutineScope(coroutineContext).launch(SupervisorJob()) {
//            //get the reminder with the request id
//            val result = remindersLocalRepository.getReminder("654841c1-b4a5-448c-a2c0-66ff82fdedc3")
//            if (result is Result.Success<ReminderDTO>) {
//                val reminderDTO = result.data
//
//                //send a notification to the user with the reminder details
//                sendNotification(
//                    this@RemindersActivity, ReminderDataItem(
//                        reminderDTO.title,
//                        reminderDTO.description,
//                        reminderDTO.location,
//                        reminderDTO.latitude,
//                        reminderDTO.longitude,
//                        reminderDTO.id
//                    )
//                )
//            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (nav_host_fragment as NavHostFragment).navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}