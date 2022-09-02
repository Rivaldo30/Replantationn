package com.sobi.replantation.domain.local

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.migration.Migration
import com.sobi.replantation.domain.model.SerahTerima


@Database(entities = [AssignmentData::class, AreaData::class, SerahTerimaData::class, BuktiSerahTerimaData::class], version = 3)
abstract class SobiDatabase : RoomDatabase() {
    abstract fun assignmentDao(): AssignmentDao
    abstract fun areaDao(): AreaDao
    abstract fun serahTerimaDao() : SerahTerimaDao
    abstract fun buktiSerahTerimaDao() : BuktiSerahTerimaDao

}

