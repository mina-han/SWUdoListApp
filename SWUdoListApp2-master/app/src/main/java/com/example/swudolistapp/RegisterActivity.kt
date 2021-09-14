package com.example.swudolistapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterActivity : AppCompatActivity() {

    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    var isOk: Boolean = true
    var register: PostItem? = null
    var subjectStr = ""
    var subjectArr = arrayOf("JAVA프로그래밍기초", "C++프로그래밍기초", "자료구조")
    var subjectCode = arrayOf("MT01044", "MT01043" ,"MT01019")
//    선택한 과목 코드 저장
    var selectItems = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        var registerService: MyAPI = retrofit.create(MyAPI::class.java)

        // 과목 선택 팝업 창
        btn_select_subject.setOnClickListener {
            selectItems.clear()
            var builder = AlertDialog.Builder(this)
            builder.setTitle("과목 선택")

            builder.setMultiChoiceItems(
                subjectArr,
                null,
                object: DialogInterface.OnMultiChoiceClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int, isChecked: Boolean) {
                        subjectStr = ""
                        if(isChecked){
                            selectItems.add(subjectCode[which])
                        }
                        else if(selectItems.contains(subjectCode[which])){
                            selectItems.remove(subjectCode[which])
                        }
                    }
                }
            )

            var listener = object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    for(item in selectItems){
                        print(item)
                        subjectStr += item + " "
                    }
                    Log.e("subject List", subjectStr)
                }
            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", null)
            builder.show()
        }

//        회원가입 실행
        btn_register.setOnClickListener{

            var reg_id = et_register_id.text.toString()
            var reg_pw = et_register_pw.text.toString()
            var reg_email = et_register_email.text.toString()

            // 넘어갈 수 있도록 조건 검사
            canSubmitForm()
            if(isOk){
                // 회원가입 가능 조건 충족 시 - 회원가입
                registerService.requestRegister(reg_id, reg_pw, reg_email, subjectStr).enqueue(object: Callback<PostItem>{
                    override fun onFailure(call: Call<PostItem>, t: Throwable) {
                        Log.e("SIGNIN - Error" , t.message)
                    }

                    override fun onResponse(call: Call<PostItem>, response: Response<PostItem>) {
                        register = response.body()
                        Log.d("SIGNIN", "msg: " + register?.msg)
                        Log.d("SIGNIN", "msg: " + register?.code)
                    }
                })

                val currentUser = User().apply {
                    id = reg_id.trim()
                    pw = reg_pw.trim()
                    email = reg_email.trim()
                    subjects = subjectStr.trim()
                }
                sharedManager.saveCurrentUser(currentUser)


                // 메인 화면으로 이동 - 이후 선택 화면으로 이동? 아니면 팝업으로 선택한 후에 이동하도록 할까?
                val intent = Intent(this@RegisterActivity, SelectSubjectActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 회원 가입 폼을 제대로 작성했는지 확인.
    private fun canSubmitForm(){
        if (et_register_id.text.toString().equals("")){
            // id 입력란이 비어 있을 때
            et_register_id.setError("아이디 입력 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }
        if (et_register_pw.text.toString().equals("")){
            // pw 입력란이 비어 있을 때
            et_register_pw.setError("비밀번호 입력 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }
        if (et_register_pw_confirm.text.toString().equals("")){
            // pw_confirm 입력란이 비어 있을 때
            et_register_pw_confirm.setError("비밀번호 확인 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }
        if (et_register_email.text.toString().equals("")){
            // email 입력란이 비어 있을 때
            et_register_email.setError("이메일 입력 란은 필수로 입력하셔야 합니다.")
            isOk = false
        }

        if (!(et_register_pw.text.toString().equals(et_register_pw_confirm.text.toString()))){
            // pw != pw_confirm
            et_register_pw_confirm.setError("입력한 비밀번호와 비밀번호 확인이 일치하지 않습니다.")
            isOk = false
        }

        if (subjectStr.equals("")){
            Log.e("SELECT", subjectStr)
            btn_select_subject.setError("과목은 1개 이상 선택해야 합니다.")
            isOk = false
        }
    }
}