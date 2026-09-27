package com.omran.quran.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    @SerialName("id") val studentId: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("age") val age: Int = 0,
    @SerialName("parent_phone") val parentPhone: String = "",
    @SerialName("teacher_id") val teacherId: String = "",
    @SerialName("teacher_name") val teacherName: String = "",
    @SerialName("circle_name") val circleName: String = "",
    @SerialName("institute_id") val instituteId: String = "",
    @SerialName("photo_url") val photoUrl: String = "",
    @SerialName("join_date") val joinDate: Long? = null,
    @SerialName("gender") val gender: String = "MALE", // "MALE" (بنين) / "FEMALE" (بنات)
) {
    val isFemale: Boolean get() = gender.equals("FEMALE", ignoreCase = true)
    val isMale: Boolean get() = !isFemale
    val genderDisplay: String get() = if (isFemale) "بنات" else "بنين"
}
