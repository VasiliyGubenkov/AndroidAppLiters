package VasiliyGubenkov.Liters

import VasiliyGubenkov.Liters.DB.MyDbManager
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Settings : AppCompatActivity() {

    var myDbManager = MyDbManager(this)

    private lateinit var imageButton_Refulling: ImageButton
    private lateinit var textView_Refulling: TextView
    private lateinit var imageButton_Money: ImageView
    private lateinit var textView_Money: TextView
    private lateinit var imageButton_Settings: ImageButton
    private lateinit var textView_Settings: TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        imageButton_Refulling = findViewById<ImageButton>(R.id.Sett_imageButton_Refulling)
        textView_Refulling = findViewById<TextView>(R.id.Sett_textView_Refulling)
        imageButton_Money = findViewById<ImageButton>(R.id.Sett_imageButton_Money)
        textView_Money = findViewById<TextView>(R.id.Sett_textView_Money)
        imageButton_Settings = findViewById<ImageButton>(R.id.Sett_imageButton_Settings)
        textView_Settings = findViewById<TextView>(R.id.Sett_textView_Settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_settings)) { v, insets ->
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