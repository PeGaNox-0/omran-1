package com.omran.quran.ui.students;

import com.omran.quran.data.model.AdminSession;
import com.omran.quran.data.repository.AuthRepository;
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
public final class StudentsViewModel_Factory implements Factory<StudentsViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StudentRepository> repositoryProvider;

  private final Provider<AdminSession> adminSessionProvider;

  public StudentsViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> repositoryProvider, Provider<AdminSession> adminSessionProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.repositoryProvider = repositoryProvider;
    this.adminSessionProvider = adminSessionProvider;
  }

  @Override
  public StudentsViewModel get() {
    return newInstance(authRepositoryProvider.get(), repositoryProvider.get(), adminSessionProvider.get());
  }

  public static StudentsViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> repositoryProvider, Provider<AdminSession> adminSessionProvider) {
    return new StudentsViewModel_Factory(authRepositoryProvider, repositoryProvider, adminSessionProvider);
  }

  public static StudentsViewModel newInstance(AuthRepository authRepository,
      StudentRepository repository, AdminSession adminSession) {
    return new StudentsViewModel(authRepository, repository, adminSession);
  }
}
