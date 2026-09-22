package com.example.vade.database

import androidx.room.TypeConverter
import com.example.vade.tasks.data.Priority
import com.example.vade.tasks.data.RepeatRule
import com.example.vade.habits.data.HabitScheduleType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Converters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.format(DateTimeFormatter.ISO_LOCAL_DATE)

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? = time?.format(DateTimeFormatter.ISO_LOCAL_TIME)

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? = timeString?.let { LocalTime.parse(it, DateTimeFormatter.ISO_LOCAL_TIME) }
    
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? = dateTimeString?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }

    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(name: String): Priority = Priority.valueOf(name)
    
    @TypeConverter
    fun fromRepeatRule(rule: RepeatRule): String = rule.name

    @TypeConverter
    fun toRepeatRule(name: String): RepeatRule = RepeatRule.valueOf(name)
    
    @TypeConverter
    fun fromHabitScheduleType(type: HabitScheduleType): String = type.name

    @TypeConverter
    fun toHabitScheduleType(name: String): HabitScheduleType = HabitScheduleType.valueOf(name)
}
