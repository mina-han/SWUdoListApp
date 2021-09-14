package com.example.swudolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class PostActivity : AppCompatActivity() {

    lateinit var postItem: BoardData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        if(intent.hasExtra("post")){
            postItem = intent.getParcelableExtra<BoardData>("post")
        }

        Log.e("author", postItem.author)
        Log.e("subject", postItem.subject)
        Log.e("title", postItem.title)
        Log.e("context", postItem.context)
        Log.e("created", postItem.created.toString())

    }
}