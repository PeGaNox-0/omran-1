package com.omran.quran.data.model;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AdminSession_Factory implements Factory<AdminSession> {
  @Override
  public AdminSession get() {
    return newInstance();
  }

  public static AdminSession_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AdminSession newInstance() {
    return new AdminSession();
  }

  private static final class InstanceHolder {
    private static final AdminSession_Factory INSTANCE = new AdminSession_Factory();
  }
}
