package com.example.myapplication.activities

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.myapplication.R
import com.example.myapplication.db.DBHelper
import com.example.myapplication.db.DBRepository
import kotlinx.android.synthetic.main.activity_chart.*
import java.time.LocalDate
import java.time.LocalDateTime

class ChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val dbHelper = DBHelper(this@ChartActivity)
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val dbRepository = DBRepository(db)

        val currentDateTime = LocalDateTime.now().toLocalDate()

        val periods = arrayOf(
            "Неделю",
            "Месяц",
            "Год"
        )
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            periods
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        periodspinner.adapter = adapter
        periodspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }


            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var startDay: LocalDate = currentDateTime
                if (position == 0) {
                    startDay = currentDateTime.minusWeeks(1)
                } else if (position == 1) {
                    startDay = currentDateTime.minusMonths(1)
                } else if (position == 2) {
                    startDay = currentDateTime.minusYears(1)
                }
                val mood = dbRepository.getMostPopularMoodByDates(startDay, currentDateTime)
                val state = dbRepository.getMostPopularStateByDates(startDay, currentDateTime)
                val doing = dbRepository.getMostPopularDoingByDates(startDay, currentDateTime)
                val map = dbRepository.getActivitiesByDays(startDay, currentDateTime)
                val mostActiveDay = map.maxBy { it.value }?.key.toString()
                val mostNotActiveDay = map.minBy { it.value }?.key.toString()

                moodtext.text = mood
                statetext.text = state
                doingtext.text = doing
                mostactivedaytext.text = mostActiveDay
                mostnotactivedaytext.text = mostNotActiveDay
            }
        }

        // go to calendar
        btn_calendar.setOnClickListener {
            val myintent = Intent(this, CalendarActivity::class.java)
            startActivity(myintent)
        }

    }
}