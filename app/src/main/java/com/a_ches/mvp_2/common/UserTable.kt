package com.a_ches.mvp_2.common

object UserTable {

    const val TABLE = "users"
    val CREATE_SCRIPT = String.format(
        "create table %s ("
                + "%s integer primary key autoincrement,"
                + "%s text,"
                + "%s text" + ");",
        TABLE, COLUMN.ID, COLUMN.NAME, COLUMN.EMAIL
    )

    object COLUMN {
        const val ID = "_id"
        const val NAME = "name"
        const val EMAIL = "email"
    }

}