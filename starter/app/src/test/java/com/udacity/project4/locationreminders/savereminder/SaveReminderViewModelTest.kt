package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.MyApp
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.*
import org.junit.*
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    private lateinit var tasksRepository: FakeDataSource

    // Subject under test
    private lateinit var viewModel: SaveReminderViewModel

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        var context = ApplicationProvider.getApplicationContext<MyApp>()

        //We initialise the tasks to 3, with one active and two completed
        tasksRepository = FakeDataSource()

        viewModel = SaveReminderViewModel(context, tasksRepository)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun saveReminder_setsValidateInfo() = runBlocking {
        // Given a fresh ViewModel

        val reminderData =  ReminderDataItem(
            "Buy Food",
            "remember of buy vegetable ",
            "Supermarket BRs",
            7895.15416546,
            -1253.15416456
        )

        viewModel.validateAndSaveReminder(reminderData)

        when (val result = tasksRepository.getReminders()) {
            is Result.Success<*> -> {
                val listOfReminder = result.data as List<ReminderDTO>

                Assert.assertEquals(listOfReminder.size, 1)
            }
        }
    }

    @Test
    fun saveReminder_errorTitleMissing() {
        // Given a fresh ViewModel
        val reminderData =  ReminderDataItem(
            "",
            "remember of buy vegetable ",
            "Supermarket BRs",
            7895.15416546,
            -1253.15416456
        )

        viewModel.validateAndSaveReminder(reminderData)

//    // Assert that the snackbar has been updated with the correct text.
        val snackbarText: Int =  viewModel.showSnackBarInt.getOrAwaitValue()
        assertThat(snackbarText, `is`(R.string.err_enter_title))
    }
}