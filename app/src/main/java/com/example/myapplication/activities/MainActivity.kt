package com.example.myapplication.activities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.db.DBHelper
import com.example.myapplication.db.DBRepository
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val dbHelper = DBHelper(this@MainActivity)
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val dbRepository = DBRepository(db)

        val arguments = intent.extras
        val currentDateTime:LocalDate

        if (arguments != null) {
            val dateTime = arguments["currentDateTime"].toString()
            currentDateTime = LocalDate.parse(dateTime)
        } else {
            currentDateTime = LocalDateTime.now().toLocalDate()
        }

        if (currentDateTime.plusDays(2).isBefore(LocalDate.now())) {
            moodspinner.visibility = View.GONE
            statespinner.visibility = View.GONE
            timespinner.visibility = View.GONE
            doingspinner.visibility = View.GONE
        } else {
            moodspinner.visibility = View.VISIBLE
            statespinner.visibility = View.VISIBLE
            timespinner.visibility = View.VISIBLE
            doingspinner.visibility = View.VISIBLE
        }
        val dateText = currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
        val currentDateTimeString = currentDateTime.toString()

        maintext.text = dateText

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
                "Мечтательное",
                "Свой вариант..."
        )

        val moodData = dbRepository.getMoodByDate(currentDateTimeString)
        val moodAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, moodData)
        var mLastClickTime = 0L
        moodlistview.adapter = moodAdapter
        moodlistview.setOnItemClickListener { _, _, position, _ ->
            val currTime = System.currentTimeMillis()
            if (mLastClickTime != 0L) {
                if (currTime - mLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    val dialogMoodClickListener =
                        DialogInterface.OnClickListener {_, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val item = moodData[position]
                                    dbRepository.deleteMood(currentDateTimeString, item)
                                    moodData.removeAt(position)
                                    moodAdapter.notifyDataSetChanged()

                                }
                                DialogInterface.BUTTON_NEGATIVE -> {

                                }
                            }
                        }

                    val moodBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    moodBuilder.setMessage("Удалить запись?")
                        .setPositiveButton("Да", dialogMoodClickListener)
                        .setNegativeButton("Нет", dialogMoodClickListener).show()
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
                    if (item == "Свой вариант...") {
                        parent.visibility = View.GONE
                        moodedit.visibility = View.VISIBLE
                        return
                    }
                    if (!moodData.contains(item)) {
                        moodData.add(item)
                        moodAdapter.notifyDataSetChanged()
                        dbRepository.setMood(currentDateTimeString, item)
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

        moodedit.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    val item = moodedit.text.toString()
                    if (!moodData.contains(item)) {
                        moodData.add(item)
                        moodAdapter.notifyDataSetChanged()
                        dbRepository.setMood(currentDateTimeString, item)
                        Toast.makeText(
                            this@MainActivity,
                            "Настроение добавлено",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Такое настроение уже добавлено",Toast.LENGTH_LONG).show()
                    }

                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (v != null) {
                        imm?.hideSoftInputFromWindow(v.windowToken, 0)
                    }

                    moodedit.visibility = View.GONE
                    moodspinner.visibility = View.VISIBLE
                    moodedit.text.clear()
                    moodspinner.setSelection(0)
                    return true
                }
                return false
            }
        })
        moodedit.setHintTextColor(Color.GRAY)

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
                "Бодро",
                "Свой вариант..."
        )

        val stateData = dbRepository.getStateByDate(currentDateTimeString)
        val stateAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateData)
        var mStateLastClickTime = 0L
        statelistview.adapter = stateAdapter
        statelistview.setOnItemClickListener { _, _, position, _ ->
            val currTime = System.currentTimeMillis()
            if (mStateLastClickTime != 0L) {
                if (currTime - mStateLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    val dialogStateClickListener =
                        DialogInterface.OnClickListener {_, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val item = stateData[position]
                                    dbRepository.deleteState(currentDateTimeString, item)
                                    stateData.removeAt(position)
                                    stateAdapter.notifyDataSetChanged()

                                }
                                DialogInterface.BUTTON_NEGATIVE -> {

                                }
                            }
                        }

                    val stateBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    stateBuilder.setMessage("Удалить запись?")
                        .setPositiveButton("Да", dialogStateClickListener)
                        .setNegativeButton("Нет", dialogStateClickListener).show()
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
                    if (item == "Свой вариант...") {
                        parent.visibility = View.GONE
                        stateedit.visibility = View.VISIBLE
                        return
                    }
                    if (!stateData.contains(item)) {
                        stateData.add(item)
                        stateAdapter.notifyDataSetChanged()
                        dbRepository.setState(currentDateTimeString, item)
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

        stateedit.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    val item = stateedit.text.toString()
                    if (!stateData.contains(item)) {
                        stateData.add(item)
                        stateAdapter.notifyDataSetChanged()
                        dbRepository.setState(currentDateTimeString, item)
                        Toast.makeText(
                            this@MainActivity,
                            "Самочувствие добавлено",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Такое самочувствие уже добавлено",Toast.LENGTH_LONG).show()
                    }
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (v != null) {
                        imm?.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    stateedit.visibility = View.GONE
                    statespinner.visibility = View.VISIBLE
                    stateedit.text.clear()
                    statespinner.setSelection(0)
                    return true
                }
                return false
            }
        })
        stateedit.setHintTextColor(Color.GRAY)


        val doingData = dbRepository.getDoingByDate(currentDateTimeString)
        val doingAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doingData)
        var mDoingLastClickTime = 0L
        doinglistview.adapter = doingAdapter
        doinglistview.setOnItemClickListener { _, _, position, _ ->
            val currTime = System.currentTimeMillis()
            if (mDoingLastClickTime != 0L) {
                if (currTime - mDoingLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    val dialogDoingClickListener =
                        DialogInterface.OnClickListener {_, which ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE -> {
                                    val item = doingData[position].split(". ")
                                    dbRepository.deleteDoing(currentDateTimeString, item[0], item[1])
                                    doingData.removeAt(position)
                                    doingAdapter.notifyDataSetChanged()

                                }
                                DialogInterface.BUTTON_NEGATIVE -> {

                                }
                            }
                        }

                    val doingBuilder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    doingBuilder.setMessage("Удалить запись?")
                        .setPositiveButton("Да", dialogDoingClickListener)
                        .setNegativeButton("Нет", dialogDoingClickListener).show()
                }
            }
            mDoingLastClickTime = currTime
        }

        var time: String? = null
        var doing: String? = null
        // timespinner creation
        val times = arrayOf(
                "Время", "Утро", "День", "Вечер", "Ночь", "Свой вариант..."
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
                    if (item == "Свой вариант...") {
                        parent.visibility = View.GONE
                        timeedit.visibility = View.VISIBLE
                        timeedit.isActivated = true
                        return
                    }
                    if (doing != null) {
                        val text = "$item. $doing"
                        if (!doingData.contains(text)) {
                            doingData.add(text)
                            doingAdapter.notifyDataSetChanged()
                            dbRepository.setDoing(currentDateTimeString, item, doing.orEmpty())
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

        timeedit.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    val item = timeedit.text.toString()
                    if (doing != null) {
                        val text = "$item. $doing"
                        if (!doingData.contains(text)) {
                            doingData.add(text)
                            doingAdapter.notifyDataSetChanged()
                            dbRepository.setDoing(currentDateTimeString, item, doing.orEmpty())
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
                        timespinner.setSelection(0)
                        doingspinner.setSelection(0)
                        time = null
                        doing = null
                        timeedit.visibility = View.GONE
                        timespinner.visibility = View.VISIBLE
                        doingedit.visibility = View.GONE
                        doingspinner.visibility = View.VISIBLE
                        timeedit.text.clear()
                        doingedit.text.clear()
                    } else {
                        time = item
                        timeedit.isEnabled = false
                    }
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (v != null) {
                        imm?.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    return true
                }
                return false
            }
        })
        stateedit.setHintTextColor(Color.GRAY)

        val doings = arrayOf(
                "Занятие", "Работа", "Учеба", "Завтрак", "Второй завтрак", "Обед", "Ужин", "Сон",
                "Разглядывание стены", "Свой вариант...")

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
                    if (item == "Свой вариант...") {
                        parent.visibility = View.GONE
                        doingedit.visibility = View.VISIBLE
                        doingedit.isActivated = true
                        return
                    }
                    if (time != null) {
                        val text = "$time. $item"
                        if (!doingData.contains(text)) {
                            doingData.add(text)
                            doingAdapter.notifyDataSetChanged()
                            dbRepository.setDoing(currentDateTimeString, time.orEmpty(), item)
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

        doingedit.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    val item = doingedit.text.toString()
                    if (time != null) {
                        val text = "$time. $item"
                        if (!doingData.contains(text)) {
                            doingData.add(text)
                            doingAdapter.notifyDataSetChanged()
                            dbRepository.setDoing(currentDateTimeString, time.orEmpty(), item)
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
                        timespinner.setSelection(0)
                        doingspinner.setSelection(0)
                        time = null
                        doing = null
                        timeedit.visibility = View.GONE
                        timespinner.visibility = View.VISIBLE
                        doingedit.visibility = View.GONE
                        doingspinner.visibility = View.VISIBLE
                        timeedit.text.clear()
                        doingedit.text.clear()
                    } else {
                        doing = item
                        doingedit.isEnabled = false
                    }
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    if (v != null) {
                        imm?.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                    return true
                }
                return false
            }
        })
        doingedit.setHintTextColor(Color.GRAY)

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