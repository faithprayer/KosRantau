package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.login.databinding.ActivityMainBinding
import com.example.login.databinding.ActivityRegisterBinding
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputusername: TextInputLayout
    private lateinit var inputpassword: TextInputLayout
    private lateinit var inputemail: TextInputLayout
    private lateinit var inputtanggalLahir: TextInputLayout
    private lateinit var inputnomorhandphone: TextInputLayout
    private lateinit var btnRegister: Button
    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moveLogin: TextView = findViewById(R.id.textMoveLogin)

        binding.btnRegistrasi.setOnClickListener(View.OnClickListener  {

            var checkRegis = false

            val username: String = binding.inputLayoutRegUsername.getEditText()?.getText().toString()
            val nohandphone: String = binding.inputLayoutNomorHandphone.getEditText()?.getText().toString()
            val email: String = binding.inputLayoutEmail.getEditText()?.getText().toString()
            val tanggalLahir: String = binding.inputLayoutTanggalLahir.getEditText()?.getText().toString()
            val password: String = binding.inputLayoutPassword.getEditText()?.getText().toString()
            val mBundle = Bundle()

            mBundle.putString("nama", username)
            mBundle.putString("noHandphone", nohandphone)
            mBundle.putString("email", email)
            mBundle.putString("tanggalLahir", tanggalLahir)
            mBundle.putString("password", password)

            if (username.isEmpty()) {
                inputusername.setError("Username must be filled with text")
                checkRegis = false
            }

            if (nohandphone.isEmpty()) {
                inputnomorhandphone.setError("Password must be filled with text")
                checkRegis = false
            }

            if (email.isEmpty()) {
                inputemail.setError("Password must be filled with text")
                checkRegis = false
            }

            if (tanggalLahir.isEmpty()) {
                inputtanggalLahir.setError("Password must be filled with text")
                checkRegis = false
            }

            if (password.isEmpty()) {
                inputpassword.setError("Password must be filled with text")
                checkRegis = false
            }

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !nohandphone.isEmpty() ) {
                val moveRegister = Intent(this@RegisterActivity, MainActivity::class.java)
                moveRegister.putExtra("register", mBundle)
                startActivity(moveRegister)
                checkRegis=true
            }

            if (!checkRegis) return@OnClickListener
        })
        moveLogin.setOnClickListener{
            val moveLog = Intent(this, MainActivity::class.java)
            startActivity(moveLog)
        }

    }


}