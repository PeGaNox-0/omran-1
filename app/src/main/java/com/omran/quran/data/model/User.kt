package com.omran.quran.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class UserRole {
    ADMIN,
    TEACHER,
    ;

    companion object {
        fun from(value: String?): UserRole =
            entries.find { it.name.equals(value, ignoreCase = true) } ?: TEACHER
    }
}

sealed interface Session {
    data object Loading : Session
    data object SignedOut : Session
    data class SignedIn(val user: AppUser) : Session
}

@Serializable
data class AppUser(
    @SerialName("id") val userId: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("role") val role: String = UserRole.TEACHER.name,
    @SerialName("institute_id") val instituteId: String = "",
    @SerialName("institute_name") val instituteName: String = "",
    @SerialName("circle_name") val circleName: String = "",
    @SerialName("circle_gender") val circleGender: String = "MALE", // "MALE" (بنين/ذكور) / "FEMALE" (بنات/إناث)
    @SerialName("photo_url") val photoUrl: String = "",
    @SerialName("fcm_token") val fcmToken: String = "",
    @SerialName("created_at") val createdAt: Long = 0L,
) {
    val userRole: UserRole get() = UserRole.from(role)
    val isAdmin: Boolean get() = userRole == UserRole.ADMIN
    val isFemaleCircle: Boolean get() = circleGender.equals("FEMALE", ignoreCase = true)
    val isMaleCircle: Boolean get() = !isFemaleCircle
    val displayCircleName: String get() = circleName.ifBlank { if (name.isNotBlank()) "حلقة $name" else "الحلقة القرآنية" }
    val genderBadge: String get() = if (isFemaleCircle) "حلقة بنات" else "حلقة بنين"
}

@Serializable
data class Institute(
    @SerialName("id") val instituteId: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("short_code") val shortCode: String = "",
    @SerialName("created_at") val createdAt: Long = 0L,
)
