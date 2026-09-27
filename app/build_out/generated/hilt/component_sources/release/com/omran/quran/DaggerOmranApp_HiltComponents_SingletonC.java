package com.omran.quran;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.omran.quran.data.local.OmranDatabase;
import com.omran.quran.data.local.dao.OmranDao;
import com.omran.quran.data.model.AdminSession;
import com.omran.quran.data.repository.AcademicRepository;
import com.omran.quran.data.repository.AttendanceRepository;
import com.omran.quran.data.repository.AuthRepository;
import com.omran.quran.data.repository.SettingsRepository;
import com.omran.quran.data.repository.StudentRepository;
import com.omran.quran.data.sync.SyncManager;
import com.omran.quran.di.AppModule_ProvideDataStoreFactory;
import com.omran.quran.di.AppModule_ProvideOmranDaoFactory;
import com.omran.quran.di.AppModule_ProvideOmranDatabaseFactory;
import com.omran.quran.di.AppModule_ProvideSupabaseClientFactory;
import com.omran.quran.ui.AppViewModel;
import com.omran.quran.ui.AppViewModel_HiltModules;
import com.omran.quran.ui.admin.AdminViewModel;
import com.omran.quran.ui.admin.AdminViewModel_HiltModules;
import com.omran.quran.ui.attendance.AttendanceViewModel;
import com.omran.quran.ui.attendance.AttendanceViewModel_HiltModules;
import com.omran.quran.ui.auth.AuthViewModel;
import com.omran.quran.ui.auth.AuthViewModel_HiltModules;
import com.omran.quran.ui.home.HomeViewModel;
import com.omran.quran.ui.home.HomeViewModel_HiltModules;
import com.omran.quran.ui.records.ExamListViewModel;
import com.omran.quran.ui.records.ExamListViewModel_HiltModules;
import com.omran.quran.ui.records.MemorizationListViewModel;
import com.omran.quran.ui.records.MemorizationListViewModel_HiltModules;
import com.omran.quran.ui.records.RecordFormViewModel;
import com.omran.quran.ui.records.RecordFormViewModel_HiltModules;
import com.omran.quran.ui.records.RevisionListViewModel;
import com.omran.quran.ui.records.RevisionListViewModel_HiltModules;
import com.omran.quran.ui.settings.SettingsViewModel;
import com.omran.quran.ui.settings.SettingsViewModel_HiltModules;
import com.omran.quran.ui.students.StudentEditorViewModel;
import com.omran.quran.ui.students.StudentEditorViewModel_HiltModules;
import com.omran.quran.ui.students.StudentProfileViewModel;
import com.omran.quran.ui.students.StudentProfileViewModel_HiltModules;
import com.omran.quran.ui.students.StudentsViewModel;
import com.omran.quran.ui.students.StudentsViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.github.jan.supabase.SupabaseClient;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerOmranApp_HiltComponents_SingletonC {
  private DaggerOmranApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public OmranApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements OmranApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements OmranApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements OmranApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements OmranApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements OmranApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements OmranApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements OmranApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public OmranApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends OmranApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends OmranApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends OmranApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends OmranApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(13).put(LazyClassKeyProvider.com_omran_quran_ui_admin_AdminViewModel, AdminViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_AppViewModel, AppViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_attendance_AttendanceViewModel, AttendanceViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_auth_AuthViewModel, AuthViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_records_ExamListViewModel, ExamListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_records_MemorizationListViewModel, MemorizationListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_records_RecordFormViewModel, RecordFormViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_records_RevisionListViewModel, RevisionListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_students_StudentEditorViewModel, StudentEditorViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_students_StudentProfileViewModel, StudentProfileViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_omran_quran_ui_students_StudentsViewModel, StudentsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_omran_quran_ui_settings_SettingsViewModel = "com.omran.quran.ui.settings.SettingsViewModel";

      static String com_omran_quran_ui_home_HomeViewModel = "com.omran.quran.ui.home.HomeViewModel";

      static String com_omran_quran_ui_records_RecordFormViewModel = "com.omran.quran.ui.records.RecordFormViewModel";

      static String com_omran_quran_ui_students_StudentEditorViewModel = "com.omran.quran.ui.students.StudentEditorViewModel";

      static String com_omran_quran_ui_records_RevisionListViewModel = "com.omran.quran.ui.records.RevisionListViewModel";

      static String com_omran_quran_ui_auth_AuthViewModel = "com.omran.quran.ui.auth.AuthViewModel";

      static String com_omran_quran_ui_records_MemorizationListViewModel = "com.omran.quran.ui.records.MemorizationListViewModel";

      static String com_omran_quran_ui_admin_AdminViewModel = "com.omran.quran.ui.admin.AdminViewModel";

      static String com_omran_quran_ui_AppViewModel = "com.omran.quran.ui.AppViewModel";

      static String com_omran_quran_ui_students_StudentsViewModel = "com.omran.quran.ui.students.StudentsViewModel";

      static String com_omran_quran_ui_attendance_AttendanceViewModel = "com.omran.quran.ui.attendance.AttendanceViewModel";

      static String com_omran_quran_ui_records_ExamListViewModel = "com.omran.quran.ui.records.ExamListViewModel";

      static String com_omran_quran_ui_students_StudentProfileViewModel = "com.omran.quran.ui.students.StudentProfileViewModel";

      @KeepFieldType
      SettingsViewModel com_omran_quran_ui_settings_SettingsViewModel2;

      @KeepFieldType
      HomeViewModel com_omran_quran_ui_home_HomeViewModel2;

      @KeepFieldType
      RecordFormViewModel com_omran_quran_ui_records_RecordFormViewModel2;

      @KeepFieldType
      StudentEditorViewModel com_omran_quran_ui_students_StudentEditorViewModel2;

      @KeepFieldType
      RevisionListViewModel com_omran_quran_ui_records_RevisionListViewModel2;

      @KeepFieldType
      AuthViewModel com_omran_quran_ui_auth_AuthViewModel2;

      @KeepFieldType
      MemorizationListViewModel com_omran_quran_ui_records_MemorizationListViewModel2;

      @KeepFieldType
      AdminViewModel com_omran_quran_ui_admin_AdminViewModel2;

      @KeepFieldType
      AppViewModel com_omran_quran_ui_AppViewModel2;

      @KeepFieldType
      StudentsViewModel com_omran_quran_ui_students_StudentsViewModel2;

      @KeepFieldType
      AttendanceViewModel com_omran_quran_ui_attendance_AttendanceViewModel2;

      @KeepFieldType
      ExamListViewModel com_omran_quran_ui_records_ExamListViewModel2;

      @KeepFieldType
      StudentProfileViewModel com_omran_quran_ui_students_StudentProfileViewModel2;
    }
  }

  private static final class ViewModelCImpl extends OmranApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AdminViewModel> adminViewModelProvider;

    private Provider<AppViewModel> appViewModelProvider;

    private Provider<AttendanceViewModel> attendanceViewModelProvider;

    private Provider<AuthViewModel> authViewModelProvider;

    private Provider<ExamListViewModel> examListViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<MemorizationListViewModel> memorizationListViewModelProvider;

    private Provider<RecordFormViewModel> recordFormViewModelProvider;

    private Provider<RevisionListViewModel> revisionListViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private Provider<StudentEditorViewModel> studentEditorViewModelProvider;

    private Provider<StudentProfileViewModel> studentProfileViewModelProvider;

    private Provider<StudentsViewModel> studentsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.adminViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.appViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.attendanceViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.authViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.examListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.memorizationListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.recordFormViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.revisionListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
      this.studentEditorViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 10);
      this.studentProfileViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 11);
      this.studentsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 12);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(13).put(LazyClassKeyProvider.com_omran_quran_ui_admin_AdminViewModel, ((Provider) adminViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_AppViewModel, ((Provider) appViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_attendance_AttendanceViewModel, ((Provider) attendanceViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_auth_AuthViewModel, ((Provider) authViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_records_ExamListViewModel, ((Provider) examListViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_records_MemorizationListViewModel, ((Provider) memorizationListViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_records_RecordFormViewModel, ((Provider) recordFormViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_records_RevisionListViewModel, ((Provider) revisionListViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_students_StudentEditorViewModel, ((Provider) studentEditorViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_students_StudentProfileViewModel, ((Provider) studentProfileViewModelProvider)).put(LazyClassKeyProvider.com_omran_quran_ui_students_StudentsViewModel, ((Provider) studentsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_omran_quran_ui_students_StudentProfileViewModel = "com.omran.quran.ui.students.StudentProfileViewModel";

      static String com_omran_quran_ui_home_HomeViewModel = "com.omran.quran.ui.home.HomeViewModel";

      static String com_omran_quran_ui_admin_AdminViewModel = "com.omran.quran.ui.admin.AdminViewModel";

      static String com_omran_quran_ui_attendance_AttendanceViewModel = "com.omran.quran.ui.attendance.AttendanceViewModel";

      static String com_omran_quran_ui_records_RecordFormViewModel = "com.omran.quran.ui.records.RecordFormViewModel";

      static String com_omran_quran_ui_records_MemorizationListViewModel = "com.omran.quran.ui.records.MemorizationListViewModel";

      static String com_omran_quran_ui_AppViewModel = "com.omran.quran.ui.AppViewModel";

      static String com_omran_quran_ui_records_ExamListViewModel = "com.omran.quran.ui.records.ExamListViewModel";

      static String com_omran_quran_ui_students_StudentEditorViewModel = "com.omran.quran.ui.students.StudentEditorViewModel";

      static String com_omran_quran_ui_students_StudentsViewModel = "com.omran.quran.ui.students.StudentsViewModel";

      static String com_omran_quran_ui_records_RevisionListViewModel = "com.omran.quran.ui.records.RevisionListViewModel";

      static String com_omran_quran_ui_auth_AuthViewModel = "com.omran.quran.ui.auth.AuthViewModel";

      static String com_omran_quran_ui_settings_SettingsViewModel = "com.omran.quran.ui.settings.SettingsViewModel";

      @KeepFieldType
      StudentProfileViewModel com_omran_quran_ui_students_StudentProfileViewModel2;

      @KeepFieldType
      HomeViewModel com_omran_quran_ui_home_HomeViewModel2;

      @KeepFieldType
      AdminViewModel com_omran_quran_ui_admin_AdminViewModel2;

      @KeepFieldType
      AttendanceViewModel com_omran_quran_ui_attendance_AttendanceViewModel2;

      @KeepFieldType
      RecordFormViewModel com_omran_quran_ui_records_RecordFormViewModel2;

      @KeepFieldType
      MemorizationListViewModel com_omran_quran_ui_records_MemorizationListViewModel2;

      @KeepFieldType
      AppViewModel com_omran_quran_ui_AppViewModel2;

      @KeepFieldType
      ExamListViewModel com_omran_quran_ui_records_ExamListViewModel2;

      @KeepFieldType
      StudentEditorViewModel com_omran_quran_ui_students_StudentEditorViewModel2;

      @KeepFieldType
      StudentsViewModel com_omran_quran_ui_students_StudentsViewModel2;

      @KeepFieldType
      RevisionListViewModel com_omran_quran_ui_records_RevisionListViewModel2;

      @KeepFieldType
      AuthViewModel com_omran_quran_ui_auth_AuthViewModel2;

      @KeepFieldType
      SettingsViewModel com_omran_quran_ui_settings_SettingsViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.omran.quran.ui.admin.AdminViewModel 
          return (T) new AdminViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.attendanceRepositoryProvider.get(), singletonCImpl.syncManagerProvider.get(), singletonCImpl.adminSessionProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.omran.quran.ui.AppViewModel 
          return (T) new AppViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.settingsRepositoryProvider.get());

          case 2: // com.omran.quran.ui.attendance.AttendanceViewModel 
          return (T) new AttendanceViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.attendanceRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.settingsRepositoryProvider.get(), singletonCImpl.adminSessionProvider.get());

          case 3: // com.omran.quran.ui.auth.AuthViewModel 
          return (T) new AuthViewModel(singletonCImpl.authRepositoryProvider.get());

          case 4: // com.omran.quran.ui.records.ExamListViewModel 
          return (T) new ExamListViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.adminSessionProvider.get());

          case 5: // com.omran.quran.ui.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.attendanceRepositoryProvider.get(), singletonCImpl.syncManagerProvider.get(), singletonCImpl.adminSessionProvider.get());

          case 6: // com.omran.quran.ui.records.MemorizationListViewModel 
          return (T) new MemorizationListViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.adminSessionProvider.get());

          case 7: // com.omran.quran.ui.records.RecordFormViewModel 
          return (T) new RecordFormViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.authRepositoryProvider.get(), singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get());

          case 8: // com.omran.quran.ui.records.RevisionListViewModel 
          return (T) new RevisionListViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.adminSessionProvider.get());

          case 9: // com.omran.quran.ui.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.settingsRepositoryProvider.get());

          case 10: // com.omran.quran.ui.students.StudentEditorViewModel 
          return (T) new StudentEditorViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.authRepositoryProvider.get(), singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), singletonCImpl.adminSessionProvider.get());

          case 11: // com.omran.quran.ui.students.StudentProfileViewModel 
          return (T) new StudentProfileViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.academicRepositoryProvider.get(), singletonCImpl.attendanceRepositoryProvider.get(), singletonCImpl.authRepositoryProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 12: // com.omran.quran.ui.students.StudentsViewModel 
          return (T) new StudentsViewModel(singletonCImpl.authRepositoryProvider.get(), singletonCImpl.studentRepositoryProvider.get(), singletonCImpl.adminSessionProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends OmranApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends OmranApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends OmranApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<SupabaseClient> provideSupabaseClientProvider;

    private Provider<AuthRepository> authRepositoryProvider;

    private Provider<OmranDatabase> provideOmranDatabaseProvider;

    private Provider<OmranDao> provideOmranDaoProvider;

    private Provider<StudentRepository> studentRepositoryProvider;

    private Provider<AcademicRepository> academicRepositoryProvider;

    private Provider<AttendanceRepository> attendanceRepositoryProvider;

    private Provider<SyncManager> syncManagerProvider;

    private Provider<AdminSession> adminSessionProvider;

    private Provider<DataStore<Preferences>> provideDataStoreProvider;

    private Provider<SettingsRepository> settingsRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideSupabaseClientProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseClient>(singletonCImpl, 1));
      this.authRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AuthRepository>(singletonCImpl, 0));
      this.provideOmranDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<OmranDatabase>(singletonCImpl, 4));
      this.provideOmranDaoProvider = DoubleCheck.provider(new SwitchingProvider<OmranDao>(singletonCImpl, 3));
      this.studentRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<StudentRepository>(singletonCImpl, 2));
      this.academicRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AcademicRepository>(singletonCImpl, 5));
      this.attendanceRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AttendanceRepository>(singletonCImpl, 6));
      this.syncManagerProvider = DoubleCheck.provider(new SwitchingProvider<SyncManager>(singletonCImpl, 7));
      this.adminSessionProvider = DoubleCheck.provider(new SwitchingProvider<AdminSession>(singletonCImpl, 8));
      this.provideDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<DataStore<Preferences>>(singletonCImpl, 10));
      this.settingsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SettingsRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectOmranApp(OmranApp omranApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.omran.quran.data.repository.AuthRepository 
          return (T) new AuthRepository(singletonCImpl.provideSupabaseClientProvider.get());

          case 1: // io.github.jan.supabase.SupabaseClient 
          return (T) AppModule_ProvideSupabaseClientFactory.provideSupabaseClient();

          case 2: // com.omran.quran.data.repository.StudentRepository 
          return (T) new StudentRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.provideOmranDaoProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.omran.quran.data.local.dao.OmranDao 
          return (T) AppModule_ProvideOmranDaoFactory.provideOmranDao(singletonCImpl.provideOmranDatabaseProvider.get());

          case 4: // com.omran.quran.data.local.OmranDatabase 
          return (T) AppModule_ProvideOmranDatabaseFactory.provideOmranDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.omran.quran.data.repository.AcademicRepository 
          return (T) new AcademicRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.provideOmranDaoProvider.get());

          case 6: // com.omran.quran.data.repository.AttendanceRepository 
          return (T) new AttendanceRepository(singletonCImpl.provideSupabaseClientProvider.get(), singletonCImpl.provideOmranDaoProvider.get());

          case 7: // com.omran.quran.data.sync.SyncManager 
          return (T) new SyncManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideOmranDaoProvider.get(), singletonCImpl.provideSupabaseClientProvider.get());

          case 8: // com.omran.quran.data.model.AdminSession 
          return (T) new AdminSession();

          case 9: // com.omran.quran.data.repository.SettingsRepository 
          return (T) new SettingsRepository(singletonCImpl.provideDataStoreProvider.get());

          case 10: // androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> 
          return (T) AppModule_ProvideDataStoreFactory.provideDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
