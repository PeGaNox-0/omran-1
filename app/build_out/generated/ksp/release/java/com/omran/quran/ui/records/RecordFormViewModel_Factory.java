package com.omran.quran.ui.records;

import androidx.lifecycle.SavedStateHandle;
import com.omran.quran.data.repository.AcademicRepository;
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
public final class RecordFormViewModel_Factory implements Factory<RecordFormViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<StudentRepository> studentsRepoProvider;

  private final Provider<AcademicRepository> academicProvider;

  public RecordFormViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentsRepoProvider,
      Provider<AcademicRepository> academicProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.authRepositoryProvider = authRepositoryProvider;
    this.studentsRepoProvider = studentsRepoProvider;
    this.academicProvider = academicProvider;
  }

  @Override
  public RecordFormViewModel get() {
    return newInstance(savedStateHandleProvider.get(), authRepositoryProvider.get(), studentsRepoProvider.get(), academicProvider.get());
  }

  public static RecordFormViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AuthRepository> authRepositoryProvider,
      Provider<StudentRepository> studentsRepoProvider,
      Provider<AcademicRepository> academicProvider) {
    return new RecordFormViewModel_Factory(savedStateHandleProvider, authRepositoryProvider, studentsRepoProvider, academicProvider);
  }

  public static RecordFormViewModel newInstance(SavedStateHandle savedStateHandle,
      AuthRepository authRepository, StudentRepository studentsRepo, AcademicRepository academic) {
    return new RecordFormViewModel(savedStateHandle, authRepository, studentsRepo, academic);
  }
}
