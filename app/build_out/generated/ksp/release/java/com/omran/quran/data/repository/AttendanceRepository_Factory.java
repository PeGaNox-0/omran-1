package com.omran.quran.data.repository;

import com.omran.quran.data.local.dao.OmranDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class AttendanceRepository_Factory implements Factory<AttendanceRepository> {
  private final Provider<SupabaseClient> supabaseProvider;

  private final Provider<OmranDao> daoProvider;

  public AttendanceRepository_Factory(Provider<SupabaseClient> supabaseProvider,
      Provider<OmranDao> daoProvider) {
    this.supabaseProvider = supabaseProvider;
    this.daoProvider = daoProvider;
  }

  @Override
  public AttendanceRepository get() {
    return newInstance(supabaseProvider.get(), daoProvider.get());
  }

  public static AttendanceRepository_Factory create(Provider<SupabaseClient> supabaseProvider,
      Provider<OmranDao> daoProvider) {
    return new AttendanceRepository_Factory(supabaseProvider, daoProvider);
  }

  public static AttendanceRepository newInstance(SupabaseClient supabase, OmranDao dao) {
    return new AttendanceRepository(supabase, dao);
  }
}
