package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class HistoryHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 3) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = "create table history(ID integer primary key autoincrement, query text, user_id integer)"
        Log.i("DATABASE:::", "CREATED")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        val query = "DROP TABLE IF EXISTS history"
        db.execSQL(query)
        onCreate(db)
    }

    fun getData (id : Long) : Cursor {
        val db = this.writableDatabase
        val result = db.rawQuery("select * from $TABLE_NAME where user_id=$id", null)

        return result
    }

    fun insertQuery(query: String, id : Long): Boolean {
        var ret = false

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(QUERY, query)
        contentValues.put("user_id", id)
        val res = db.insert(TABLE_NAME, null, contentValues)

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    fun deleteEntry(query: String, id: Long): Boolean {
        var ret = false

        val db = this.writableDatabase
        val res = db.delete(TABLE_NAME, "query = '$query' and user_id=$id;", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    fun updateEntry(note: String, newNote: String): Boolean {
        var ret = false

        val db = this.writableDatabase
        val content = ContentValues()
        content.put(QUERY, newNote)
        val res = db.update(TABLE_NAME, content, "query ='$note';", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    companion object {

        val DATABASE_NAME = "foodbarbaz"
        val TABLE_NAME = "history"
        val ID = "id"
        val QUERY = "query"
    }
}
