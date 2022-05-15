package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.data.dto.Result.Success
import com.udacity.project4.locationreminders.data.dto.Result.Error
import java.util.LinkedHashMap

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource : ReminderDataSource {

    var tasksServiceData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return Success(tasksServiceData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        tasksServiceData[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        tasksServiceData[id]?.let {
            return Success(it)
        }
        return Error("Could not find task")
    }

    override suspend fun deleteAllReminders() {
        tasksServiceData.clear()
    }
}