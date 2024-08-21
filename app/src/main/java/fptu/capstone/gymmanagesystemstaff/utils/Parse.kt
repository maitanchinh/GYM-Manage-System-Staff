package fptu.capstone.gymmanagesystemstaff.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseDateTime(dateTime: String): LocalDateTime {
    return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}