package com.omran.quran.data.sync;

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
public final class SyncManager_Factory implements Factory<SyncManager> {
  private final Provider<Context> contextProvider;

  private final Provider<OmranDao> daoProvider;

  private final Provider<SupabaseClient> supabaseProvider;

  public SyncManager_Factory(Provider<Context> contextProvider, Provider<OmranDao> daoProvider,
      Provider<SupabaseClient> supabaseProvider) {
    this.contextProvider = contextProvider;
    this.daoProvider = daoProvider;
    this.supabaseProvider = supabaseProvider;
  }

  @Override
  public SyncManager get() {
    return newInstance(contextProvider.get(), daoProvider.get(), supabaseProvider.get());
  }

  public static SyncManager_Factory create(Provider<Context> contextProvider,
      Provider<OmranDao> daoProvider, Provider<SupabaseClient> supabaseProvider) {
    return new SyncManager_Factory(contextProvider, daoProvider, supabaseProvider);
  }

  public static SyncManager newInstance(Context context, OmranDao dao, SupabaseClient supabase) {
    return new SyncManager(context, dao, supabase);
  }
}
