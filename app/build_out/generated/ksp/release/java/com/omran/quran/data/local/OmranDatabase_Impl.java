package com.omran.quran.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.omran.quran.data.local.dao.OmranDao;
import com.omran.quran.data.local.dao.OmranDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OmranDatabase_Impl extends OmranDatabase {
  private volatile OmranDao _omranDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `students` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `age` INTEGER NOT NULL, `parentPhone` TEXT NOT NULL, `teacherId` TEXT NOT NULL, `teacherName` TEXT NOT NULL, `circleName` TEXT NOT NULL, `instituteId` TEXT NOT NULL, `photoUrl` TEXT NOT NULL, `joinDate` INTEGER, `gender` TEXT NOT NULL, `syncStatus` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `memorizations` (`id` TEXT NOT NULL, `studentId` TEXT NOT NULL, `studentName` TEXT NOT NULL, `teacherId` TEXT NOT NULL, `instituteId` TEXT NOT NULL, `surah` TEXT NOT NULL, `fromVerse` INTEGER NOT NULL, `toVerse` INTEGER NOT NULL, `amount` TEXT NOT NULL, `rating` TEXT NOT NULL, `category` TEXT NOT NULL, `note` TEXT NOT NULL, `date` INTEGER NOT NULL, `syncStatus` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `exams` (`id` TEXT NOT NULL, `studentId` TEXT NOT NULL, `studentName` TEXT NOT NULL, `teacherId` TEXT NOT NULL, `instituteId` TEXT NOT NULL, `surah` TEXT NOT NULL, `score` INTEGER NOT NULL, `mistakes` TEXT NOT NULL, `mastery` TEXT NOT NULL, `note` TEXT NOT NULL, `date` INTEGER NOT NULL, `syncStatus` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `revisions` (`id` TEXT NOT NULL, `studentId` TEXT NOT NULL, `studentName` TEXT NOT NULL, `teacherId` TEXT NOT NULL, `instituteId` TEXT NOT NULL, `content` TEXT NOT NULL, `level` TEXT NOT NULL, `rating` TEXT NOT NULL, `category` TEXT NOT NULL, `note` TEXT NOT NULL, `date` INTEGER NOT NULL, `syncStatus` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `attendance` (`id` TEXT NOT NULL, `studentId` TEXT NOT NULL, `studentName` TEXT NOT NULL, `teacherId` TEXT NOT NULL, `instituteId` TEXT NOT NULL, `status` TEXT NOT NULL, `date` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `syncStatus` TEXT NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'fb42fc1504eb4f97363f235c77318028')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `students`");
        db.execSQL("DROP TABLE IF EXISTS `memorizations`");
        db.execSQL("DROP TABLE IF EXISTS `exams`");
        db.execSQL("DROP TABLE IF EXISTS `revisions`");
        db.execSQL("DROP TABLE IF EXISTS `attendance`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsStudents = new HashMap<String, TableInfo.Column>(13);
        _columnsStudents.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("parentPhone", new TableInfo.Column("parentPhone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("teacherId", new TableInfo.Column("teacherId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("teacherName", new TableInfo.Column("teacherName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("circleName", new TableInfo.Column("circleName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("instituteId", new TableInfo.Column("instituteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("photoUrl", new TableInfo.Column("photoUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("joinDate", new TableInfo.Column("joinDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("gender", new TableInfo.Column("gender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudents.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudents = new TableInfo("students", _columnsStudents, _foreignKeysStudents, _indicesStudents);
        final TableInfo _existingStudents = TableInfo.read(db, "students");
        if (!_infoStudents.equals(_existingStudents)) {
          return new RoomOpenHelper.ValidationResult(false, "students(com.omran.quran.data.local.entity.StudentEntity).\n"
                  + " Expected:\n" + _infoStudents + "\n"
                  + " Found:\n" + _existingStudents);
        }
        final HashMap<String, TableInfo.Column> _columnsMemorizations = new HashMap<String, TableInfo.Column>(15);
        _columnsMemorizations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("studentId", new TableInfo.Column("studentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("studentName", new TableInfo.Column("studentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("teacherId", new TableInfo.Column("teacherId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("instituteId", new TableInfo.Column("instituteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("surah", new TableInfo.Column("surah", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("fromVerse", new TableInfo.Column("fromVerse", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("toVerse", new TableInfo.Column("toVerse", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("amount", new TableInfo.Column("amount", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("rating", new TableInfo.Column("rating", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMemorizations.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMemorizations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMemorizations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMemorizations = new TableInfo("memorizations", _columnsMemorizations, _foreignKeysMemorizations, _indicesMemorizations);
        final TableInfo _existingMemorizations = TableInfo.read(db, "memorizations");
        if (!_infoMemorizations.equals(_existingMemorizations)) {
          return new RoomOpenHelper.ValidationResult(false, "memorizations(com.omran.quran.data.local.entity.MemorizationEntity).\n"
                  + " Expected:\n" + _infoMemorizations + "\n"
                  + " Found:\n" + _existingMemorizations);
        }
        final HashMap<String, TableInfo.Column> _columnsExams = new HashMap<String, TableInfo.Column>(13);
        _columnsExams.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("studentId", new TableInfo.Column("studentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("studentName", new TableInfo.Column("studentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("teacherId", new TableInfo.Column("teacherId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("instituteId", new TableInfo.Column("instituteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("surah", new TableInfo.Column("surah", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("score", new TableInfo.Column("score", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("mistakes", new TableInfo.Column("mistakes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("mastery", new TableInfo.Column("mastery", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsExams.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysExams = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesExams = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoExams = new TableInfo("exams", _columnsExams, _foreignKeysExams, _indicesExams);
        final TableInfo _existingExams = TableInfo.read(db, "exams");
        if (!_infoExams.equals(_existingExams)) {
          return new RoomOpenHelper.ValidationResult(false, "exams(com.omran.quran.data.local.entity.ExamEntity).\n"
                  + " Expected:\n" + _infoExams + "\n"
                  + " Found:\n" + _existingExams);
        }
        final HashMap<String, TableInfo.Column> _columnsRevisions = new HashMap<String, TableInfo.Column>(13);
        _columnsRevisions.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("studentId", new TableInfo.Column("studentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("studentName", new TableInfo.Column("studentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("teacherId", new TableInfo.Column("teacherId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("instituteId", new TableInfo.Column("instituteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("level", new TableInfo.Column("level", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("rating", new TableInfo.Column("rating", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("category", new TableInfo.Column("category", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRevisions.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRevisions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRevisions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRevisions = new TableInfo("revisions", _columnsRevisions, _foreignKeysRevisions, _indicesRevisions);
        final TableInfo _existingRevisions = TableInfo.read(db, "revisions");
        if (!_infoRevisions.equals(_existingRevisions)) {
          return new RoomOpenHelper.ValidationResult(false, "revisions(com.omran.quran.data.local.entity.RevisionEntity).\n"
                  + " Expected:\n" + _infoRevisions + "\n"
                  + " Found:\n" + _existingRevisions);
        }
        final HashMap<String, TableInfo.Column> _columnsAttendance = new HashMap<String, TableInfo.Column>(10);
        _columnsAttendance.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("studentId", new TableInfo.Column("studentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("studentName", new TableInfo.Column("studentName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("teacherId", new TableInfo.Column("teacherId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("instituteId", new TableInfo.Column("instituteId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("date", new TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("syncStatus", new TableInfo.Column("syncStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAttendance.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAttendance = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAttendance = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAttendance = new TableInfo("attendance", _columnsAttendance, _foreignKeysAttendance, _indicesAttendance);
        final TableInfo _existingAttendance = TableInfo.read(db, "attendance");
        if (!_infoAttendance.equals(_existingAttendance)) {
          return new RoomOpenHelper.ValidationResult(false, "attendance(com.omran.quran.data.local.entity.AttendanceEntity).\n"
                  + " Expected:\n" + _infoAttendance + "\n"
                  + " Found:\n" + _existingAttendance);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "fb42fc1504eb4f97363f235c77318028", "932af7920fa49714b50e5935c9a8e6af");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "students","memorizations","exams","revisions","attendance");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `students`");
      _db.execSQL("DELETE FROM `memorizations`");
      _db.execSQL("DELETE FROM `exams`");
      _db.execSQL("DELETE FROM `revisions`");
      _db.execSQL("DELETE FROM `attendance`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(OmranDao.class, OmranDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public OmranDao omranDao() {
    if (_omranDao != null) {
      return _omranDao;
    } else {
      synchronized(this) {
        if(_omranDao == null) {
          _omranDao = new OmranDao_Impl(this);
        }
        return _omranDao;
      }
    }
  }
}
