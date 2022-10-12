package ren.tianh.hwa2_tr_mod5

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /*This database is only a cache for online data, so it's upgrade
        policy is to simply discard the data and start over*/
        db!!.execSQL(SQL_DELETE_ENTRIES)

        //recreate the database
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //override method, super class has to be called
        super.onDowngrade(db, oldVersion, newVersion)
    }
    fun insertUser(user: UserModel): Boolean{

        //get db repo in write mode
        val db = writableDatabase

        //create new map of values, where column names are the keys
        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_FULL_NAMES,user.fullUserNames)
        values.put(DBContract.UserEntry.COLUMN_USERNAME, user.username)
        values.put(DBContract.UserEntry.COLUMN_USER_ID, user.userId)
        values.put(DBContract.UserEntry.COLUMN_PASSWORD, user.password)

        //insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME,null,values)

        return true
    }

    fun deleteUser(userId: String): Boolean{
        //gets data repo in write mode
        val db = writableDatabase

        //define where part of query
        val selection = DBContract.UserEntry.COLUMN_USER_ID + "LIKE ?"

        //specify arguments in placeholder order
        val selectionArgs = arrayOf(userId)

        //issue sql statement
        db.delete(DBContract.UserEntry.TABLE_NAME,selection,selectionArgs)

        return true
    }

    fun readUserDetails(userName: String): ArrayList<UserModel>{

        //initialize an arrayList to hold the user details
        val users = ArrayList<UserModel>()

        //obtain an instance of a writable database
        val db = writableDatabase

        //create cursor to hold user details from db
        var cursor: Cursor? = null

        try{
            //the query to obtain user details
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_USERNAME + " = '" + userName + "'",null)
        }catch (e: SQLException){
            //if table not yet present, create
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        //instantiate required variables
        var fullNames: String
        var userName: String
        var password: String
        var userId: String

        //iterate through the cursor and assign data to variables
        if (cursor.moveToFirst()){
                fullNames = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_FULL_NAMES))
                userName = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_USERNAME))
                password = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_PASSWORD))
                userId = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.UserEntry.COLUMN_USER_ID))

                users.add(UserModel(userId,fullNames,userName,password))
                cursor.moveToNext()
            }
        //return an array list of the use details
        return users
    }

    companion object{
        //if you change the database schema, you must increment the database version
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Users.db"


        //variable used in creating the user table
        private const val SQL_CREATE_ENTRIES = "Create table " + DBContract.UserEntry.TABLE_NAME + " (" +
                DBContract.UserEntry.COLUMN_USER_ID + " TEXT PRIMARY KEY," +
                DBContract.UserEntry.COLUMN_FULL_NAMES + " TEXT," +
                DBContract.UserEntry.COLUMN_USERNAME + " TEXT," +
                DBContract.UserEntry.COLUMN_PASSWORD + " TEXT)"

        //variable used in recreating the user table
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME
    }
}