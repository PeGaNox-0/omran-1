package com.omran.quran.ui.admin;

import android.content.Context;
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
public final class AdminViewModel_Factory implements Factory<AdminViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StudentRepository> studentRepositoryProvider;

  private final Provider<AcademicRepository> academicRepositoryProvider;

  private final Provider<AttendanceRepository> attendanceRepositoryProvider;

  private final Provider<SyncManager> syncManagerProvider;

  private final Provider<AdminSession> adminSessionProvider;

  private final Provider<Context> contextProvider;

  public AdminViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentRepositoryProvider,
      Provider<AcademicRepository> academicRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<SyncManager> syncManagerProvider, Provider<AdminSession> adminSessionProvider,
      Provider<Context> contextProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.studentRepositoryProvider = studentRepositoryProvider;
    this.academicRepositoryProvider = academicRepositoryProvider;
    this.attendanceRepositoryProvider = attendanceRepositoryProvider;
    this.syncManagerProvider = syncManagerProvider;
    this.adminSessionProvider = adminSessionProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public AdminViewModel get() {
    return newInstance(authRepositoryProvider.get(), studentRepositoryProvider.get(), academicRepositoryProvider.get(), attendanceRepositoryProvider.get(), syncManagerProvider.get(), adminSessionProvider.get(), contextProvider.get());
  }

  public static AdminViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentRepositoryProvider,
      Provider<AcademicRepository> academicRepositoryProvider,
      Provider<AttendanceRepository> attendanceRepositoryProvider,
      Provider<SyncManager> syncManagerProvider, Provider<AdminSession> adminSessionProvider,
      Provider<Context> contextProvider) {
    return new AdminViewModel_Factory(authRepositoryProvider, studentRepositoryProvider, academicRepositoryProvider, attendanceRepositoryProvider, syncManagerProvider, adminSessionProvider, contextProvider);
  }

  public static AdminViewModel newInstance(AuthRepository authRepository,
      StudentRepository studentRepository, AcademicRepository academicRepository,
      AttendanceRepository attendanceRepository, SyncManager syncManager, AdminSession adminSession,
      Context context) {
    return new AdminViewModel(authRepository, studentRepository, academicRepository, attendanceRepository, syncManager, adminSession, context);
  }
}
