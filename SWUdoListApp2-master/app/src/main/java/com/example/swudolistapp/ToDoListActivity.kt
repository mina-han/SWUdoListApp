package com.example.swudolistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_select_subject.*
import kotlinx.android.synthetic.main.activity_to_do_list.*
import kotlinx.android.synthetic.main.to_do_list_dialog_item.*
import kotlinx.android.synthetic.main.to_do_list_rv_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.typeOf

// ToDoList 액티비티
class ToDoListActivity : AppCompatActivity() {

    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCodeArr = arrayOf("MT01044", "MT01043", "MT01019")

    var subjectCode: String = ""

    var count = 0

    companion object{
        var todoList = arrayListOf<ToDoListData>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)

        var retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var addTDLService: MyAPI = retrofit.create(MyAPI::class.java)


        if (intent.hasExtra("subject")) {
            // 해당 게시글 과목코드 얻기
            subjectCode = subjectCodeArr.get(subjectArr.indexOf(intent.getStringExtra("subject")))
        } else {
            Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
        }

        // todolist 표시
        addTDLService.getTodolist(subjectCode).enqueue(object : Callback<List<ToDoListData>>{
            override fun onResponse(
                call: Call<List<ToDoListData>>,
                response: Response<List<ToDoListData>>
            ) {
                var datas = response.body()
                if (datas != null) {
                    for (data in datas){
                        if(todoList.contains(ToDoListData(data.context, false)) || todoList.contains(ToDoListData(data.context, true))){
                            continue
                        }else{
                            todoList.add(ToDoListData(data.context, false))
                        }
                    }
                }
                setToDoListView()
            }

            override fun onFailure(call: Call<List<ToDoListData>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // todolist 추가
        btn_add_tdl.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.to_do_list_dialog_item, null)
            val dialogText = dialogView.findViewById<EditText>(R.id.tdl_dialog_context)

            builder.setView(dialogView)
                .setPositiveButton("등록"){ dialogInterface, i ->
                    addTDLService.addTodolist(subjectCode, dialogText.text.toString()).enqueue(object: Callback<PostItem>{
                        override fun onResponse(
                            call: Call<PostItem>,
                            response: Response<PostItem>
                        ) {
                            var register = response.body()
                            Log.d("ADD-LIST", "msg: " + register?.msg)
                            Log.d("ADD-LIST", "msg: " + register?.code)

                            todoList.add(ToDoListData(dialogText.text.toString(), false))
                            for(list in todoList){
                                Log.e("tdl", list.context)
                            }
                            setToDoListView()
                        }

                        override fun onFailure(call: Call<PostItem>, t: Throwable) {
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

    // List 만들기

    fun setToDoListView(){
        val mAdapter = ToDoListAdapter(this, todoList)
        rv_tdl.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_tdl.layoutManager = layout
        rv_tdl.setHasFixedSize(true)
    }
}