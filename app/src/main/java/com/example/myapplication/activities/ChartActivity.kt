package com.example.myapplication.activities

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.db.DBHelper
import com.example.myapplication.db.DBRepository
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_chart.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class ChartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        val defaultZoneId: ZoneId = ZoneId.systemDefault()

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
                graph.removeAllSeries()
                var startDay: LocalDate = currentDateTime
                if (position == 0) {
                    startDay = currentDateTime.minusWeeks(1)
                } else if (position == 1) {
                    startDay = currentDateTime.minusMonths(1)
                } else if (position == 2) {
                    startDay = currentDateTime.minusYears(1)
                }
                var mood = dbRepository.getMostPopularMoodByDates(startDay, currentDateTime)
                var state = dbRepository.getMostPopularStateByDates(startDay, currentDateTime)
                var doing = dbRepository.getMostPopularDoingByDates(startDay, currentDateTime)
                val map = dbRepository.getActivitiesByDays(startDay, currentDateTime)
                val mostActiveDay = map.maxBy { it.value }?.key.toString()
                val mostNotActiveDay = map.minBy { it.value }?.key.toString()


                if (mood == "null") {
                    mood = "-"
                }

                if (state == "null") {
                    state = "-"
                }

                if (doing == "null") {
                    doing = "-"
                }
                moodtext.text = mood
                statetext.text = state
                doingtext.text = doing
                mostactivedaytext.text = mostActiveDay
                mostnotactivedaytext.text = mostNotActiveDay

                val dataPoint = mutableListOf<DataPoint>()
                for (key in map.keys) {
                    val localDate = LocalDate.parse(key)
                    val date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant())
                    dataPoint.add(DataPoint(date, map[key]!!.toDouble()))
                }
                val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>(dataPoint.toTypedArray())
                graph.addSeries(series)
                graph.gridLabelRenderer.labelFormatter =
                    DateAsXAxisLabelFormatter(this@ChartActivity);
                graph.gridLabelRenderer.horizontalAxisTitle = "Дата"
                graph.gridLabelRenderer.verticalAxisTitle = "Количество записей"
                graph.gridLabelRenderer.textSize = 35F
                graph.gridLabelRenderer.numHorizontalLabels = map.keys.size
                graph.gridLabelRenderer.setHorizontalLabelsAngle(45)
                graph.gridLabelRenderer.numVerticalLabels = map.maxBy { it.value }!!.value

                graph.viewport.isScrollable = true
                graph.viewport.isScalable = true
                //graph.viewport.isXAxisBoundsManual = true
                graph.viewport.setMaxX(Date.from(LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay(defaultZoneId).toInstant()).time.toDouble())
                graph.viewport.setMinX(Date.from(LocalDateTime.now().toLocalDate().minusDays(3).atStartOfDay(defaultZoneId).toInstant()).time.toDouble())
                graph.viewport.scrollToEnd()
            }
        }

        // go to calendar
        btn_calendar.setOnClickListener {
            val myintent = Intent(this, CalendarActivity::class.java)
            startActivity(myintent)
        }

    }
}