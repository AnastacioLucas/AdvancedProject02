package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {


    @Test
    fun clickNewReminder_navigateToSaveRemindersWithFragment() = runBlockingTest {
//        val reminderData =  ReminderDTO(
//            "Buy Food",
//            "remember of buy vegetable ",
//            "Supermarket BRs",
//            7895.15416546,
//            -1253.15416456
//        )

        // GIVEN - On the home screen
        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)

        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        Thread.sleep(2000)

        // WHEN - Click on the first list item
        onView(withId(R.id.saveReminder)).perform(click())

        Thread.sleep(2000)

//        // THEN - Verify that we navigate to the first detail screen
//        Mockito.verify(navController).navigate(
//            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment( "id1")
//        )


//        val reminder =  ReminderDataItem(
//            "The package",
//            "Get the package at the delivery company",
//            "Supermarket BRs",
//            7895.15416546,
//            -1253.15416456
//        )
//
//        viewModel.validateAndSaveReminder(reminder)
//
////    // Assert that the snackbar has been updated with the correct text.
//        val snackbarText: Int =  viewModel.showSnackBarInt.getOrAwaitValue()
//        MatcherAssert.assertThat(snackbarText, CoreMatchers.`is`(R.string.err_select_location))
    }
}