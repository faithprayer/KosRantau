package com.example.login

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.login.room.User
import com.example.login.room.UserDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences : SharedPreferences? = null
    val db by lazy { UserDB(this) }
    private val userDAO = db.userDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        btnEditProfile.setOnClickListener(){
            val username: String = findViewById<View?>(R.id.edUsername).toString()
            val password: String = findViewById<View?>(R.id.edPassword).toString()
            val email: String = findViewById<View?>(R.id.edEmail).toString()
            val tglLahir: String = findViewById<View?>(R.id.edTglLahir).toString()
            val noTelp: String = findViewById<View?>(R.id.edNoTelp).toString()

            CoroutineScope(Dispatchers.IO).launch {
                val user = User(0, username, password, tglLahir, email, noTelp)
                userDAO.updateUser(user)

                replaceFragment(ProfileFragment())
            }
        }

    }
    fun replaceFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_graph,fragment)
        fragmentTransaction.commit()
    }
}