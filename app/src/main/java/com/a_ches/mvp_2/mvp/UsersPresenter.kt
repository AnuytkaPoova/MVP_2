package com.a_ches.mvp_2.mvp

import android.content.ContentValues
import android.text.TextUtils
import com.a_ches.mvp_2.R
import com.a_ches.mvp_2.common.User
import com.a_ches.mvp_2.common.UserTable

class UsersPresenter (private val model: UsersModel) {
    private var view: UsersActivity? = null
    fun attachView(usersActivity: UsersActivity?) {
        view = usersActivity
    }

    fun detachView() {
        view = null
    }

    fun viewIsReady() {
        loadUsers()
    }

    fun loadUsers() {
        model.loadUsers(object : UsersModel.LoadUserCallback {
            override fun onLoad(users: List<User>) {
                view!!.showUsers(users)
            }
        })
    }

    fun add() {
        val userData = view!!.userData
        if (TextUtils.isEmpty(userData.name) || TextUtils.isEmpty(userData.email)) {
            view!!.showToast(R.string.empty_values)
            return
        }
        val cv = ContentValues(2)
        cv.put(UserTable.COLUMN.NAME, userData.name)
        cv.put(UserTable.COLUMN.EMAIL, userData.email)
        view!!.showProgress()
        model.addUser(cv, object : UsersModel.CompleteCallback {
            override fun onComplete() {
                view!!.hideProgress()
                loadUsers()
            }
        })
    }

    fun clear() {
        view!!.showProgress()
        model.clearUsers(object : UsersModel.CompleteCallback {
            override fun onComplete() {
                view!!.hideProgress()
                loadUsers()
            }
        })
    }
}