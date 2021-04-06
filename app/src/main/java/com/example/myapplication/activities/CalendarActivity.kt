package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView;
import android.widget.Toast
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_calendar.btn_chart
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // go to chart
        btn_chart.setOnClickListener {
            val myintent = Intent(this, ChartActivity::class.java)
            startActivity(myintent)
        }

        calendar_view.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(p0: CalendarView, p1: Int, p2: Int, p3: Int) {
                val myintent = Intent(this@CalendarActivity, MainActivity::class.java)
                val currentDateTime = LocalDate.of(p1, Month.values()[p2], p3).toString()
                myintent.putExtra("currentDateTime", currentDateTime);
                startActivity(myintent)
            }
        })
    }
}