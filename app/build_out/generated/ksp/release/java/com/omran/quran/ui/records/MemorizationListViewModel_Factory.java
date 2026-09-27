package com.omran.quran.ui.records;

import com.omran.quran.data.model.AdminSession;
import com.omran.quran.data.repository.AcademicRepository;
import com.omran.quran.data.repository.AuthRepository;
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
public final class MemorizationListViewModel_Factory implements Factory<MemorizationListViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<AcademicRepository> academicProvider;

  private final Provider<AdminSession> adminSessionProvider;

  public MemorizationListViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<AcademicRepository> academicProvider, Provider<AdminSession> adminSessionProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.academicProvider = academicProvider;
    this.adminSessionProvider = adminSessionProvider;
  }

  @Override
  public MemorizationListViewModel get() {
    return newInstance(authRepositoryProvider.get(), academicProvider.get(), adminSessionProvider.get());
  }

  public static MemorizationListViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<AcademicRepository> academicProvider, Provider<AdminSession> adminSessionProvider) {
    return new MemorizationListViewModel_Factory(authRepositoryProvider, academicProvider, adminSessionProvider);
  }

  public static MemorizationListViewModel newInstance(AuthRepository authRepository,
      AcademicRepository academic, AdminSession adminSession) {
    return new MemorizationListViewModel(authRepository, academic, adminSession);
  }
}
