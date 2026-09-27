package com.omran.quran.ui;

import com.omran.quran.data.repository.AuthRepository;
import com.omran.quran.data.repository.SettingsRepository;
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
public final class AppViewModel_Factory implements Factory<AppViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SettingsRepository> settingsRepositoryProvider;

  public AppViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.settingsRepositoryProvider = settingsRepositoryProvider;
  }

  @Override
  public AppViewModel get() {
    return newInstance(authRepositoryProvider.get(), settingsRepositoryProvider.get());
  }

  public static AppViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<SettingsRepository> settingsRepositoryProvider) {
    return new AppViewModel_Factory(authRepositoryProvider, settingsRepositoryProvider);
  }

  public static AppViewModel newInstance(AuthRepository authRepository,
      SettingsRepository settingsRepository) {
    return new AppViewModel(authRepository, settingsRepository);
  }
}
