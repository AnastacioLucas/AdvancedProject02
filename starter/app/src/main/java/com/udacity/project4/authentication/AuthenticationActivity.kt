package com.udacity.project4.authentication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.project4.R

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
class AuthenticationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
//         TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google

//          TODO: If the user was authenticated, send him to RemindersActivity

//          TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

    }

//    private fun launchSignInFlow() {
//        // Give users the option to sign in / register with their email
//        // If users choose to register with their email,
//        // they will need to create a password as well
//        val providers = arrayListOf(
//            AuthUI.IdpConfig.EmailBuilder().build()
//            //
//        )
//
//        // Create and launch sign-in intent.
//        // We listen to the response of this activity with the
//        // SIGN_IN_RESULT_CODE code
//        startActivityForResult(
//            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(
//                providers
//            ).build(), MainFragment.SIGN_IN_RESULT_CODE
//        )
//    }
}