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

class Mutable: AppCompatActivity() {

    var myDbManager = MyDbManager(this)
    var id = 0

    private lateinit var editTextText5_mutable: EditText
    private lateinit var editTextText2_mutable: EditText
    private lateinit var editTextText3_mutable: EditText
    private lateinit var textView11_mutable: TextView
    private lateinit var editTextText4_mutable: EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mutable)
        editTextText5_mutable = findViewById<EditText>(R.id.editTextText5_mutable)
        editTextText2_mutable = findViewById<EditText>(R.id.editTextText2_mutable)
        editTextText3_mutable = findViewById<EditText>(R.id.editTextText3_mutable)
        editTextText4_mutable = findViewById<EditText>(R.id.editTextText4_mutable)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_mutable)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getColor(this, R.color.base_them_for_liters).also {
                window.statusBarColor = it
            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.base_them_for_liters, theme)
        }


        editTextText4_mutable.setOnClickListener {
            editTextText4_mutable.text.clear()
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year)
                    editTextText4_mutable.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        getMyIntents()

        editTextText5_mutable.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    val dotIndex = input.indexOf('.')
                    if (dotIndex != -1 && input.length - dotIndex > 3) {
                        val truncated = input.substring(0, dotIndex + 3)
                        editTextText5_mutable.setText(truncated)
                        editTextText5_mutable.setSelection(truncated.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        editTextText2_mutable.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val input = it.toString()
                    val dotIndex = input.indexOf('.')
                    if (dotIndex != -1 && input.length - dotIndex > 3) {
                        val truncated = input.substring(0, dotIndex + 3)
                        editTextText2_mutable.setText(truncated)
                        editTextText2_mutable.setSelection(truncated.length)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
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


    private fun getMyIntents() {
        id = intent.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
        val liters = intent.getStringExtra(MyIntentConstants.I_LITERS_KEY)?.replace(',', '.') ?: ""
        val amount = intent.getStringExtra(MyIntentConstants.I_AMOUNT_KEY)?.replace(',', '.') ?: ""
        editTextText3_mutable.setText(intent.getStringExtra(MyIntentConstants.I_MILEAGE_KEY))
        var propperDate = convertDateToNormalFormat((intent.getStringExtra(MyIntentConstants.I_DATE_KEY)).toString())
        editTextText4_mutable.setText(propperDate)
        editTextText5_mutable.setText(liters)
        editTextText2_mutable.setText(amount)
    }


    fun onClickUpdate(view: View) {
        val myLiters = editTextText5_mutable.text.toString()
        val myAmount = editTextText2_mutable.text.toString()
        val myMileage = editTextText3_mutable.text.toString()
        val myDate = editTextText4_mutable.text.toString()
        val datePattern = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(\\d{4})$"

        if (myLiters.isNotEmpty() && myAmount.isNotEmpty() && myMileage.isNotEmpty() && myDate.isNotEmpty()) {
            if (myDate.matches(Regex(datePattern))) {
                val DbDate = convertDateToDbFormat(myDate)
                var myLitersMod = ((myLiters.toDouble()) * 100).toInt()
                var myAmountMod = ((myAmount.toDouble()) * 100).toInt()
                var myMileageMod = myMileage.toInt()
                CoroutineScope(Dispatchers.Main).launch {
                    myDbManager.updateItem(DbDate, myAmountMod, myLitersMod, id, myMileageMod)
                }
                finish()
            } else {
                Toast.makeText(this, "Вы ввели некорректную дату!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Вы заполнили не все данные!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun convertDateToDbFormat(date: String): String {
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate ?: return date)
    }


    private fun convertDateToNormalFormat(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parsedDate = inputFormat.parse(date)
        return outputFormat.format(parsedDate ?: return date)
    }


    fun onClickDelete(view: View) {
        myDbManager.removeItemFromDb(id.toString())
        finish()
    }
}
