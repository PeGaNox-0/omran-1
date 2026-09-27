package com.omran.quran.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.omran.quran.data.local.OmranTypeConverters;
import com.omran.quran.data.local.SyncStatus;
import com.omran.quran.data.local.entity.AttendanceEntity;
import com.omran.quran.data.local.entity.ExamEntity;
import com.omran.quran.data.local.entity.MemorizationEntity;
import com.omran.quran.data.local.entity.RevisionEntity;
import com.omran.quran.data.local.entity.StudentEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class OmranDao_Impl implements OmranDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StudentEntity> __insertionAdapterOfStudentEntity;

  private final OmranTypeConverters __omranTypeConverters = new OmranTypeConverters();

  private final EntityInsertionAdapter<MemorizationEntity> __insertionAdapterOfMemorizationEntity;

  private final EntityInsertionAdapter<ExamEntity> __insertionAdapterOfExamEntity;

  private final EntityInsertionAdapter<RevisionEntity> __insertionAdapterOfRevisionEntity;

  private final EntityInsertionAdapter<AttendanceEntity> __insertionAdapterOfAttendanceEntity;

  private final SharedSQLiteStatement __preparedStmtOfMarkStudentDeleted;

  private final SharedSQLiteStatement __preparedStmtOfDeleteStudentPermanently;

  private final SharedSQLiteStatement __preparedStmtOfMarkMemorizationDeleted;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMemorizationPermanently;

  private final SharedSQLiteStatement __preparedStmtOfMarkExamDeleted;

  private final SharedSQLiteStatement __preparedStmtOfDeleteExamPermanently;

  private final SharedSQLiteStatement __preparedStmtOfMarkRevisionDeleted;

  private final SharedSQLiteStatement __preparedStmtOfDeleteRevisionPermanently;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAttendancePermanently;

  public OmranDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudentEntity = new EntityInsertionAdapter<StudentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `students` (`id`,`name`,`age`,`parentPhone`,`teacherId`,`teacherName`,`circleName`,`instituteId`,`photoUrl`,`joinDate`,`gender`,`syncStatus`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final StudentEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getAge());
        statement.bindString(4, entity.getParentPhone());
        statement.bindString(5, entity.getTeacherId());
        statement.bindString(6, entity.getTeacherName());
        statement.bindString(7, entity.getCircleName());
        statement.bindString(8, entity.getInstituteId());
        statement.bindString(9, entity.getPhotoUrl());
        if (entity.getJoinDate() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getJoinDate());
        }
        statement.bindString(11, entity.getGender());
        final String _tmp = __omranTypeConverters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(12, _tmp);
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfMemorizationEntity = new EntityInsertionAdapter<MemorizationEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `memorizations` (`id`,`studentId`,`studentName`,`teacherId`,`instituteId`,`surah`,`fromVerse`,`toVerse`,`amount`,`rating`,`category`,`note`,`date`,`syncStatus`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MemorizationEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getStudentId());
        statement.bindString(3, entity.getStudentName());
        statement.bindString(4, entity.getTeacherId());
        statement.bindString(5, entity.getInstituteId());
        statement.bindString(6, entity.getSurah());
        statement.bindLong(7, entity.getFromVerse());
        statement.bindLong(8, entity.getToVerse());
        statement.bindString(9, entity.getAmount());
        statement.bindString(10, entity.getRating());
        statement.bindString(11, entity.getCategory());
        statement.bindString(12, entity.getNote());
        statement.bindLong(13, entity.getDate());
        final String _tmp = __omranTypeConverters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(14, _tmp);
        statement.bindLong(15, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfExamEntity = new EntityInsertionAdapter<ExamEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `exams` (`id`,`studentId`,`studentName`,`teacherId`,`instituteId`,`surah`,`score`,`mistakes`,`mastery`,`note`,`date`,`syncStatus`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ExamEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getStudentId());
        statement.bindString(3, entity.getStudentName());
        statement.bindString(4, entity.getTeacherId());
        statement.bindString(5, entity.getInstituteId());
        statement.bindString(6, entity.getSurah());
        statement.bindLong(7, entity.getScore());
        statement.bindString(8, entity.getMistakes());
        statement.bindString(9, entity.getMastery());
        statement.bindString(10, entity.getNote());
        statement.bindLong(11, entity.getDate());
        final String _tmp = __omranTypeConverters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(12, _tmp);
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfRevisionEntity = new EntityInsertionAdapter<RevisionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `revisions` (`id`,`studentId`,`studentName`,`teacherId`,`instituteId`,`content`,`level`,`rating`,`category`,`note`,`date`,`syncStatus`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RevisionEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getStudentId());
        statement.bindString(3, entity.getStudentName());
        statement.bindString(4, entity.getTeacherId());
        statement.bindString(5, entity.getInstituteId());
        statement.bindString(6, entity.getContent());
        statement.bindString(7, entity.getLevel());
        statement.bindString(8, entity.getRating());
        statement.bindString(9, entity.getCategory());
        statement.bindString(10, entity.getNote());
        statement.bindLong(11, entity.getDate());
        final String _tmp = __omranTypeConverters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(12, _tmp);
        statement.bindLong(13, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfAttendanceEntity = new EntityInsertionAdapter<AttendanceEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `attendance` (`id`,`studentId`,`studentName`,`teacherId`,`instituteId`,`status`,`date`,`createdAt`,`syncStatus`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AttendanceEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getStudentId());
        statement.bindString(3, entity.getStudentName());
        statement.bindString(4, entity.getTeacherId());
        statement.bindString(5, entity.getInstituteId());
        statement.bindString(6, entity.getStatus());
        statement.bindString(7, entity.getDate());
        statement.bindLong(8, entity.getCreatedAt());
        final String _tmp = __omranTypeConverters.fromSyncStatus(entity.getSyncStatus());
        statement.bindString(9, _tmp);
        statement.bindLong(10, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfMarkStudentDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE students SET syncStatus = 'PENDING_DELETE' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteStudentPermanently = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM students WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkMemorizationDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE memorizations SET syncStatus = 'PENDING_DELETE' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteMemorizationPermanently = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM memorizations WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkExamDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE exams SET syncStatus = 'PENDING_DELETE' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteExamPermanently = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM exams WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkRevisionDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE revisions SET syncStatus = 'PENDING_DELETE' WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteRevisionPermanently = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM revisions WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAttendancePermanently = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM attendance WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertStudents(final List<StudentEntity> students,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStudentEntity.insert(students);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertStudent(final StudentEntity student,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStudentEntity.insert(student);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertMemorizations(final List<MemorizationEntity> mems,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMemorizationEntity.insert(mems);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertMemorization(final MemorizationEntity mem,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMemorizationEntity.insert(mem);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertExams(final List<ExamEntity> exams,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExamEntity.insert(exams);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertExam(final ExamEntity exam, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfExamEntity.insert(exam);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertRevisions(final List<RevisionEntity> revisions,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRevisionEntity.insert(revisions);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertRevision(final RevisionEntity rev,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRevisionEntity.insert(rev);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAttendances(final List<AttendanceEntity> attendances,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAttendanceEntity.insert(attendances);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertAttendance(final AttendanceEntity attendance,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAttendanceEntity.insert(attendance);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object markStudentDeleted(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkStudentDeleted.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkStudentDeleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteStudentPermanently(final String id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteStudentPermanently.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteStudentPermanently.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markMemorizationDeleted(final String id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkMemorizationDeleted.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkMemorizationDeleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMemorizationPermanently(final String id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMemorizationPermanently.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteMemorizationPermanently.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markExamDeleted(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkExamDeleted.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkExamDeleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteExamPermanently(final String id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteExamPermanently.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteExamPermanently.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markRevisionDeleted(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkRevisionDeleted.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkRevisionDeleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteRevisionPermanently(final String id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteRevisionPermanently.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteRevisionPermanently.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAttendancePermanently(final String id,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAttendancePermanently.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAttendancePermanently.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getStudents(final String instituteId, final String teacherId, final String query,
      final String gender, final Continuation<? super List<StudentEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM students \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR gender = ?)\n"
            + "        AND (? = '' OR name LIKE '%' || ? || '%')\n"
            + "        ORDER BY name ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 10);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 4;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 5;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 6;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 7;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 8;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 9;
    _statement.bindString(_argIndex, query);
    _argIndex = 10;
    _statement.bindString(_argIndex, query);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StudentEntity>>() {
      @Override
      @NonNull
      public List<StudentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfParentPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "parentPhone");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfTeacherName = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherName");
          final int _cursorIndexOfCircleName = CursorUtil.getColumnIndexOrThrow(_cursor, "circleName");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUrl");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<StudentEntity> _result = new ArrayList<StudentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudentEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpParentPhone;
            _tmpParentPhone = _cursor.getString(_cursorIndexOfParentPhone);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpTeacherName;
            _tmpTeacherName = _cursor.getString(_cursorIndexOfTeacherName);
            final String _tmpCircleName;
            _tmpCircleName = _cursor.getString(_cursorIndexOfCircleName);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpPhotoUrl;
            _tmpPhotoUrl = _cursor.getString(_cursorIndexOfPhotoUrl);
            final Long _tmpJoinDate;
            if (_cursor.isNull(_cursorIndexOfJoinDate)) {
              _tmpJoinDate = null;
            } else {
              _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            }
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StudentEntity(_tmpId,_tmpName,_tmpAge,_tmpParentPhone,_tmpTeacherId,_tmpTeacherName,_tmpCircleName,_tmpInstituteId,_tmpPhotoUrl,_tmpJoinDate,_tmpGender,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getStudentById(final String id,
      final Continuation<? super StudentEntity> $completion) {
    final String _sql = "SELECT * FROM students WHERE id = ? AND syncStatus != 'PENDING_DELETE' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<StudentEntity>() {
      @Override
      @Nullable
      public StudentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfParentPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "parentPhone");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfTeacherName = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherName");
          final int _cursorIndexOfCircleName = CursorUtil.getColumnIndexOrThrow(_cursor, "circleName");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUrl");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final StudentEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpParentPhone;
            _tmpParentPhone = _cursor.getString(_cursorIndexOfParentPhone);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpTeacherName;
            _tmpTeacherName = _cursor.getString(_cursorIndexOfTeacherName);
            final String _tmpCircleName;
            _tmpCircleName = _cursor.getString(_cursorIndexOfCircleName);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpPhotoUrl;
            _tmpPhotoUrl = _cursor.getString(_cursorIndexOfPhotoUrl);
            final Long _tmpJoinDate;
            if (_cursor.isNull(_cursorIndexOfJoinDate)) {
              _tmpJoinDate = null;
            } else {
              _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            }
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new StudentEntity(_tmpId,_tmpName,_tmpAge,_tmpParentPhone,_tmpTeacherId,_tmpTeacherName,_tmpCircleName,_tmpInstituteId,_tmpPhotoUrl,_tmpJoinDate,_tmpGender,_tmpSyncStatus,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countStudents(final String instituteId, final String teacherId, final String gender,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM students \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR gender = ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 4;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 5;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 6;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 7;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 8;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countMemorizations(final String instituteId, final String teacherId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM memorizations\n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 4;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 5;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countRevisions(final String instituteId, final String teacherId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM revisions\n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 4;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 5;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countExams(final String instituteId, final String teacherId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM exams\n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 4;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 5;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTopStudents(final String instituteId, final int limitCount, final String gender,
      final Continuation<? super List<StudentEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT s.* FROM students s\n"
            + "        LEFT JOIN (\n"
            + "            SELECT studentId, COUNT(*) as memCount\n"
            + "            FROM memorizations\n"
            + "            WHERE syncStatus != 'PENDING_DELETE'\n"
            + "            GROUP BY studentId\n"
            + "        ) m ON m.studentId = s.id\n"
            + "        WHERE s.syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR s.instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR s.gender = ?)\n"
            + "        ORDER BY COALESCE(m.memCount, 0) DESC\n"
            + "        LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 6);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 4;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 5;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 6;
    _statement.bindLong(_argIndex, limitCount);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StudentEntity>>() {
      @Override
      @NonNull
      public List<StudentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfParentPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "parentPhone");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfTeacherName = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherName");
          final int _cursorIndexOfCircleName = CursorUtil.getColumnIndexOrThrow(_cursor, "circleName");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUrl");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<StudentEntity> _result = new ArrayList<StudentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudentEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpParentPhone;
            _tmpParentPhone = _cursor.getString(_cursorIndexOfParentPhone);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpTeacherName;
            _tmpTeacherName = _cursor.getString(_cursorIndexOfTeacherName);
            final String _tmpCircleName;
            _tmpCircleName = _cursor.getString(_cursorIndexOfCircleName);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpPhotoUrl;
            _tmpPhotoUrl = _cursor.getString(_cursorIndexOfPhotoUrl);
            final Long _tmpJoinDate;
            if (_cursor.isNull(_cursorIndexOfJoinDate)) {
              _tmpJoinDate = null;
            } else {
              _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            }
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StudentEntity(_tmpId,_tmpName,_tmpAge,_tmpParentPhone,_tmpTeacherId,_tmpTeacherName,_tmpCircleName,_tmpInstituteId,_tmpPhotoUrl,_tmpJoinDate,_tmpGender,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getStudentsByTeacher(final String instituteId, final String teacherId,
      final String gender, final Continuation<? super List<StudentEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM students\n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR gender = ?)\n"
            + "        ORDER BY name ASC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 4;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 5;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 6;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 7;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    _argIndex = 8;
    if (gender == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, gender);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StudentEntity>>() {
      @Override
      @NonNull
      public List<StudentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfParentPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "parentPhone");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfTeacherName = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherName");
          final int _cursorIndexOfCircleName = CursorUtil.getColumnIndexOrThrow(_cursor, "circleName");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUrl");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<StudentEntity> _result = new ArrayList<StudentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudentEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpParentPhone;
            _tmpParentPhone = _cursor.getString(_cursorIndexOfParentPhone);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpTeacherName;
            _tmpTeacherName = _cursor.getString(_cursorIndexOfTeacherName);
            final String _tmpCircleName;
            _tmpCircleName = _cursor.getString(_cursorIndexOfCircleName);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpPhotoUrl;
            _tmpPhotoUrl = _cursor.getString(_cursorIndexOfPhotoUrl);
            final Long _tmpJoinDate;
            if (_cursor.isNull(_cursorIndexOfJoinDate)) {
              _tmpJoinDate = null;
            } else {
              _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            }
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StudentEntity(_tmpId,_tmpName,_tmpAge,_tmpParentPhone,_tmpTeacherId,_tmpTeacherName,_tmpCircleName,_tmpInstituteId,_tmpPhotoUrl,_tmpJoinDate,_tmpGender,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingStudents(final Continuation<? super List<StudentEntity>> $completion) {
    final String _sql = "SELECT * FROM students WHERE syncStatus != 'SYNCED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<StudentEntity>>() {
      @Override
      @NonNull
      public List<StudentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfParentPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "parentPhone");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfTeacherName = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherName");
          final int _cursorIndexOfCircleName = CursorUtil.getColumnIndexOrThrow(_cursor, "circleName");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfPhotoUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUrl");
          final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "joinDate");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<StudentEntity> _result = new ArrayList<StudentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final StudentEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final int _tmpAge;
            _tmpAge = _cursor.getInt(_cursorIndexOfAge);
            final String _tmpParentPhone;
            _tmpParentPhone = _cursor.getString(_cursorIndexOfParentPhone);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpTeacherName;
            _tmpTeacherName = _cursor.getString(_cursorIndexOfTeacherName);
            final String _tmpCircleName;
            _tmpCircleName = _cursor.getString(_cursorIndexOfCircleName);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpPhotoUrl;
            _tmpPhotoUrl = _cursor.getString(_cursorIndexOfPhotoUrl);
            final Long _tmpJoinDate;
            if (_cursor.isNull(_cursorIndexOfJoinDate)) {
              _tmpJoinDate = null;
            } else {
              _tmpJoinDate = _cursor.getLong(_cursorIndexOfJoinDate);
            }
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new StudentEntity(_tmpId,_tmpName,_tmpAge,_tmpParentPhone,_tmpTeacherId,_tmpTeacherName,_tmpCircleName,_tmpInstituteId,_tmpPhotoUrl,_tmpJoinDate,_tmpGender,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countMemorizationsByStudent(final String studentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM memorizations WHERE syncStatus != 'PENDING_DELETE' AND studentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, studentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countRevisionsByStudent(final String studentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM revisions WHERE syncStatus != 'PENDING_DELETE' AND studentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, studentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countExamsByStudent(final String studentId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM exams WHERE syncStatus != 'PENDING_DELETE' AND studentId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, studentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMemorizations(final String instituteId, final String teacherId,
      final String studentId, final Continuation<? super List<MemorizationEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM memorizations \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR studentId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 4;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 5;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 6;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 7;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 8;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MemorizationEntity>>() {
      @Override
      @NonNull
      public List<MemorizationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfFromVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "fromVerse");
          final int _cursorIndexOfToVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "toVerse");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemorizationEntity> _result = new ArrayList<MemorizationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemorizationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpFromVerse;
            _tmpFromVerse = _cursor.getInt(_cursorIndexOfFromVerse);
            final int _tmpToVerse;
            _tmpToVerse = _cursor.getInt(_cursorIndexOfToVerse);
            final String _tmpAmount;
            _tmpAmount = _cursor.getString(_cursorIndexOfAmount);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemorizationEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpFromVerse,_tmpToVerse,_tmpAmount,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMemorizationsForDay(final String instituteId, final long startOfDay,
      final long endOfDay, final Continuation<? super List<MemorizationEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM memorizations \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND date >= ? AND date <= ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 4;
    _statement.bindLong(_argIndex, endOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MemorizationEntity>>() {
      @Override
      @NonNull
      public List<MemorizationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfFromVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "fromVerse");
          final int _cursorIndexOfToVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "toVerse");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemorizationEntity> _result = new ArrayList<MemorizationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemorizationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpFromVerse;
            _tmpFromVerse = _cursor.getInt(_cursorIndexOfFromVerse);
            final int _tmpToVerse;
            _tmpToVerse = _cursor.getInt(_cursorIndexOfToVerse);
            final String _tmpAmount;
            _tmpAmount = _cursor.getString(_cursorIndexOfAmount);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemorizationEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpFromVerse,_tmpToVerse,_tmpAmount,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getMemorizationById(final String id,
      final Continuation<? super MemorizationEntity> $completion) {
    final String _sql = "SELECT * FROM memorizations WHERE id = ? AND syncStatus != 'PENDING_DELETE' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MemorizationEntity>() {
      @Override
      @Nullable
      public MemorizationEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfFromVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "fromVerse");
          final int _cursorIndexOfToVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "toVerse");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final MemorizationEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpFromVerse;
            _tmpFromVerse = _cursor.getInt(_cursorIndexOfFromVerse);
            final int _tmpToVerse;
            _tmpToVerse = _cursor.getInt(_cursorIndexOfToVerse);
            final String _tmpAmount;
            _tmpAmount = _cursor.getString(_cursorIndexOfAmount);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new MemorizationEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpFromVerse,_tmpToVerse,_tmpAmount,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingMemorizations(
      final Continuation<? super List<MemorizationEntity>> $completion) {
    final String _sql = "SELECT * FROM memorizations WHERE syncStatus != 'SYNCED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MemorizationEntity>>() {
      @Override
      @NonNull
      public List<MemorizationEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfFromVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "fromVerse");
          final int _cursorIndexOfToVerse = CursorUtil.getColumnIndexOrThrow(_cursor, "toVerse");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<MemorizationEntity> _result = new ArrayList<MemorizationEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MemorizationEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpFromVerse;
            _tmpFromVerse = _cursor.getInt(_cursorIndexOfFromVerse);
            final int _tmpToVerse;
            _tmpToVerse = _cursor.getInt(_cursorIndexOfToVerse);
            final String _tmpAmount;
            _tmpAmount = _cursor.getString(_cursorIndexOfAmount);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new MemorizationEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpFromVerse,_tmpToVerse,_tmpAmount,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getExams(final String instituteId, final String teacherId, final String studentId,
      final Continuation<? super List<ExamEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR studentId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 4;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 5;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 6;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 7;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 8;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMistakes = CursorUtil.getColumnIndexOrThrow(_cursor, "mistakes");
          final int _cursorIndexOfMastery = CursorUtil.getColumnIndexOrThrow(_cursor, "mastery");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final String _tmpMistakes;
            _tmpMistakes = _cursor.getString(_cursorIndexOfMistakes);
            final String _tmpMastery;
            _tmpMastery = _cursor.getString(_cursorIndexOfMastery);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ExamEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpScore,_tmpMistakes,_tmpMastery,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getExamById(final String id, final Continuation<? super ExamEntity> $completion) {
    final String _sql = "SELECT * FROM exams WHERE id = ? AND syncStatus != 'PENDING_DELETE' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ExamEntity>() {
      @Override
      @Nullable
      public ExamEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMistakes = CursorUtil.getColumnIndexOrThrow(_cursor, "mistakes");
          final int _cursorIndexOfMastery = CursorUtil.getColumnIndexOrThrow(_cursor, "mastery");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final ExamEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final String _tmpMistakes;
            _tmpMistakes = _cursor.getString(_cursorIndexOfMistakes);
            final String _tmpMastery;
            _tmpMastery = _cursor.getString(_cursorIndexOfMastery);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new ExamEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpScore,_tmpMistakes,_tmpMastery,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getExamsForDay(final String instituteId, final long startOfDay, final long endOfDay,
      final Continuation<? super List<ExamEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM exams \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND date >= ? AND date <= ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 4;
    _statement.bindLong(_argIndex, endOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMistakes = CursorUtil.getColumnIndexOrThrow(_cursor, "mistakes");
          final int _cursorIndexOfMastery = CursorUtil.getColumnIndexOrThrow(_cursor, "mastery");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final String _tmpMistakes;
            _tmpMistakes = _cursor.getString(_cursorIndexOfMistakes);
            final String _tmpMastery;
            _tmpMastery = _cursor.getString(_cursorIndexOfMastery);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ExamEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpScore,_tmpMistakes,_tmpMastery,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingExams(final Continuation<? super List<ExamEntity>> $completion) {
    final String _sql = "SELECT * FROM exams WHERE syncStatus != 'SYNCED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ExamEntity>>() {
      @Override
      @NonNull
      public List<ExamEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfSurah = CursorUtil.getColumnIndexOrThrow(_cursor, "surah");
          final int _cursorIndexOfScore = CursorUtil.getColumnIndexOrThrow(_cursor, "score");
          final int _cursorIndexOfMistakes = CursorUtil.getColumnIndexOrThrow(_cursor, "mistakes");
          final int _cursorIndexOfMastery = CursorUtil.getColumnIndexOrThrow(_cursor, "mastery");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<ExamEntity> _result = new ArrayList<ExamEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ExamEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpSurah;
            _tmpSurah = _cursor.getString(_cursorIndexOfSurah);
            final int _tmpScore;
            _tmpScore = _cursor.getInt(_cursorIndexOfScore);
            final String _tmpMistakes;
            _tmpMistakes = _cursor.getString(_cursorIndexOfMistakes);
            final String _tmpMastery;
            _tmpMastery = _cursor.getString(_cursorIndexOfMastery);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new ExamEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpSurah,_tmpScore,_tmpMistakes,_tmpMastery,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRevisions(final String instituteId, final String teacherId,
      final String studentId, final Continuation<? super List<RevisionEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM revisions \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR studentId = ?)\n"
            + "        AND (? IS NULL OR ? = '' OR teacherId = ?)\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 8);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 4;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 5;
    if (studentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, studentId);
    }
    _argIndex = 6;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 7;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    _argIndex = 8;
    if (teacherId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, teacherId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RevisionEntity>>() {
      @Override
      @NonNull
      public List<RevisionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RevisionEntity> _result = new ArrayList<RevisionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RevisionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpLevel;
            _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RevisionEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpContent,_tmpLevel,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRevisionById(final String id,
      final Continuation<? super RevisionEntity> $completion) {
    final String _sql = "SELECT * FROM revisions WHERE id = ? AND syncStatus != 'PENDING_DELETE' LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RevisionEntity>() {
      @Override
      @Nullable
      public RevisionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final RevisionEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpLevel;
            _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new RevisionEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpContent,_tmpLevel,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRevisionsForDay(final String instituteId, final long startOfDay,
      final long endOfDay, final Continuation<? super List<RevisionEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM revisions \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND date >= ? AND date <= ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 4;
    _statement.bindLong(_argIndex, endOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RevisionEntity>>() {
      @Override
      @NonNull
      public List<RevisionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RevisionEntity> _result = new ArrayList<RevisionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RevisionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpLevel;
            _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RevisionEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpContent,_tmpLevel,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingRevisions(final Continuation<? super List<RevisionEntity>> $completion) {
    final String _sql = "SELECT * FROM revisions WHERE syncStatus != 'SYNCED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<RevisionEntity>>() {
      @Override
      @NonNull
      public List<RevisionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "level");
          final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<RevisionEntity> _result = new ArrayList<RevisionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RevisionEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final String _tmpLevel;
            _tmpLevel = _cursor.getString(_cursorIndexOfLevel);
            final String _tmpRating;
            _tmpRating = _cursor.getString(_cursorIndexOfRating);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final long _tmpDate;
            _tmpDate = _cursor.getLong(_cursorIndexOfDate);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new RevisionEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpContent,_tmpLevel,_tmpRating,_tmpCategory,_tmpNote,_tmpDate,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAttendanceForDate(final String instituteId, final String date,
      final Continuation<? super List<AttendanceEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM attendance \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND date = ?\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 3;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttendanceEntity>>() {
      @Override
      @NonNull
      public List<AttendanceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AttendanceEntity> _result = new ArrayList<AttendanceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new AttendanceEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpStatus,_tmpDate,_tmpCreatedAt,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAttendanceForStudent(final String studentId,
      final Continuation<? super List<AttendanceEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM attendance \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND studentId = ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, studentId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttendanceEntity>>() {
      @Override
      @NonNull
      public List<AttendanceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AttendanceEntity> _result = new ArrayList<AttendanceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new AttendanceEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpStatus,_tmpDate,_tmpCreatedAt,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAttendanceHistory(final String instituteId,
      final Continuation<? super List<AttendanceEntity>> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM attendance \n"
            + "        WHERE syncStatus != 'PENDING_DELETE'\n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttendanceEntity>>() {
      @Override
      @NonNull
      public List<AttendanceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AttendanceEntity> _result = new ArrayList<AttendanceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new AttendanceEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpStatus,_tmpDate,_tmpCreatedAt,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingAttendances(
      final Continuation<? super List<AttendanceEntity>> $completion) {
    final String _sql = "SELECT * FROM attendance WHERE syncStatus != 'SYNCED'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AttendanceEntity>>() {
      @Override
      @NonNull
      public List<AttendanceEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
          final int _cursorIndexOfStudentName = CursorUtil.getColumnIndexOrThrow(_cursor, "studentName");
          final int _cursorIndexOfTeacherId = CursorUtil.getColumnIndexOrThrow(_cursor, "teacherId");
          final int _cursorIndexOfInstituteId = CursorUtil.getColumnIndexOrThrow(_cursor, "instituteId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfSyncStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "syncStatus");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AttendanceEntity> _result = new ArrayList<AttendanceEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AttendanceEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpStudentId;
            _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            final String _tmpStudentName;
            _tmpStudentName = _cursor.getString(_cursorIndexOfStudentName);
            final String _tmpTeacherId;
            _tmpTeacherId = _cursor.getString(_cursorIndexOfTeacherId);
            final String _tmpInstituteId;
            _tmpInstituteId = _cursor.getString(_cursorIndexOfInstituteId);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final SyncStatus _tmpSyncStatus;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSyncStatus);
            _tmpSyncStatus = __omranTypeConverters.toSyncStatus(_tmp);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new AttendanceEntity(_tmpId,_tmpStudentId,_tmpStudentName,_tmpTeacherId,_tmpInstituteId,_tmpStatus,_tmpDate,_tmpCreatedAt,_tmpSyncStatus,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countTotalAttendance(final String instituteId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM attendance \n"
            + "        WHERE syncStatus != 'PENDING_DELETE' \n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countPresentOrLateAttendance(final String instituteId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM attendance \n"
            + "        WHERE syncStatus != 'PENDING_DELETE' \n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND status IN ('PRESENT', 'LATE')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countAbsentAttendance(final String instituteId,
      final Continuation<? super Integer> $completion) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM attendance \n"
            + "        WHERE syncStatus != 'PENDING_DELETE' \n"
            + "        AND (? = '' OR instituteId = ?)\n"
            + "        AND status = 'ABSENT'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, instituteId);
    _argIndex = 2;
    _statement.bindString(_argIndex, instituteId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
