package VasiliyGubenkov.Liters.DB

import android.provider.BaseColumns

object MyDbNameClass: BaseColumns {
    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "Main.db"
    const val TABLE_NAME = "MainTable"
    const val COLUMN_NAME_DATE = "Date"
    const val COLUMN_NAME_AMOUNT = "Amount"
    const val COLUMN_NAME_LITERS = "Liters"
    const val COLUMN_NAME_MILEAGE = "Mileage"
    const val CREATE_TABLE =
        "CREATE TABLE IF NOT EXISTS $TABLE_NAME" +
                "(" + "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME_DATE TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), " +
                "$COLUMN_NAME_AMOUNT INTEGER NOT NULL, " +
                "$COLUMN_NAME_LITERS INTEGER NOT NULL," +
                "$COLUMN_NAME_MILEAGE INTEGER NOT NULL)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}