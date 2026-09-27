package com.omran.quran.data.model

data class DashboardStats(
    val studentCount: Int = 0,
    val presentToday: Int = 0,
    val absentToday: Int = 0,
    val lateToday: Int = 0,
    val memorizationToday: Int = 0,
    val examsToday: Int = 0,
    val revisionsToday: Int = 0,
)
