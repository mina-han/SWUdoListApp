package com.example.swudolistapp

import android.content.Context
import android.content.SharedPreferences
import com.example.swudolistapp.PreferenceHelper.set
import com.example.swudolistapp.PreferenceHelper.get

// SharedManager를 통해 SharedPreference를 이용

class SharedManager(context: Context) {

    // private 추가 해야 하는데 시간이 없어서 미안,, - SelectSubjectActivity 에서 prefs.edit 사용 -> 이후 함수로 정의하면 될 듯.. 시간나면 하자
    val prefs: SharedPreferences = PreferenceHelper.defultPrefs(context)

    fun saveCurrentUser(user: User){
        prefs["id"] = user.id
        prefs["pw"] = user.pw
        prefs["email"] = user.email
        prefs["subjects"] = user.subjects
    }

    fun getCurrentUser(): User{
        return User().apply {
            id = prefs["id", ""]
            pw = prefs["pw", ""]
            email = prefs["email", ""]
            subjects = prefs["subjects", ""]
        }
    }
}