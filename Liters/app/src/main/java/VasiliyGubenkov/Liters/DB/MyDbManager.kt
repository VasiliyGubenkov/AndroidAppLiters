package VasiliyGubenkov.Liters.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale


class MyDbManager(context: Context) {

    var myDbHelper = MyDbHelper(context)
    var db: SQLiteDatabase? = null


    fun openDb () {
        db = myDbHelper.writableDatabase
    }


    fun closeDb() {
        myDbHelper.close()
    }


    suspend fun insertToDb (date: String, amount: Int, liters: Int, mileage: Int) = withContext(Dispatchers.IO) {
        var values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_AMOUNT, amount)
            put(MyDbNameClass.COLUMN_NAME_LITERS, liters)
            put(MyDbNameClass.COLUMN_NAME_MILEAGE, mileage)
        }
        db?.insert(MyDbNameClass.TABLE_NAME, null, values)
    }


    suspend fun updateItem (date: String, amount: Int, liters: Int, id: Int, mileage : Int) = withContext(Dispatchers.IO) {
        var selection = BaseColumns._ID + "=$id"
        var values = ContentValues().apply {
            put(MyDbNameClass.COLUMN_NAME_DATE, date)
            put(MyDbNameClass.COLUMN_NAME_AMOUNT, amount)
            put(MyDbNameClass.COLUMN_NAME_LITERS, liters)
            put(MyDbNameClass.COLUMN_NAME_MILEAGE, mileage)
        }
        db?.update(MyDbNameClass.TABLE_NAME, values, selection, null)
    }


    fun removeItemFromDb (id: String){
        var selection = BaseColumns._ID + "=$id"
        db?.delete(MyDbNameClass.TABLE_NAME, selection, null)
    }


    @SuppressLint("Range")
    fun readDbData(): ArrayList<ListItem> {
        var dataList = ArrayList<ListItem>()
        var cursor = db?.query(MyDbNameClass.TABLE_NAME, null, null, null, null, null, null)
        while (cursor?.moveToNext()!!){
            var dataDate = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE))
            var dataAmount = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_AMOUNT))
            var dataLiters = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_LITERS))
            var dataId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            var mileage = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_MILEAGE))
            var item = ListItem()
            item.date = dataDate.toString()
            item.amount = dataAmount
            item.liters = dataLiters
            item.id = dataId
            item.mileage = mileage
            dataList.add(item)
        }
        cursor.close()
        dataList.reverse()
        return dataList
    }


    @SuppressLint("Range")
    fun myTestDBData(startDate: String, endDate: String): MyDbData{
        val dates = ArrayList<String>()
        val amounts = ArrayList<Double>()
        val liters = ArrayList<Double>()
        val mileages = ArrayList<Int>()

        val startDateFormatted = convertDateToDbFormat(startDate)
        val endDateFormatted = convertDateToDbFormat(endDate)

        val selection = "${MyDbNameClass.COLUMN_NAME_DATE} BETWEEN ? AND ?"
        val selectionArgs = arrayOf(startDateFormatted, endDateFormatted)

        var cursor = db?.query(MyDbNameClass.TABLE_NAME, null, selection, selectionArgs, null, null, null)

        while (cursor?.moveToNext()!!) {
            val dataDate = cursor.getString(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_DATE))
            val dataAmount = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_AMOUNT))
            val dataAmountDouble = dataAmount/100.00
            val dataLiters = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_LITERS))
            val dataLitersDouble = dataLiters/100.0
            val dataMileage = cursor.getInt(cursor.getColumnIndex(MyDbNameClass.COLUMN_NAME_MILEAGE))

            dates.add(dataDate)
            amounts.add(dataAmountDouble)
            liters.add(dataLitersDouble)
            mileages.add(dataMileage)
        }
        cursor.close()
        return MyDbData(dates, amounts, liters, mileages)
    }


    private fun convertDateToDbFormat(date: String): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val parsedDate = sdf.parse(date)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return outputFormat.format(parsedDate ?: return "")
    }
}


