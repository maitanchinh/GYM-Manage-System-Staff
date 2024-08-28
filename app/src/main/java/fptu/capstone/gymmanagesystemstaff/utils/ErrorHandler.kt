package fptu.capstone.gymmanagesystemstaff.utils

fun ErrorHandler(errorCode: Int): String {
    return when (errorCode) {
        401 -> Message.UNAUTHORIZED.message
        408 -> Message.REQUEST_TIMEOUT.message
        409 -> Message.CONFLICT.message
        else -> Message.FETCH_DATA_FAILURE.message
    }
}