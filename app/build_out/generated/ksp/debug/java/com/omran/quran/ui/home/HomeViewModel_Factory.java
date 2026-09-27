package com.omran.quran.ui.home;

import com.omran.quran.data.model.AdminSession;
import com.omran.quran.data.repository.AcademicRepository;
import com.omran.quran.data.repository.AttendanceRepository;
import com.omran.quran.data.repository.AuthRepository;
import com.omran.quran.data.repository.StudentRepository;
import com.omran.quran.data.sync.SyncManager;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StudentRepository> studentsProvider;

  private final Provider<AcademicRepository> academicProvider;

  private final Provider<AttendanceRepository> attendanceProvider;

  private final Provider<SyncManager> syncManagerProvider;

  private final Provider<AdminSession> adminSessionProvider;

  public HomeViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentsProvider, Provider<AcademicRepository> academicProvider,
      Provider<AttendanceRepository> attendanceProvider, Provider<SyncManager> syncManagerProvider,
      Provider<AdminSession> adminSessionProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.studentsProvider = studentsProvider;
    this.academicProvider = academicProvider;
    this.attendanceProvider = attendanceProvider;
    this.syncManagerProvider = syncManagerProvider;
    this.adminSessionProvider = adminSessionProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(authRepositoryProvider.get(), studentsProvider.get(), academicProvider.get(), attendanceProvider.get(), syncManagerProvider.get(), adminSessionProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentsProvider, Provider<AcademicRepository> academicProvider,
      Provider<AttendanceRepository> attendanceProvider, Provider<SyncManager> syncManagerProvider,
      Provider<AdminSession> adminSessionProvider) {
    return new HomeViewModel_Factory(authRepositoryProvider, studentsProvider, academicProvider, attendanceProvider, syncManagerProvider, adminSessionProvider);
  }

  public static HomeViewModel newInstance(AuthRepository authRepository, StudentRepository students,
      AcademicRepository academic, AttendanceRepository attendance, SyncManager syncManager,
      AdminSession adminSession) {
    return new HomeViewModel(authRepository, students, academic, attendance, syncManager, adminSession);
  }
}
