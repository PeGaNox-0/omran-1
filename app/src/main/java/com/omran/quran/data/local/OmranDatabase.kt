package com.omran.quran.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.omran.quran.data.local.dao.OmranDao
import com.omran.quran.data.local.entity.AttendanceEntity
import com.omran.quran.data.local.entity.ExamEntity
import com.omran.quran.data.local.entity.MemorizationEntity
import com.omran.quran.data.local.entity.RevisionEntity
import com.omran.quran.data.local.entity.StudentEntity

class OmranTypeConverters {
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = try {
        SyncStatus.valueOf(value)
    } catch (e: Exception) {
        SyncStatus.SYNCED
    }
}

@Database(
    entities = [
        StudentEntity::class,
        MemorizationEntity::class,
        ExamEntity::class,
        RevisionEntity::class,
        AttendanceEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
@TypeConverters(OmranTypeConverters::class)
abstract class OmranDatabase : RoomDatabase() {
    abstract fun omranDao(): OmranDao

    companion object {
        const val DATABASE_NAME = "omran_quran.db"

        val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE students ADD COLUMN gender TEXT NOT NULL DEFAULT 'MALE'")
            }
        }
    }
}
