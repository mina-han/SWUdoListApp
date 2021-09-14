package com.example.swudolistapp

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_select_subject.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SelectSubjectActivity : AppCompatActivity() {

    // sharedPreference
    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    // retrofit
    var retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 요청 서비스
    var updateSubjectService: MyAPI = retrofit.create(MyAPI::class.java)
    // 로그아웃 서비스
    var logoutService: MyAPI = retrofit.create(MyAPI::class.java)

    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCode = arrayOf("MT01044", "MT01043", "MT01019")
    var selectItems = ArrayList<String>(subjectCode.size)
    var subjectStr = ""
    var checked: BooleanArray = BooleanArray(subjectCode.size)

    var subjectList: ArrayList<SubjectListData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_subject)

        // 초반 selectItems 설정 및 subjectList 설정
        setSubjectList()

        // SubjectList 설정 - subjectListAdapter, List 화면에 나오도록 함.
        setSubjectListView()

    }

//    옵션 메뉴 생성
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    // 선택 과목 변경
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 과목 변경
            R.id.menu_my_change_subject -> {
                // 과목 변경
                setChecked()
                var builder = AlertDialog.Builder(this)
                builder.setTitle("과목 선택")
                builder.setMultiChoiceItems(
                    subjectArr,
                    checked,
                    object : DialogInterface.OnMultiChoiceClickListener {
                        override fun onClick(
                            dialog: DialogInterface?,
                            which: Int,
                            isChecked: Boolean
                        ) {
                            if (isChecked) {
                                selectItems.add(subjectCode[which])
                                checked[which] = true
                            } else if (selectItems.contains(subjectCode[which])) {
                                selectItems.remove(subjectCode[which])
                                checked[which] = false
                            }
                        }
                    }
                )

                var listener = object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        subjectStr = ""
                        for (item in selectItems) {
                            subjectStr += item + ' '
                        }

                        // 변경된 subjects prefs에 저장
                        sharedManager.prefs.edit().putString("subjects", subjectStr).apply()

                        // 변경된 내용을 selectItems에 적용
                        stringToArrayList(sharedManager.getCurrentUser().subjects.toString())

                        updateSubjectService.requestChange(sharedManager.getCurrentUser().id.toString(),
                            sharedManager.getCurrentUser().subjects.toString()).enqueue(object: Callback<PostItem>{
                            override fun onFailure(call: Call<PostItem>, t: Throwable) {
                                t.printStackTrace()
                            }

                            override fun onResponse(
                                call: Call<PostItem>,
                                response: Response<PostItem>
                            ) {
                                var update = response.body()
                                Log.d("Login", "msg: " + update?.msg)
                                Log.d("Login", "code: " + update?.code)
                            }
                            }
                        )
                        setSubjectList()
                        setSubjectListView()

                    }
                }
                builder.setPositiveButton("확인", listener)
                builder.setNegativeButton("취소", null)
                builder.show()

            }
            // 로그아웃
            R.id.menu_my_logout -> {
                var id = sharedManager.getCurrentUser().id.toString()
                Log.e("logout id" , id)
                logoutService.requestLogout(id).enqueue(object : Callback<PostItem>{
                    override fun onFailure(call: Call<PostItem>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                        var logout = response.body()
                        Log.d("Logout", "msg: " + logout?.msg)
                        Log.d("Logout", "code: " + logout?.code)

                        // sharedPreference 비우기
                        val currentUser = User().apply {
                            id = ""
                            pw = ""
                            email = ""
                            subjects = ""
                        }
                        sharedManager.saveCurrentUser(currentUser)

                        val intent = Intent(this@SelectSubjectActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // String To ArrayList - String으로 받은 subjects를 ArrayList로 바꿈
    fun stringToArrayList(s: String) {

        selectItems.clear()
        var str = s.trim().splitToSequence(" ")
            .filter { it.isNotEmpty() }
            .toList()

        for (item in str) {
            if (!selectItems.contains(item)) {
                selectItems.add(item.toString())
            }
        }
    }

    // 선택된 과목 담기
    fun setChecked(): BooleanArray {
        var i = 0
        // 선택한 코드 checked 상태 설정
        for (item in subjectCode) {
            if (selectItems.contains(item)) {
                checked.set(i, value = true)
            } else {
                checked.set(i, value = false)
            }
            i++
        }
        return checked
    }

    // List 만들기
    fun setSubjectList(){
        // subjects 리스트 불러오기
        subjectList.clear()
        stringToArrayList(sharedManager.getCurrentUser().subjects.toString())

        for (item in selectItems){
            subjectList.add(SubjectListData(subjectArr[subjectCode.indexOf(item)], item))
        }
    }

    fun setSubjectListView(){
        val mAdapter = SubjectListAdapter(this, subjectList)
        rv_select_sub.adapter = mAdapter

        val layout = LinearLayoutManager(this)
        rv_select_sub.layoutManager = layout
        rv_select_sub.setHasFixedSize(true)
    }

}