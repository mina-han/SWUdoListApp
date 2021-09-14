package com.example.swudolistapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(private val context: Context, private val dataList: ArrayList<ToDoListData>):
    RecyclerView.Adapter<ToDoListAdapter.ViewHolder>() {

    private var ck = 0

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tdlCheckBox = view.findViewById<CheckBox>(R.id.tdl_cb_rv)

        fun bind(toDoList: ToDoListData, num: Int){
            tdlCheckBox.text = toDoList.context
            tdlCheckBox.isChecked = toDoList.checked

            tdlCheckBox.setOnClickListener {
                toDoList.checked = tdlCheckBox.isChecked
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.to_do_list_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], position)
    }

}
