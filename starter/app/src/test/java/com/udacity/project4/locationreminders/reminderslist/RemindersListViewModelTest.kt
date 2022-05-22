package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.MyApp
import com.udacity.project4.R
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.resumeDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest : AutoCloseKoinTest() {

    private lateinit var tasksRepository: FakeDataSource

    // Subject under test
    private lateinit var viewModel: RemindersListViewModel

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

        viewModel = RemindersListViewModel(context, tasksRepository)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun saveReminder_setsValidateInfo() = runBlocking {
        // Given a fresh ViewModel
        val reminder =  ReminderDTO(
            "Buy Food",
            "remember of buy vegetable ",
            "Supermarket BRs",
            7895.15416546,
            -1253.15416456
        )

        tasksRepository.saveReminder(reminder)
        viewModel.loadReminders()

        val reminderList = viewModel.remindersList.getOrAwaitValue()

        reminderList.let {
            if(reminderList.isNotEmpty()){
                assertThat(reminderList[0].id, CoreMatchers.`is`(reminder.id))
            }
        }
    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay() {
        // Make the repository return errors
        tasksRepository.setReturnError(true)

        // When
        viewModel.loadReminders()

        // Then an error message is shown
        assertThat(
            viewModel.showSnackBar.getOrAwaitValue(),
            CoreMatchers.`is`("Test exception")
        )
    }

    @Test
    fun loadTasks_loading(){
        // When
        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()

        // Then
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }
}