package com.udacity.project4.locationreminders.savereminder

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.PointOfInterest
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSaveReminderBinding
import com.udacity.project4.databinding.FragmentSaveReminderBindingImpl
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.geofence.GeofenceBroadcastReceiver
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.utils.GeofencingConstants
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

const val GEOFENCE_RADIUS_IN_METERS = 100f
const val ACTION_GEOFENCE_EVENT =
    "SaveReminderFragment.Remainder.action.ACTION_GEOFENCE_EVENT"

private const val TAG = "GeofenceTransitionsJobIntentService"
class SaveReminderFragment : BaseFragment() {
    //Get the view model this time as a single to be shared with the another fragment
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSaveReminderBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_save_reminder, container, false)

        setDisplayHomeAsUpEnabled(true)

        binding.viewModel = _viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.selectLocation.setOnClickListener {
            //            Navigate to another fragment to get the user location
            _viewModel.navigationCommand.value =
                NavigationCommand.To(SaveReminderFragmentDirections.toSelectLocation())
        }

        binding.saveReminder.setOnClickListener {
            val title = _viewModel.reminderTitle.value
            val description = _viewModel.reminderDescription.value
            val location = _viewModel.reminderSelectedLocationStr.value
            val selectedPOI = _viewModel.selectedPOI.value
            val latitude = _viewModel.latitude.value
            val longitude = _viewModel.longitude.value

            val reminderData =  ReminderDataItem(title, description, location, latitude, longitude)
            if(_viewModel.validateEnteredData(reminderData)){
                addGeofenceForClue(latitude, longitude, reminderData)
            }

//            selectedPOI?.let {
//            } ?: run {
//                _viewModel.showSnackBarInt.value = R.string.err_select_location
//
//                _viewModel.showToast.value = getString(R.string.err_select_location)
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        val args = SaveReminderFragmentArgs.fromBundle(requireArguments())

        args.poi.let {
            _viewModel.reminderSelectedLocationStr.value = it?.name
            _viewModel.latitude.value = it?.latLng?.latitude
            _viewModel.longitude.value = it?.latLng?.longitude
            _viewModel.selectedPOI.value = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //make sure to clear the view model after destroy, as it's a single view model.
        _viewModel.onClear()
    }

    /*
     * Adds a Geofence for the current clue if needed, and removes any existing Geofence. This
     * method should be called after the user has granted the location permission.  If there are
     * no more geofences, we remove the geofence and let the viewmodel know that the ending hint
     * is now "active."
     */
    @SuppressLint("MissingPermission")
    private fun addGeofenceForClue(latitude: Double?, longitude: Double?, reminderData: ReminderDataItem) {
        var remindersActivity = requireActivity() as RemindersActivity

        var geofencingClient = remindersActivity.geofencingClient

        // A PendingIntent for the Broadcast Receiver that handles geofence transitions.
        var geofencePendingIntent = remindersActivity.geofencePendingIntent
//
//        // Build the Geofence Object
        val geofence = Geofence.Builder()
            // Set the request ID, string to identify the geofence.
            .setRequestId(reminderData.id)
            // Set the circular region of this geofence.
            .setCircularRegion(latitude!!, longitude!!, GEOFENCE_RADIUS_IN_METERS)
            // Set the expiration duration of the geofence. This geofence gets
            // automatically removed after this period of time.
            .setExpirationDuration(GeofencingConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            // Set the transition types of interest. Alerts are only generated for these
            // transition. We track entry and exit transitions in this sample.
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        // Build the geofence request
        val geofencingRequest = GeofencingRequest.Builder()
            // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
            // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
            // is already inside that geofence.
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)

            // Add the geofences to be monitored by geofencing service.
            .addGeofence(geofence)
            .build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)?.run {
            addOnSuccessListener {
                // Geofences added.
                // Tell the viewmodel that we've reached the end of the game and
                // activated the last "geofence" --- by removing the Geofence.
//                        viewModel.geofenceActivated()
                binding.viewModel?.validateAndSaveReminder(reminderData)
            }
            addOnFailureListener {
                // Failed to add geofences.
                Toast.makeText(requireContext(), R.string.geofences_not_added,
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
