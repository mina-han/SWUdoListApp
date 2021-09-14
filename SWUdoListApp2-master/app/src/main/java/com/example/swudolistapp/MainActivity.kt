package com.example.swudolistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var subjectName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 전체적인 게시판+미션 친구들 보이는 곳

        if (intent.hasExtra("subject_name")) {
            // 해당 게시글 과목명 얻기
            subjectName = intent.getStringExtra("subject_name")
            tv_main_subject_name.text = subjectName
        } else {
            Toast.makeText(applicationContext, "해당 과목 data가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        // to-do-list 더보기
        btn_more_tdl.setOnClickListener {
            val intent = Intent(this@MainActivity, ToDoListActivity::class.java)
            intent.putExtra("subject", subjectName)
            startActivity(intent)
        }

        // 게시판 더보기
        btn_more_post.setOnClickListener {
            val intent = Intent(this@MainActivity, BoardActivity::class.java)
            intent.putExtra("subject", subjectName)
            startActivity(intent)
        }
        
    }
}