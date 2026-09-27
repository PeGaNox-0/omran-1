package com.omran.quran.di;

import android.content.Context;
import com.omran.quran.data.local.OmranDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideOmranDatabaseFactory implements Factory<OmranDatabase> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideOmranDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public OmranDatabase get() {
    return provideOmranDatabase(contextProvider.get());
  }

  public static AppModule_ProvideOmranDatabaseFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideOmranDatabaseFactory(contextProvider);
  }

  public static OmranDatabase provideOmranDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideOmranDatabase(context));
  }
}
