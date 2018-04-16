package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class ThemeHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 7) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = "create table theme(ID integer primary key autoincrement, query text, user_id integer)"
        Log.i("DATABASE THEME:::", "CREATED")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        val query = "DROP TABLE IF EXISTS theme"
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

    fun deleteEntry(id: Long): Boolean {
        var ret = false

        val db = this.writableDatabase
        val res = db.delete(TABLE_NAME, "user_id=$id;", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    fun updateEntry(id: Long, newNote: String): Boolean {
        var ret = false

        val db = this.writableDatabase
        val content = ContentValues()
        content.put(QUERY, newNote)
        val res = db.update(TABLE_NAME, content, "user_id=$id;", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    companion object {

        val DATABASE_NAME = "foodbarbaz_theme"
        val TABLE_NAME = "theme"
        val ID = "id"
        val QUERY = "query"
    }
}
