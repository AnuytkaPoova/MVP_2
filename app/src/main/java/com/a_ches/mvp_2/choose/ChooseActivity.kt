package com.a_ches.mvp_2.choose

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.a_ches.mvp_2.R
import com.a_ches.mvp_2.activity.SingleActivity
import com.a_ches.mvp_2.mvp.UsersActivity

class ChooseActivity: AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_choose)

            val buttonActivity: Button = findViewById(R.id.activity)
            buttonActivity.setOnClickListener { view: View? ->
                    startActivity(Intent(this@ChooseActivity, SingleActivity::class.java))

            }

            val buttonMvp: Button = findViewById(R.id.mvp)
            buttonMvp.setOnClickListener { view: View? ->
                    startActivity(Intent(this@ChooseActivity, UsersActivity::class.java))

            }
        }

}