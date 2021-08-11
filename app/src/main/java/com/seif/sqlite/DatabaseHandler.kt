package com.seif.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.seif.sqlite.models.EmpModelClass

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) : SQLiteOpenHelper(context,
        DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        // change by upgrading database
        private const val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        // Table name
        private val TABLE_CONTACTS = "EmployeeTable"
        // Primary keys
        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
    }

    // it will be called once the database created
    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        // CREATE TABLE (_id INTEGER PRIMARY KEY, name TEXT, email TEXT)
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        // Execute sql command
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // delete old table then create the new one
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    /**
     * Function to insert data
     */
    fun addEmployee(emp: EmpModelClass): Long {
        // to write in the database
        val db = this.writableDatabase
        // content values that we need to put in our database (As container)
        // ArrayMap<String, Object> mMap;
        // public ContentValues() {
        //        mMap = new ArrayMap<>();
        //    }
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.email) // EmpModelClass Email

        // Inserting employee details using insert query.
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success // if something went wrong with inserting it will return -1
    }

    //Method to read the records from database in form of ArrayList
    fun viewEmployee(): ArrayList<EmpModelClass> {

        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        // Query to select all the records from the table.
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        // Cursor is used to read the record one by one. Add them to data model class.
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndex(KEY_EMAIL))

                val emp = EmpModelClass(id, name, email)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    /**
     * Function to update record (row)
     */
    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.email) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    /**
     * Function to delete record (row)
     */
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass id
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

}