package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val moods = arrayOf(
                "Добавьте настроение",
                "Хорошее",
                "Плохое",
                "Грустное",
                "Радостное",
                "Задумчивое"
        )
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                moods
        ) {
            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                        position,
                        convertView,
                        parent
                ) as TextView


                return view
            }

            override fun isEnabled(position: Int): Boolean {
                // disable first item
                // first item is display as hint
                return position != 0
            }
        }//ITS JUST TO MAKE A GODDAMN HINT
        moodspinner.adapter = adapter
        moodspinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                Toast.makeText(this@MainActivity, moods[position], Toast.LENGTH_LONG).show()
            }

            //            state creation
        }
        val states = arrayOf(
                "Добавьте самочувствие",
                "Хорошее",
                "Ужасное",
                "Болезненное",
                "Сонное",
                "Бодрое"
        )
        val stateadapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, states
        ){
            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                        position,
                        convertView,
                        parent
                ) as TextView
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                // disable first item
                // first item is display as hint
                return position != 0
            }
        }//ITS JUST TO MAKE A GODDAMN HINT

        statespinner.adapter = stateadapter
        statespinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                    ) {
                        Toast.makeText(this@MainActivity, states[position], Toast.LENGTH_LONG).show()
                    }
                }
        // timespinner creation
        val times = arrayOf(
                "Добавьте время", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00",
                "16:00", "17:00", "18:00", "19:00", "20:00", "21:00","22:00", "23:00", "00:00", "1:00", "2:00",
                "3:00", "4:00", "5:00", "6:00"
        )
        val timeadapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                times
        ){
            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                        position,
                        convertView,
                        parent
                ) as TextView
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                // disable first item
                // first item is display as hint
                return position != 0
            }
        }//ITS JUST TO MAKE A GODDAMN HINT

        timespinner.adapter = timeadapter
        timespinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                    ) {
                        Toast.makeText(this@MainActivity, times[position], Toast.LENGTH_LONG).show()
                    }
                }
        // doings creation
        val doings = arrayOf(
                "Добавьте занятие", "Работа", "Учеба", "Завтрак", "Второй завтрак", "Обед", "Ужин", "Сон",
                "Разглядывание стены"
        )
        val doingadapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                doings
        ){
            override fun getDropDownView(
                    position: Int,
                    convertView: View?,
                    parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                        position,
                        convertView,
                        parent
                ) as TextView
                return view
            }

            override fun isEnabled(position: Int): Boolean {
                // disable first item
                // first item is display as hint
                return position != 0
            }
        }//ITS JUST TO MAKE A GODDAMN HINT

        doingspinner.adapter = doingadapter
        doingspinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                    ) {
                        Toast.makeText(this@MainActivity, doings[position], Toast.LENGTH_LONG).show()
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