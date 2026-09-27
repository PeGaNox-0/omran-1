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
public final class RevisionListViewModel_Factory implements Factory<RevisionListViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<AcademicRepository> academicProvider;

  private final Provider<AdminSession> adminSessionProvider;

  public RevisionListViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<AcademicRepository> academicProvider, Provider<AdminSession> adminSessionProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.academicProvider = academicProvider;
    this.adminSessionProvider = adminSessionProvider;
  }

  @Override
  public RevisionListViewModel get() {
    return newInstance(authRepositoryProvider.get(), academicProvider.get(), adminSessionProvider.get());
  }

  public static RevisionListViewModel_Factory create(
      Provider<AuthRepository> authRepositoryProvider,
      Provider<AcademicRepository> academicProvider, Provider<AdminSession> adminSessionProvider) {
    return new RevisionListViewModel_Factory(authRepositoryProvider, academicProvider, adminSessionProvider);
  }

  public static RevisionListViewModel newInstance(AuthRepository authRepository,
      AcademicRepository academic, AdminSession adminSession) {
    return new RevisionListViewModel(authRepository, academic, adminSession);
  }
}
