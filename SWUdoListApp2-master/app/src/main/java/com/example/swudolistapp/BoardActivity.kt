package com.example.swudolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_select_subject.*
import kotlinx.android.synthetic.main.board_rv_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 게시판 액티비티
class BoardActivity : AppCompatActivity() {

    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCodeArr = arrayOf("MT01044", "MT01043", "MT01019")

    var subjectCode = ""

    companion object{
        var boardDataList = arrayListOf<BoardData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var addBoardService: MyAPI = retrofit.create(MyAPI::class.java)

        if (intent.hasExtra("subject")) {
            // 해당 게시글 과목코드 얻기
            subjectCode = subjectCodeArr.get(subjectArr.indexOf(intent.getStringExtra("subject")))
        } else {
            Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
        }

        btn_board_edit.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.board_dialog_item, null)




            builder.setView(dialogView)
                .setPositiveButton("등록"){ dialogInterface, i ->

                    val dialogTitle = dialogView.findViewById<EditText>(R.id.board_et_title).text.toString()
                    val dialogContext = dialogView.findViewById<EditText>(R.id.board_et_context).text.toString()
                    val author = sharedManager.getCurrentUser().id.toString()

                    Log.e("title", dialogTitle)
                    Log.e("context", dialogContext)

                    addBoardService.editPost(author, dialogTitle, dialogContext, subjectCode).enqueue(object:
                        Callback<EditItem> {
                        override fun onResponse(
                            call: Call<EditItem>,
                            response: Response<EditItem>
                        ) {
                            var register = response.body()
                            Log.d("EDIT", "msg: " + register?.created)

                            if (register != null) {
                                boardDataList.add(BoardData(author, subjectCode, dialogTitle, dialogContext, register.created))
                            }
                            setSubjectListView()
                        }

                        override fun onFailure(call: Call<EditItem>, t: Throwable) {
                            t.printStackTrace()
                        }

                    })
                }
                .setNegativeButton("취소") {dialogInterface, i ->
                    // 아무 액션 없음
                }
                .show()
        }
    }

    fun setSubjectListView(){
        val mAdapter = BoardListAdapter(this, boardDataList)
        rv_board.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_board.layoutManager = layout
        rv_board.setHasFixedSize(true)
    }

}