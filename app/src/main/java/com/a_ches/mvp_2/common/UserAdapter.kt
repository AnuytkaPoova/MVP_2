package com.a_ches.mvp_2.common

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.a_ches.mvp_2.R

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserHolder?>() {
        var data = ArrayList<User>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
            val view: View =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false)
            return UserHolder(view)
        }

        override fun onBindViewHolder(holder: UserHolder, position: Int) {
            holder.bind(data[position])
        }

        fun setData(users: List<User>) {
            data.clear()
            data.addAll(users)
            notifyDataSetChanged()
            Log.d("qweee", "size  = $itemCount")
        }

        class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var text: TextView
            fun bind(user: User) {
                text.setText(
                    String.format(
                        "id: %s, name: %s, email: %s",
                        user.id,
                        user.name,
                        user.email
                    )
                )
            }

            init {
                text = itemView.findViewById(R.id.text) as TextView
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

}