package com.example.myapplication.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE MOODS (" +
                    "ID INTEGER PRIMARY KEY," +
                    "DATE TEXT," +
                    "MOOD TEXT)")
        db.execSQL(
            "CREATE TABLE STATES (" +
                    "ID INTEGER PRIMARY KEY," +
                    "DATE TEXT," +
                    "STATE TEXT)")
        db.execSQL(
            "CREATE TABLE DOINGS (" +
                    "ID INTEGER PRIMARY KEY," +
                    "DATE TEXT," +
                    "TIME TEXT," +
                    "DOING TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS MOODS")
        db.execSQL("DROP TABLE IF EXISTS STATES")
        db.execSQL("DROP TABLE IF EXISTS DOINGS")
        onCreate(db)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "MoodReader.db"
    }
}