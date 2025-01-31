package VasiliyGubenkov.Liters

import VasiliyGubenkov.Liters.DB.MyDbManager
import VasiliyGubenkov.Liters.DB.MyIntentConstants
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditActivity: AppCompatActivity() {

    var myDbManager = MyDbManager(this)
    var id = 0

    private lateinit var editTextText5: EditText
    private lateinit var editTextText2: EditText
    private lateinit var editTextText3: EditText
    private lateinit var editTextText4: EditText
    private lateinit var textView11: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)

        editTextText5 = findViewById<EditText>(R.id.editTextText5)
        editTextText2 = findViewById<EditText>(R.id.editTextText2)
        editTextText3 = findViewById<EditText>(R.id.editTextText3)
        editTextText4 = findViewById<EditText>(R.id.editTextText4)
        textView11 = findViewById<TextView>(R.id.textView11)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_edit)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var tryMax = intent.getStringExtra(MyIntentConstants.I_MILEAGE_MAX)
        if (tryMax != null && tryMax.isNotEmpty()) {
            textView11.text = tryMax
        } else {
            textView11.text = "1"
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getColor(this, R.color.base_them_for_liters)
                .also { window.statusBarColor = it }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.base_them_for_liters, theme)
        }

        editTextText5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    val dotIndex = input.indexOf('.')
                    if (dotIndex != -1 && input.length - dotIndex > 3) {
                        val truncated = input.substring(0, dotIndex + 3)
                        editTextText5.setText(truncated)
                        editTextText5.setSelection(truncated.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        editTextText2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    val dotIndex = input.indexOf('.')
                    if (dotIndex != -1 && input.length - dotIndex > 3) {
                        val truncated = input.substring(0, dotIndex + 3)
                        editTextText2.setText(truncated)
                        editTextText2.setSelection(truncated.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        editTextText4.setText(getCurrentDate())
        editTextText4.setOnClickListener {
            editTextText4.text.clear()
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                    editTextText4.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
    }


    fun getCurrentDate(): String {
        var time = Calendar.getInstance().time
        var formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        var ftime = formatter.format(time)
        return ftime
    }


    fun onClickSave(view: View) {
        val myLiters = editTextText5.text.toString()
        val myAmount = editTextText2.text.toString()
        val myMileage = editTextText3.text.toString()
        val myDate = editTextText4.text.toString()
        val datePattern = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$"

        if (myLiters.isNotEmpty() && myAmount.isNotEmpty() && myMileage.isNotEmpty() && myDate.isNotEmpty()) {
            if (myDate.matches(Regex(datePattern))) {
                if(myMileage.toDouble() >= (textView11.text.toString().toInt()).toDouble()) {
                    val myDateFormatted =
                        convertDateToDbFormat(myDate)
                    var myLitersMod = ((myLiters.toDouble()) * 100).toInt()
                    var myAmountMod = ((myAmount.toDouble()) * 100).toInt()
                    var myMileageMod = myMileage.toInt()
                    CoroutineScope(Dispatchers.Main).launch {
                        myDbManager.insertToDb(
                            myDateFormatted,
                            myAmountMod,
                            myLitersMod,
                            myMileageMod
                        )
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Пробег не может быть меньше, чем предыдущий!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Вы ввели некорректную дату!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Вы заполнили не все поля!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun convertDateToDbFormat(date: String): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate ?: return date)
    }


    fun goToStatistic(view: View) {
        var intent = Intent(this, GenerateStatistics::class.java)
        startActivity(intent)
    }

    fun goToSettings(view: View) {
        var intent = Intent(this, Settings::class.java)
        startActivity(intent)
    }

    fun goToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }


    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }
}

