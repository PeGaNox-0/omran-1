package com.omran.quran.data.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * حالة المدير المشتركة بين جميع الـ ViewModels.
 * عندما يختار المدير معلماً، يُخزَّن هنا ويُطبَّق على كل الشاشات.
 */
@Singleton
class AdminSession @Inject constructor() {
    private val _selectedTeacher = MutableStateFlow<AppUser?>(null)
    val selectedTeacher = _selectedTeacher.asStateFlow()

    fun selectTeacher(teacher: AppUser?) {
        _selectedTeacher.value = teacher
    }

    /** معرف المعلم المختار — null يعني "الكل" (للمدير بلا فلتر) */
    val selectedTeacherId: String?
        get() = _selectedTeacher.value?.userId

    /** اسم الحلقة المختارة */
    val selectedCircleName: String?
        get() = _selectedTeacher.value?.displayCircleName

    fun clear() {
        _selectedTeacher.value = null
    }
}
