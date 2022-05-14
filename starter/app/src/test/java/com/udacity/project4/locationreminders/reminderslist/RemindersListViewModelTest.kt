package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.koinApplication

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

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
        //We initialise the tasks to 3, with one active and two completed
        tasksRepository = FakeDataSource()
//        val task1 = Task("Title1", "Description1")
//        val task2 = Task("Title2", "Description2", true)
//        val task3 = Task("Title3", "Description3", true)
//        tasksRepository.addTasks(task1, task2, task3)
//
        viewModel = SaveReminderViewModel(tasksRepository)
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
//        // Given a fresh ViewModel
//
//        // When adding a new task
//        viewModel.addNewTask()
//
//        // Then the new task event is triggered
//        val value = viewModel.newTaskEvent.getOrAwaitValue()
//        MatcherAssert.assertThat(
//            value.getContentIfNotHandled(), (CoreMatchers.not(CoreMatchers.nullValue()))
//        )
    }
}