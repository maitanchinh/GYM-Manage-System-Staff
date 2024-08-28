package fptu.capstone.gymmanagesystemstaff.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

fun parseDateTime(dateTime: String): LocalDateTime {
    return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}

fun formatDateTime(input: String, outputFormat: String): String {
    // Định dạng của chuỗi đầu vào
    val inputFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
        .optionalEnd()
        .toFormatter()
    // Định dạng mong muốn
    val outputFormatter = DateTimeFormatter.ofPattern(outputFormat)

    // Phân tích chuỗi đầu vào thành đối tượng LocalDateTime
    val dateTime = LocalDateTime.parse(input, inputFormatter)

    // Định dạng lại đối tượng LocalDateTime thành chuỗi đầu ra mong muốn
    return dateTime.format(outputFormatter)
}