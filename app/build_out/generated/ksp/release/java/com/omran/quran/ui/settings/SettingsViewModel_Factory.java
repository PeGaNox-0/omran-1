package com.omran.quran.ui.settings;

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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<AuthRepository> authRepositoryProvider;

  private final Provider<SettingsRepository> settingsProvider;

  public SettingsViewModel_Factory(Provider<AuthRepository> authRepositoryProvider,
      Provider<SettingsRepository> settingsProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(authRepositoryProvider.get(), settingsProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<AuthRepository> authRepositoryProvider,
      Provider<SettingsRepository> settingsProvider) {
    return new SettingsViewModel_Factory(authRepositoryProvider, settingsProvider);
  }

  public static SettingsViewModel newInstance(AuthRepository authRepository,
      SettingsRepository settings) {
    return new SettingsViewModel(authRepository, settings);
  }
}
