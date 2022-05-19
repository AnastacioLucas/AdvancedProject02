package com.udacity.project4

import android.app.Application
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.local.LocalDB
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.EspressoIdlingResource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.test.espresso.action.Press

import androidx.test.espresso.action.CoordinatesProvider

import androidx.test.espresso.action.Tap

import androidx.test.espresso.action.GeneralClickAction

import androidx.test.espresso.ViewAction
import android.util.DisplayMetrics
import android.util.Log
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import com.udacity.project4.util.*
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsNot
import kotlin.math.roundToInt


@RunWith(AndroidJUnit4::class)
@LargeTest
@ExperimentalCoroutinesApi
//END TO END test to black box test the app
class RemindersActivityTest :
    AutoCloseKoinTest() {// Extended Koin Test - embed autoclose @after method to close Koin after every test

    private lateinit var repository: ReminderDataSource
    private lateinit var appContext: Application

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get() as ReminderDataSource
                )
            }
            // RemindersLocalRepository
            single<RemindersLocalRepository> {RemindersLocalRepository(get())}
            // ReminderDataSource
            single<ReminderDataSource> {get<RemindersLocalRepository>()}
            single { LocalDB.createRemindersDao(appContext) }
        }
        //declare a new koin module
        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        repository = get()

        //clear the data to start fresh
        runBlocking {
            repository.deleteAllReminders()
        }
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun clickNewReminder_navigateToSaveRemindersTestSnackBar() = runBlocking {
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the edit task button
        onView(withId(R.id.addReminderFAB)).perform(click())
        onView(withId(R.id.reminderTitle)).check(matches(isDisplayed()))

        onView(withId(R.id.saveReminder)).perform(click())

        // Is toast displayed and is the message correct
        onView(withText(R.string.err_enter_title)).check(matches(isDisplayed()))

        val reminderData =  ReminderDTO(
            "Buy Food",
            "remember of buy vegetable ",
            "Supermarket BRs",
            7895.15416546,
            -1253.15416456
        )

        repository.saveReminder(reminderData)
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(withId(R.id.title)).check(matches(withText(reminderData.title)))

        // When using ActivityScenario.launch, always call close()
        activityScenario.close()
    }

    @Test
    fun clickNewReminder_navigateToSaveRemindersTestToast() = runBlocking {
        val activityScenario = ActivityScenario.launch(RemindersActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the edit task button
        onView(withId(R.id.addReminderFAB)).perform(click())

        val reminder =  ReminderDataItem(
            "Buy Food",
            "remember of buy vegetable",
            "Supermarket BRs",
            7895.15416546,
            -1253.15416456
        )

        val displayMetrics: DisplayMetrics = appContext.resources.displayMetrics

        val height = displayMetrics.heightPixels/2
        val width = displayMetrics.widthPixels/2

        onView(withId(R.id.selectLocation)).perform(click())

        onView(withId(R.id.cl_select_location_map_area)).perform(actionLongClick(width,height))
        onView(withId(R.id.cl_select_location_map_area)).perform(actionClick(width,height))
        Thread.sleep(700)

        val littleHigh = (height*0.75).roundToInt()
        onView(withId(R.id.cl_select_location_map_area)).perform(actionClick(width, littleHigh))

        onView(withId(R.id.reminderTitle))
            .perform(replaceText("Buy Food"))
        onView(withId(R.id.reminderDescription))
            .perform(replaceText("remember of buy vegetable"))

        onView(withId(R.id.saveReminder)).perform(click())

        activityScenario.onActivity {
            //Test_01
//            onView(withText(R.string.err_select_location))
//                .inRoot(withDecorView(
//                    not(activityVar?.window?.decorView)))
//                        .check(matches(isDisplayed()));

            //Test_02
            onView(withText(R.string.reminder_saved)).inRoot(withDecorView(
                IsNot.not(
                    CoreMatchers.`is`(
                        it?.window?.decorView
                    )
                )
            )).check(matches(isDisplayed()))
        }

        //Test_03
//        onView(withText(R.string.reminder_saved)).inRoot(ToastMatcher()).check(matches(isDisplayed()))

        activityScenario.close()
    }
}
