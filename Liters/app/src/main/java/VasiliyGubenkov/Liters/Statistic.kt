package VasiliyGubenkov.Liters

import VasiliyGubenkov.Liters.DB.MyDbManager
import VasiliyGubenkov.Liters.DB.MyIntentConstants
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Statistic: AppCompatActivity() {

    var myDbManager = MyDbManager(this)

    lateinit var textView30 : TextView
    lateinit var textView31: TextView
    lateinit var textView32: TextView
    lateinit var textView33: TextView
    lateinit var textView36: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_statistic)
        textView30 = findViewById<TextView>(R.id.textView30)
        textView31 = findViewById<TextView>(R.id.textView31)
        textView32 = findViewById<TextView>(R.id.textView32)
        textView33 = findViewById<TextView>(R.id.textView33)
        textView36 = findViewById<TextView>(R.id.textView36)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_statistic1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ContextCompat.getColor(this, R.color.base_them_for_liters).also { window.statusBarColor = it
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = resources.getColor(R.color.base_them_for_liters, theme)
        }

        var fromGenerateStartDate = intent.getStringExtra(MyIntentConstants.I_START_DATE).toString()
        var fromGenerateEndDate = intent.getStringExtra(MyIntentConstants.I_END_DATE).toString()
        var fromGenerateLiters = intent.getStringExtra(MyIntentConstants.I_HOW_MANY_LITERS)
        myDbManager.openDb()
        var fromDb = myDbManager.myTestDBData(fromGenerateStartDate, fromGenerateEndDate)
        var fromDbAllAmounts = fromDb.myAmounts
        var fromDbAllLiters = fromDb.myLiters
        var fromDbAllMileages = fromDb.myMileages
        var countAllLiters = 0.00
        for (i in fromDbAllLiters) {
            countAllLiters += i
        }


        if(fromGenerateLiters != null) {
            fromGenerateLiters = "0"
        }

        var countAllLitersMinusRestInTank = countAllLiters - fromGenerateLiters!!.toInt()
        var countAllLitersMinusRestInTankRounded = String.format("%.2f", countAllLitersMinusRestInTank)
        var countAllAmounts = 0.00
        for (i in fromDbAllAmounts) {
            countAllAmounts +=i
        }
        var countAllAmountsRounded = String.format("%.2f", countAllAmounts)
        var maxMileage = fromDbAllMileages.maxOrNull()
        var minMileage = fromDbAllMileages.minOrNull()
        var rangeMileage = maxMileage!! - minMileage!!
        textView30.text = countAllAmountsRounded.toString()
        textView31.text = countAllLitersMinusRestInTankRounded
        textView32.text = rangeMileage.toString()
        textView33.text = String.format("%.2f", ((countAllLitersMinusRestInTank / rangeMileage.toDouble()) * 100))
        textView36.text = String.format("%.2f", ((countAllAmounts / rangeMileage) * 100))
    }


    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }


    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
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
}