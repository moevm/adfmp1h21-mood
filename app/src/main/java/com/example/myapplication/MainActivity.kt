package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentDateTime = LocalDateTime.now()
        maintext.text = currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

        val moods = arrayOf(
                "Сейчас мое настроение...",
                "Отличное",
                "Радостное",
                "Хорошее",
                "Нормальное",
                "Плохое",
                "Грустное",
                "Ужасное",
                "Задумчивое",
                "Мечтательное"
        )

        val moodData = mutableListOf<String>()
        val moodAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moodData)
        var mLastClickTime = 0L
        moodlistview.adapter = moodAdapter
        moodlistview.setOnItemClickListener { _, _, position, _ ->
            val currTime = System.currentTimeMillis()
            if (mLastClickTime != 0L) {
                if (currTime - mLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    moodData.removeAt(position)
                    moodAdapter.notifyDataSetChanged()
                }
            }
            mLastClickTime = currTime
        }

        val moodSpinnerAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, moods
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val tv =
                    view.findViewById<View>(android.R.id.text1) as TextView
                tv.setTextColor(Color.GRAY)
                return view
            }
        }

        moodspinner.adapter = moodSpinnerAdapter
        moodspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val item = parent!!.getItemAtPosition(position).toString()
                    if (!moodData.contains(item)) {
                        moodData.add(item)
                        moodAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this@MainActivity,
                            "Настроение добавлено",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Такое настроение уже добавлено",Toast.LENGTH_LONG).show()
                    }
                    parent.setSelection(0)
                }
            }
        }
        //
        val states = arrayOf(
                "Я чувствую себя...",
                "Отлично",
                "Хорошо",
                "Нормально",
                "Плохо",
                "Ужасно",
                "Болезненно",
                "Сонно",
                "Бодро"
        )

        val stateData = mutableListOf<String>()
        val stateAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateData)
        var mStateLastClickTime = 0L
        statelistview.adapter = stateAdapter
        statelistview.setOnItemClickListener { _, _, position, _ ->
            val currTime = System.currentTimeMillis()
            if (mStateLastClickTime != 0L) {
                if (currTime - mStateLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    stateData.removeAt(position)
                    stateAdapter.notifyDataSetChanged()
                }
            }
            mStateLastClickTime = currTime
        }

        val stateSpinnerAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, states
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val tv =
                    view.findViewById<View>(android.R.id.text1) as TextView
                tv.setTextColor(Color.GRAY)
                return view
            }
        }

        statespinner.adapter = stateSpinnerAdapter
        statespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val item = parent!!.getItemAtPosition(position).toString()
                    if (!stateData.contains(item)) {
                        stateData.add(item)
                        stateAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this@MainActivity,
                            "Самочувствие добавлено",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Такое самочувствие уже добавлено",Toast.LENGTH_LONG).show()
                    }
                    parent.setSelection(0)
                }
            }
        }

        val doingData = mutableListOf<String>()
        val doingAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doingData)
        var mDoingLastClickTime = 0L
        doinglistview.adapter = doingAdapter
        doinglistview.setOnItemClickListener { _, _, position, _ ->
            val currTime = System.currentTimeMillis()
            if (mDoingLastClickTime != 0L) {
                if (currTime - mDoingLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    stateData.removeAt(position)
                    stateAdapter.notifyDataSetChanged()
                }
            }
            mDoingLastClickTime = currTime
        }

        var time: String? = null
        var doing: String? = null
        // timespinner creation
        val times = arrayOf(
                "Время", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00", "20:00", "21:00","22:00", "23:00", "00:00", "1:00", "2:00",
                "3:00", "4:00", "5:00", "6:00"
        )

        val timeSpinnerAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, times
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val tv =
                    view.findViewById<View>(android.R.id.text1) as TextView
                tv.setTextColor(Color.GRAY)
                return view
            }
        }

        timespinner.adapter = timeSpinnerAdapter
        timespinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val item = parent!!.getItemAtPosition(position).toString()
                    if (doing != null) {
                        val text = "$item $doing"
                        if (!doingData.contains(text)) {
                            doingData.add(text)
                            doingAdapter.notifyDataSetChanged()
                            Toast.makeText(
                                this@MainActivity,
                                "Занятие добавлено",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Такое действие уже добавлено",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        parent.setSelection(0)
                        doingspinner.setSelection(0)
                        time = null
                        doing = null
                    } else {
                        time = item
                    }
                }
            }
        }

        val doings = arrayOf(
                "Занятие", "Работа", "Учеба", "Завтрак", "Второй завтрак", "Обед", "Ужин", "Сон",
                "Разглядывание стены")

        val doingSpinnerAdapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item, doings
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                }
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val tv =
                    view.findViewById<View>(android.R.id.text1) as TextView
                tv.setTextColor(Color.GRAY)
                return view
            }
        }

        doingspinner.adapter = doingSpinnerAdapter
        doingspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != 0) {
                    val item = parent!!.getItemAtPosition(position).toString()
                    if (time != null) {
                        val text = "$time $item"
                        if (!doingData.contains(text)) {
                            doingData.add(text)
                            doingAdapter.notifyDataSetChanged()
                            Toast.makeText(
                                this@MainActivity,
                                "Занятие добавлено",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Такое действие уже добавлено",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        parent.setSelection(0)
                        timespinner.setSelection(0)
                        time = null
                        doing = null
                    } else {
                        doing = item
                    }
                }
            }
        }

        // go to calendar
        btn_calendar.setOnClickListener {
            val myintent = Intent(this, CalendarActivity::class.java)
            startActivity(myintent)
        }
        // go to chart
        btn_chart.setOnClickListener {
            val chartintent = Intent(this, ChartActivity::class.java)
            startActivity(chartintent)
        }

    }
}