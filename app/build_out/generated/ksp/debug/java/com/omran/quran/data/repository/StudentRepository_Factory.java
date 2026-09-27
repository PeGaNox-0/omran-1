package com.omran.quran.data.repository;

import android.content.Context;
import com.omran.quran.data.local.dao.OmranDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class StudentRepository_Factory implements Factory<StudentRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<OmranDao> daoProvider;

  private final Provider<Context> contextProvider;

  public StudentRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<OmranDao> daoProvider, Provider<Context> contextProvider) {
    this.supabaseProvider = supabaseProvider;
    this.daoProvider = daoProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public StudentRepository get() {
    return newInstance(supabaseProvider.get(), daoProvider.get(), contextProvider.get());
  }

  public static StudentRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<OmranDao> daoProvider, Provider<Context> contextProvider) {
    return new StudentRepository_Factory(supabaseProvider, daoProvider, contextProvider);
  }

  public static StudentRepository newInstance(SupabaseClient supabase, OmranDao dao,
      Context context) {
    return new StudentRepository(supabase, dao, context);
  }
}
