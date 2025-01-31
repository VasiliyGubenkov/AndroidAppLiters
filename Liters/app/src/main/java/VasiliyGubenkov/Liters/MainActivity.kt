package VasiliyGubenkov.Liters

import VasiliyGubenkov.Liters.DB.MaxMileageForEdit
import VasiliyGubenkov.Liters.DB.MyAdapter
import VasiliyGubenkov.Liters.DB.MyDbManager
import VasiliyGubenkov.Liters.DB.MyIntentConstants
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)
    var job: Job? = null

    private lateinit var imageButton_Refulling: ImageButton
    private lateinit var textView_Refulling: TextView
    private lateinit var imageButton_Money: ImageView
    private lateinit var textView_Money: TextView
    private lateinit var imageButton_Settings: ImageButton
    private lateinit var textView_Settings: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        imageButton_Refulling = findViewById<ImageButton>(R.id.imageButton_Refulling)
        textView_Refulling = findViewById<TextView>(R.id.textView_Refulling)
        imageButton_Money = findViewById<ImageButton>(R.id.imageButton_Money)
        textView_Money = findViewById<TextView>(R.id.textView_Money)
        imageButton_Settings = findViewById<ImageButton>(R.id.imageButton_Settings)
        textView_Settings = findViewById<TextView>(R.id.textView_Settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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

        init()
    }


    fun init() {
        var rc_view = findViewById<RecyclerView>(R.id.rcView)
        rc_view.layoutManager = LinearLayoutManager(this)
        rc_view.adapter = myAdapter

    }


    fun fillAdapter() {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            var data = myDbManager.readDbData()
            myAdapter.updateAdapter(data)

            val textView20 = findViewById<TextView>(R.id.textView20)
            val textView = findViewById<TextView>(R.id.textView)

            if (data.isEmpty()) {
                textView20.visibility = View.VISIBLE
                textView.visibility = View.INVISIBLE
            } else {
                textView20.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        }
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


    fun goToEditForAddNew(view: View) {
        val intent = Intent(this, EditActivity::class.java).apply {
            putExtra(MyIntentConstants.I_MILEAGE_MAX, MaxMileageForEdit.maxMileage)
        }
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }


    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

}