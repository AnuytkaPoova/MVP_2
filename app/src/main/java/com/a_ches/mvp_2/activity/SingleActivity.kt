package com.a_ches.mvp_2.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.database.Cursor
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a_ches.mvp_2.R
import com.a_ches.mvp_2.common.User
import com.a_ches.mvp_2.common.UserAdapter
import com.a_ches.mvp_2.common.UserTable
import com.a_ches.mvp_2.database.DbHelper
import java.util.*
import java.util.concurrent.TimeUnit

class SingleActivity : AppCompatActivity() {

    private var userAdapter: UserAdapter? = null
    private var dbHelper: DbHelper? = null
    private var editTextName: EditText? = null
    private var editTextEmail: EditText? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        init()
        loadUsers()
    }

    private fun init() {
        editTextName = findViewById(R.id.name) as EditText?
        editTextEmail = findViewById(R.id.email) as EditText?

        val buttonAdd: Button = findViewById(R.id.add)

        val onClickListener = buttonAdd.setOnClickListener {
            fun onClick(v: View?) {
                addUser()
            }
        }
        val buttonClear: Button = findViewById(R.id.clear)
        val onClickListener1 = buttonClear.setOnClickListener() {
            fun onClick(v: View?) {
                clearUsers()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        userAdapter = UserAdapter()
        val userList: RecyclerView = findViewById(R.id.list) as RecyclerView
        userList.setLayoutManager(layoutManager)
        userList.setAdapter(userAdapter)
        dbHelper = DbHelper(this)
    }

    private fun loadUsers() {
        val loadUsersTask: LoadUsersTask = LoadUsersTask()
        loadUsersTask.execute()
    }

    private fun addUser() {
        val name: String = editTextName?.getText().toString()
        val email: String = editTextEmail?.getText().toString()
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, R.string.empty_values, Toast.LENGTH_SHORT).show()
            return
        }
        val cv = ContentValues(2)
        cv.put(UserTable.COLUMN.NAME, name)
        cv.put(UserTable.COLUMN.EMAIL, email)
        showProgress()
        val addUserTask: AddUserTask = AddUserTask()
        addUserTask.execute(cv)
    }

    private fun clearUsers() {
        showProgress()
        val clearUsersTask: ClearUsersTask = ClearUsersTask()
        clearUsersTask.execute()
    }

    private fun showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait))
    }

    private fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    internal inner class LoadUsersTask : AsyncTask<Void?, Void?, List<User?>?>() {
        @SuppressLint("Range")
        override fun doInBackground(vararg params: Void?): List<User> {
            val users: List<User> = LinkedList()
            val cursor: Cursor = dbHelper!!.getReadableDatabase()
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

        fun onPostExecute(users: List<User>) {
            userAdapter?.setData(users)
        }
    }

    internal inner class AddUserTask : AsyncTask<ContentValues?, Void?, Void?>() {
        override fun doInBackground(vararg params: ContentValues?): Void? {
            val cvUser: ContentValues? = params[0]
            dbHelper?.getWritableDatabase()?.insert(UserTable.TABLE, null, cvUser)
            try {
                TimeUnit.SECONDS.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            hideProgress()
            loadUsers()
        }
    }

    internal inner class ClearUsersTask : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            dbHelper?.getWritableDatabase()?.delete(UserTable.TABLE, null, null)
            try {
                TimeUnit.SECONDS.sleep(1)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            hideProgress()
            loadUsers()
        }
    }

}