package com.example.myapplication.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_chart.*

class StatisticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistic)
        // go to calendar
        btn_calendar.setOnClickListener {
            val myintent = Intent(this, CalendarActivity::class.java)
            startActivity(myintent)
        }
    }
}