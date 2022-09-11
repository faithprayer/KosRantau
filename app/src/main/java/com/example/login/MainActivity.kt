package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var usernameInput : TextInputEditText
    private lateinit var PasswordInput : TextInputEditText
    lateinit var mBundle: Bundle

    var vUsername: String =""
    lateinit var vNoHandphone: String
    lateinit var vEmail: String
    lateinit var vTglLahir:String
    var vPassword: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.loginLayout)

        usernameInput = findViewById(R.id.TextInputUsername)
        PasswordInput = findViewById(R.id.textInputPassword)
        val btnLogin: Button = findViewById(R.id.btnLogin)
        val moveReg: TextView = findViewById(R.id.textMoveRegister)

        getBundle()

        btnLogin.setOnClickListener (View.OnClickListener{
            var checkLogin = false
            inputUsername.setError(null)
            inputPassword.setError(null)
            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if (username.isEmpty()) {
                inputUsername.setError("Username must be filled with text")
                checkLogin = false
            }

            if (password.isEmpty()) {
                inputPassword.setError("Password must be filled with text")
                checkLogin = false
            }

            if (username == "admin" && password == "admin") {
                checkLogin = true
            }
            if(intent.getBundleExtra("register") !=null){
                if(username == vUsername && password == vPassword){
                    checkLogin =true
                }
            }
            if(!checkLogin)return@OnClickListener
            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(moveHome)
        })

        moveReg.setOnClickListener(View.OnClickListener{
            val moverReg = Intent(this, RegisterActivity::class.java)
            startActivity(moverReg)
        })
    }

    fun getBundle() {
        if(intent.getBundleExtra("register") != null){
            mBundle = intent.getBundleExtra("register")!!
            vUsername = mBundle.getString("nama")!!
            vNoHandphone = mBundle.getString("noHandphone")!!
            vEmail = mBundle.getString("email")!!
            vTglLahir = mBundle.getString("tanggalLahir")!!
            vPassword = mBundle.getString("password")!!
            usernameInput.setText(vUsername)
            PasswordInput.setText(vPassword)
        }

    }

}