package com.omran.quran.ui.students;

import androidx.lifecycle.SavedStateHandle;
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
public final class StudentEditorViewModel_Factory implements Factory<StudentEditorViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StudentRepository> studentsProvider;

  private final Provider<AuthRepository> authProvider;

  private final Provider<AdminSession> adminSessionProvider;

  public StudentEditorViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<StudentRepository> studentsProvider,
      Provider<AuthRepository> authProvider, Provider<AdminSession> adminSessionProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.studentsProvider = studentsProvider;
    this.authProvider = authProvider;
    this.adminSessionProvider = adminSessionProvider;
  }

  @Override
  public StudentEditorViewModel get() {
    return newInstance(savedStateHandleProvider.get(), authRepositoryProvider.get(), studentsProvider.get(), authProvider.get(), adminSessionProvider.get());
  }

  public static StudentEditorViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AuthRepository> authRepositoryProvider, Provider<StudentRepository> studentsProvider,
      Provider<AuthRepository> authProvider, Provider<AdminSession> adminSessionProvider) {
    return new StudentEditorViewModel_Factory(savedStateHandleProvider, authRepositoryProvider, studentsProvider, authProvider, adminSessionProvider);
  }

  public static StudentEditorViewModel newInstance(SavedStateHandle savedStateHandle,
      AuthRepository authRepository, StudentRepository students, AuthRepository auth,
      AdminSession adminSession) {
    return new StudentEditorViewModel(savedStateHandle, authRepository, students, auth, adminSession);
  }
}
