package com.a_ches.mvp_2.mvp

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.a_ches.mvp_2.R
import com.a_ches.mvp_2.common.User
import com.a_ches.mvp_2.common.UserAdapter
import com.a_ches.mvp_2.database.DbHelper

class UsersActivity: AppCompatActivity() {


    private var userAdapter: UserAdapter? = null
    private var editTextName: EditText? = null
    private var editTextEmail: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var presenter: UsersPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        init()

    }

    private fun init() {
        editTextName = findViewById(R.id.name) as EditText?
        editTextEmail = findViewById(R.id.email) as EditText?

        val buttonAdd: Button = findViewById(R.id.add)
        val onClickListener = buttonAdd.setOnClickListener{
            fun onClick(v: View?) {
                presenter!!.add()
            }
        }

        val buttonClear: Button = findViewById(R.id.clear)
        val onClickListener1 = buttonClear.setOnClickListener() {
            fun onClick(v: View?) {
                presenter!!.clear()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        userAdapter = UserAdapter()
        val userList: RecyclerView = findViewById(R.id.list) as RecyclerView
        userList.setLayoutManager(layoutManager)
        userList.setAdapter(userAdapter)
        val dbHelper = DbHelper(this)
        val usersModel = UsersModel(dbHelper)
        presenter = UsersPresenter(usersModel)
        presenter!!.attachView(this)
        presenter!!.viewIsReady()
    }

    val userData: UserData
        get() {
            val userData = UserData()
            userData.name = editTextName?.getText().toString()
            userData.email = editTextEmail?.getText().toString()
            return userData
        }

    fun showUsers(users: List<User>) {
        userAdapter?.setData(users)
    }

    fun showToast(resId: Int) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
    }

    fun showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait))
    }

    fun hideProgress() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter!!.detachView()
    }
}