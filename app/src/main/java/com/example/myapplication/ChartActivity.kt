package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_chart.*

class ChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

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
                Toast.makeText(this@ChartActivity, periods[position], Toast.LENGTH_LONG).show()
            }
        }
        periodspinner2.adapter = adapter
        periodspinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                Toast.makeText(this@ChartActivity, periods[position], Toast.LENGTH_LONG).show()
            }
        }
        // go to calendar
        btn_calendar.setOnClickListener {
            val myintent = Intent(this, CalendarActivity::class.java)
            startActivity(myintent)
        }

        btn_form.setOnClickListener {
            val formintent = Intent(this, StatisticActivity::class.java)
            startActivity(formintent)
        }
    }
}