package com.omran.quran.ui.attendance;

import com.omran.quran.data.model.AdminSession;
import com.omran.quran.data.repository.AcademicRepository;
import com.omran.quran.data.repository.AttendanceRepository;
import com.omran.quran.data.repository.AuthRepository;
import com.omran.quran.data.repository.SettingsRepository;
import com.omran.quran.data.repository.StudentRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
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
public final class AttendanceViewModel_Factory implements Factory<AttendanceViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StudentRepository> studentsProvider;

  private final Provider<AttendanceRepository> attendanceProvider;

  private final Provider<AcademicRepository> academicProvider;

  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<AdminSession> adminSessionProvider;

  public AttendanceViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentsProvider,
      Provider<AttendanceRepository> attendanceProvider,
      Provider<AcademicRepository> academicProvider, Provider<SettingsRepository> settingsProvider,
      Provider<AdminSession> adminSessionProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.studentsProvider = studentsProvider;
    this.attendanceProvider = attendanceProvider;
    this.academicProvider = academicProvider;
    this.settingsProvider = settingsProvider;
    this.adminSessionProvider = adminSessionProvider;
  }

  @Override
  public AttendanceViewModel get() {
    return newInstance(authRepositoryProvider.get(), studentsProvider.get(), attendanceProvider.get(), academicProvider.get(), settingsProvider.get(), adminSessionProvider.get());
  }

  public static AttendanceViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentsProvider,
      Provider<AttendanceRepository> attendanceProvider,
      Provider<AcademicRepository> academicProvider, Provider<SettingsRepository> settingsProvider,
      Provider<AdminSession> adminSessionProvider) {
    return new AttendanceViewModel_Factory(authRepositoryProvider, studentsProvider, attendanceProvider, academicProvider, settingsProvider, adminSessionProvider);
  }

  public static AttendanceViewModel newInstance(AuthRepository authRepository,
      StudentRepository students, AttendanceRepository attendance, AcademicRepository academic,
      SettingsRepository settings, AdminSession adminSession) {
    return new AttendanceViewModel(authRepository, students, attendance, academic, settings, adminSession);
  }
}
