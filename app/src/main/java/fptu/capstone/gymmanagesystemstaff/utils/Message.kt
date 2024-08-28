package fptu.capstone.gymmanagesystemstaff.utils

enum class Message(val message: String) {
    ERROR_NETWORK("Network error, Please check your connection"),
    FETCH_DATA_FAILURE("Failed to fetch data, please try again"),
    PAYMENT_PENDING("Payment is pending, please wait for a moment"),
    EMAIL_EXISTED("Email is already existed"),
    INVALID_EMAIL("Invalid email"),
    UNAUTHORIZED("Unauthorized, please login again"),
    CONFLICT("This data is already existed, please try again"),
    REQUEST_TIMEOUT("Request timeout, please try again"),
}