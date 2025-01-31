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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class GenerateStatistics: AppCompatActivity() {

    var myDbManager = MyDbManager(this)

    private lateinit var imageButton_Refulling: ImageButton
    private lateinit var textView_Refulling: TextView
    private lateinit var imageButton_Money: ImageView
    private lateinit var textView_Money: TextView
    private lateinit var imageButton_Settings: ImageButton
    private lateinit var textView_Settings: TextView

    private lateinit var editTextText_statistic: EditText
    private lateinit var editTextText6_statistic: EditText
    private lateinit var editTextText7_statistic: EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_generate_statistics)
        imageButton_Refulling = findViewById<ImageButton>(R.id.GS_imageButton_Refulling)
        textView_Refulling = findViewById<TextView>(R.id.GS_textView_Refulling)
        imageButton_Money = findViewById<ImageButton>(R.id.GS_imageButton_Money)
        textView_Money = findViewById<TextView>(R.id.GS_textView_Money)
        imageButton_Settings = findViewById<ImageButton>(R.id.GS_imageButton_Settings)
        textView_Settings = findViewById<TextView>(R.id.GS_textView_Settings)
        editTextText_statistic = findViewById<EditText>(R.id.editTextText_statistic)
        editTextText6_statistic = findViewById<EditText>(R.id.editTextText6_statistic)
        editTextText7_statistic = findViewById<EditText>(R.id.editTextText7_statistic)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_statistic)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getColor(this, R.color.base_them_for_liters).also { window.statusBarColor = it }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.base_them_for_liters, theme)
        }

        editTextText7_statistic.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    val dotIndex = input.indexOf('.')
                    if (dotIndex != -1 && input.length - dotIndex > 3) {
                        val truncated = input.substring(0, dotIndex + 3)
                        editTextText7_statistic.setText(truncated)
                        editTextText7_statistic.setSelection(truncated.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        editTextText6_statistic.setText(getCurrentDate())
        editTextText6_statistic.setOnClickListener {
            editTextText6_statistic.text.clear()
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                    editTextText6_statistic.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        editTextText_statistic.setOnClickListener {
            editTextText_statistic.text.clear()
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                    editTextText_statistic.setText(selectedDate)
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


    fun clickOnGenerate(view: View) {
        var startDateforIntent = editTextText_statistic.text.toString()
        var endDateForIntent = editTextText6_statistic.text.toString()
        var howLitersForIntent = editTextText7_statistic.text.toString()
        val datePattern = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$"
        var  startDateBoolean = startDateforIntent.matches(Regex(datePattern))
        var endDateBoolean = endDateForIntent.matches(Regex(datePattern))
        var dateBoolean = compareDates(startDateforIntent, endDateForIntent)

        if ( startDateBoolean && endDateBoolean && dateBoolean) {
            val intent = Intent(this, Statistic::class.java).apply {
                putExtra(MyIntentConstants.I_START_DATE, startDateforIntent)
                putExtra(MyIntentConstants.I_END_DATE, endDateForIntent)
                putExtra(MyIntentConstants.I_HOW_MANY_LITERS, howLitersForIntent)
            }
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Вы ввели некорректную дату!", Toast.LENGTH_SHORT).show()
        }
    }


    fun compareDates(startDate: String, endDate: String): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return try {
            val start = dateFormat.parse(startDate)
            val end = dateFormat.parse(endDate)
            start != null && end != null && (start.before(end) || start.equals(end))
        } catch (e: ParseException) {
            e.printStackTrace()
            false
        }
    }


    fun goToMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    fun goToStatistic(view: View) {
        var intent = Intent(this, GenerateStatistics::class.java)
        startActivity(intent)
    }


    fun goToSettings(view: View) {
        var intent = Intent(this, Settings::class.java)
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
