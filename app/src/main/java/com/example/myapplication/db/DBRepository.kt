package com.example.myapplication.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase


class DBRepository(val db: SQLiteDatabase){

    fun setMood(date: String, mood:String): Long{
        val values = ContentValues().apply {
            put("DATE", date)
            put("MOOD", mood)
        }
        return db.insert("MOODS", null, values)
    }

    fun setState(date: String, state:String): Long{
        val values = ContentValues().apply {
            put("DATE", date)
            put("STATE", state)
        }
        return db.insert("STATES", null, values)
    }

    fun setDoing(date: String, time:String, doing:String): Long{
        val values = ContentValues().apply {
            put("DATE", date)
            put("TIME", time)
            put("DOING", doing)
        }
        return db.insert("DOINGS", null, values)
    }

    fun deleteMood(date: String, mood:String) {
        val selection = "MOOD IS ? AND DATE IS ?"
        val selectionArgs = arrayOf(mood, date)
        db.delete("MOODS", selection, selectionArgs)
    }

    fun deleteState(date: String, state:String) {
        val selection = "STATE IS ? AND DATE IS ?"
        val selectionArgs = arrayOf(state, date)
        db.delete("STATES", selection, selectionArgs)
    }

    fun deleteDoing(date: String, time:String, doing:String) {
        val selection = "DOING IS ? AND DATE IS ? AND TIME IS ?"
        val selectionArgs = arrayOf(doing, date, time)
        db.delete("DOINGS", selection, selectionArgs)
    }

    fun getMoodByDate(date:String): MutableList<String> {
        val projection = arrayOf("MOOD")

        val selection = "DATE IS ?"
        val selectionArgs = arrayOf(date)
        val sortOrder = "MOOD DESC"

        val cursor = db.query(
            "MOODS",   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        val items = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val item = getString(getColumnIndex("MOOD"))
                items.add(item)
            }
        }
        return items
    }

    fun getStateByDate(date:String): MutableList<String> {
        val projection = arrayOf("STATE")

        val selection = "DATE IS ?"
        val selectionArgs = arrayOf(date)
        val sortOrder = "STATE DESC"

        val cursor = db.query(
            "STATES",   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        val items = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val item = getString(getColumnIndex("STATE"))
                items.add(item)
            }
        }
        return items
    }

    fun getDoingByDate(date:String): MutableList<String> {
        val projection = arrayOf("TIME", "DOING")

        val selection = "DATE IS ?"
        val selectionArgs = arrayOf(date)
        val sortOrder = "TIME DESC"

        val cursor = db.query(
            "DOINGS",   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        val items = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val item = "${getString(getColumnIndex("TIME"))}. ${getString(getColumnIndex("DOING"))}"
                items.add(item)
            }
        }
        return items
    }

    fun cleanBD(){
        db.execSQL("DROP TABLE IF EXISTS MOODS")
        db.execSQL("DROP TABLE IF EXISTS STATES")
        db.execSQL("DROP TABLE IF EXISTS DOINGS")
    }
}