package com.omran.quran.di;

import com.omran.quran.data.local.OmranDatabase;
import com.omran.quran.data.local.dao.OmranDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideOmranDaoFactory implements Factory<OmranDao> {
  private final Provider<OmranDatabase> databaseProvider;

  public AppModule_ProvideOmranDaoFactory(Provider<OmranDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public OmranDao get() {
    return provideOmranDao(databaseProvider.get());
  }

  public static AppModule_ProvideOmranDaoFactory create(Provider<OmranDatabase> databaseProvider) {
    return new AppModule_ProvideOmranDaoFactory(databaseProvider);
  }

  public static OmranDao provideOmranDao(OmranDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideOmranDao(database));
  }
}
