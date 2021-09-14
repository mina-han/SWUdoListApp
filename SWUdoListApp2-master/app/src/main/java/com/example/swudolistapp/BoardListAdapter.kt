package com.example.swudolistapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BoardListAdapter(private val context: Context, private val dataList: ArrayList<BoardData>):
    RecyclerView.Adapter<BoardListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tv_title = view.findViewById<TextView>(R.id.tv_board_post_title)
        private val tv_context = view.findViewById<TextView>(R.id.tv_board_post_context)
        private val tv_created = view.findViewById<TextView>(R.id.tv_board_post_date)

        fun bind(boardData: BoardData, context: Context){
            tv_title.text = boardData.title
            tv_context.text = boardData.context
            tv_created.text = boardData.created

            itemView.setOnClickListener {
                Intent(context, PostActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("post", boardData)
                }.run { context.startActivity(this) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.board_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], context)
    }

}