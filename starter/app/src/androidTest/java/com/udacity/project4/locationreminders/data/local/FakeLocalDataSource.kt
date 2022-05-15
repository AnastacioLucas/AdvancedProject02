package com.udacity.project4.locationreminders.data.local

import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.util.LinkedHashMap

class FakeLocalDataSource : ReminderDataSource {

    var tasksServiceData: LinkedHashMap<String, ReminderDTO> = LinkedHashMap()

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        tasksServiceData[reminder.id] = reminder
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        tasksServiceData[id]?.let {
            return Result.Success(it)
        }
        return Result.Error("Could not find task")
    }

    override suspend fun deleteAllReminders() {
        tasksServiceData.clear()
    }
}