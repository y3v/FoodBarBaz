package database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class FavouritesHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 7) {


    override fun onCreate(db: SQLiteDatabase) {
        println("CREATING DATABASE!")
        val query = "create table fav(ID integer primary key autoincrement, name text, user_id integer)"
        Log.i("DATABASE:::", "CREATED")
        val ret = db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        val query = "DROP TABLE IF EXISTS fav"
        println("TABLE UPDATED")
        db.execSQL(query)
        onCreate(db)
    }

    fun getData (id : Long) : Cursor {
        val db = this.writableDatabase
        val result = db.rawQuery("select * from $TABLE_NAME where user_id=$id", null)

        return result
    }

    fun insertEntry(name: String, id : Long): Boolean {
        var ret = false

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(RNAME, name)
        contentValues.put("user_id", id)
        val res = db.insert(TABLE_NAME, null, contentValues)

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    fun deleteEntry(name: String, id: Long): Boolean {
        var ret = false

        val db = this.writableDatabase
        val res = db.delete(TABLE_NAME, "name = '$name' and user_id=$id;", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    fun updateEntry(note: String, newNote: String): Boolean {
        var ret = false

        val db = this.writableDatabase
        val content = ContentValues()
        content.put(RNAME, newNote)
        val res = db.update(TABLE_NAME, content, "query ='$note';", null).toLong()

        if (res != -1L) {
            ret = true
        }

        return ret
    }

    companion object {

        val DATABASE_NAME = "foodbarbaz"
        val TABLE_NAME = "fav"
        val ID = "id"
        val RNAME = "name"
    }
}
