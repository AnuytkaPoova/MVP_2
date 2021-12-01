package com.a_ches.mvp_2.mvp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.os.AsyncTask
import com.a_ches.mvp_2.common.User
import com.a_ches.mvp_2.common.UserTable
import com.a_ches.mvp_2.database.DbHelper
import java.util.*
import java.util.concurrent.TimeUnit

class UsersModel(dbHelper: DbHelper) {

    private val dbHelper: DbHelper

    fun loadUsers(callback: LoadUserCallback?) {
        val loadUsersTask: LoadUsersTask = LoadUsersTask(callback)
        loadUsersTask.execute()
    }

    fun addUser(contentValues: ContentValues?, callback: CompleteCallback?) {
        val addUserTask: AddUserTask = AddUserTask(callback)
        addUserTask.execute(contentValues)
    }

    fun clearUsers(completeCallback: CompleteCallback?) {
        val clearUsersTask: ClearUsersTask = ClearUsersTask(completeCallback)
        clearUsersTask.execute()
    }

    interface LoadUserCallback {
        fun onLoad(users: List<User>)
    }

    interface CompleteCallback {
        fun onComplete()
    }

    internal inner class LoadUsersTask(private val callback: LoadUserCallback?) :
        AsyncTask<Void?, Void?, List<User>>() {
        @SuppressLint("Range")
        override fun doInBackground(vararg params: Void?): List<User> {
            val users: List<User> = LinkedList()
            val cursor: Cursor = dbHelper.getReadableDatabase()
                .query(UserTable.TABLE, null, null, null, null, null, null)
            while (cursor.moveToNext()) {
                val user = User()
                user.id = cursor.getLong(cursor.getColumnIndex(UserTable.COLUMN.ID))
                user.name = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.NAME))
                user.email = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN.EMAIL))
                users.plus(user)

            }
            cursor.close()
            return users
        }

        override fun onPostExecute(users: List<User>) {
            callback?.onLoad(users)
        }
    }

    internal inner class AddUserTask(private val callback: CompleteCallback?) :
        AsyncTask<ContentValues?, Void?, Void?>() {
        override fun doInBackground(vararg params: ContentValues?): Void? {
            val cvUser: ContentValues? = params[0]
            dbHelper.getWritableDatabase().insert(UserTable.TABLE, null, cvUser)
            try {
                TimeUnit.SECONDS.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            callback?.onComplete()
        }
    }

    internal inner class ClearUsersTask(private val callback: CompleteCallback?) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            dbHelper.getWritableDatabase().delete(UserTable.TABLE, null, null)
            try {
                TimeUnit.SECONDS.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            callback?.onComplete()
        }
    }

    init {
        this.dbHelper = dbHelper
    }
}