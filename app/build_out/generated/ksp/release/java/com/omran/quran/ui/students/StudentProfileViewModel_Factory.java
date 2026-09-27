package com.omran.quran.ui.students;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.omran.quran.data.repository.AcademicRepository;
import com.omran.quran.data.repository.AttendanceRepository;
import com.omran.quran.data.repository.AuthRepository;
import com.omran.quran.data.repository.StudentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class StudentProfileViewModel_Factory implements Factory<StudentProfileViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<StudentRepository> studentsProvider;

  private final Provider<AcademicRepository> academicProvider;

  private final Provider<AttendanceRepository> attendanceRepoProvider;

  private final Provider<AuthRepository> authProvider;

  private final Provider<Context> contextProvider;

  public StudentProfileViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StudentRepository> studentsProvider, Provider<AcademicRepository> academicProvider,
      Provider<AttendanceRepository> attendanceRepoProvider, Provider<AuthRepository> authProvider,
      Provider<Context> contextProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.studentsProvider = studentsProvider;
    this.academicProvider = academicProvider;
    this.attendanceRepoProvider = attendanceRepoProvider;
    this.authProvider = authProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public StudentProfileViewModel get() {
    return newInstance(savedStateHandleProvider.get(), studentsProvider.get(), academicProvider.get(), attendanceRepoProvider.get(), authProvider.get(), contextProvider.get());
  }

  public static StudentProfileViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<StudentRepository> studentsProvider, Provider<AcademicRepository> academicProvider,
      Provider<AttendanceRepository> attendanceRepoProvider, Provider<AuthRepository> authProvider,
      Provider<Context> contextProvider) {
    return new StudentProfileViewModel_Factory(savedStateHandleProvider, studentsProvider, academicProvider, attendanceRepoProvider, authProvider, contextProvider);
  }

  public static StudentProfileViewModel newInstance(SavedStateHandle savedStateHandle,
      StudentRepository students, AcademicRepository academic, AttendanceRepository attendanceRepo,
      AuthRepository auth, Context context) {
    return new StudentProfileViewModel(savedStateHandle, students, academic, attendanceRepo, auth, context);
  }
}
