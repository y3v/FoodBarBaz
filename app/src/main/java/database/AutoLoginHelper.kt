package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class AutoLoginHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 4) {


    override fun onCreate(db: SQLiteDatabase) {
        val query = "create table $TABLE_NAME(ID integer primary key autoincrement, user_id integer, username text, password test, fname text, lname text, email text)"
        Log.i("DATABASE:::", "CREATED")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(query)
        onCreate(db)
    }

    fun getData () : Cursor {
        val db = this.writableDatabase
        val result = db.rawQuery("select * from $TABLE_NAME", null)

        return result
    }

    fun insertEntry(id : Long, username: String, password: String, fname: String, lname: String, email: String): Boolean {
        var ret = false

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("user_id", id)
        contentValues.put("username", username)
        contentValues.put("password", password)
        contentValues.put("fname", fname)
        contentValues.put("lname", lname)
        contentValues.put("email", email)
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

    fun updateEntry(note: String, newNote: String): Boolean {
        var ret = false

        val db = this.writableDatabase
        val content = ContentValues()
        //content.put(QUERY, newNote)
        val res = db.update(TABLE_NAME, content, "query ='$note';", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    companion object {

        val DATABASE_NAME = "foodbarbaz"
        val TABLE_NAME = "auto_login"
        val ID = "id"
    }
}
