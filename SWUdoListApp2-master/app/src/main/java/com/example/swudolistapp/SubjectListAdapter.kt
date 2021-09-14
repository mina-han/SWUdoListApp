package com.example.swudolistapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SubjectListAdapter(private val context: Context, private val dataList: ArrayList<SubjectListData>):
    RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val subjectName = view.findViewById<TextView>(R.id.tv_rv_name)
        private val subjectCode = view.findViewById<TextView>(R.id.tv_rv_code)

        fun bind(subjectListData: SubjectListData, context: Context){
            subjectName.text = subjectListData.subjectName
            subjectCode.text = subjectListData.subjectCode

            itemView.setOnClickListener {
                Intent(context, MainActivity::class.java).apply {
                    putExtra("subject_name", subjectListData.subjectName)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.select_subject_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

}