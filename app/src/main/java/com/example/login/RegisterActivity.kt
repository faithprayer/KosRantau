package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.login.databinding.ActivityMainBinding
import com.example.login.databinding.ActivityRegisterBinding
import com.example.login.room.User
import com.example.login.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding

    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()

        supportActionBar?.hide()

        val moveLogin: TextView = findViewById(R.id.textMoveLogin)

        binding.btnRegistrasi.setOnClickListener(View.OnClickListener  {
            var intent = Intent(this, MainActivity::class.java)
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
                binding.inputLayoutRegUsername.setError("Username must be filled with text")
                checkRegis = false
            }

            if (nohandphone.isEmpty()) {
                binding.inputLayoutNomorHandphone.setError("Password must be filled with text")
                checkRegis = false
            }

            if (email.isEmpty()) {
                binding.inputLayoutEmail.setError("Password must be filled with text")
                checkRegis = false
            }

            if (tanggalLahir.isEmpty()) {
                binding.inputLayoutTanggalLahir.setError("Password must be filled with text")
                checkRegis = false
            }

            if (password.isEmpty()) {
                binding.inputLayoutPassword.setError("Password must be filled with text")
                checkRegis = false
            }

            if(!username.isEmpty() && !password.isEmpty() && !email.isEmpty() && !tanggalLahir.isEmpty() && !nohandphone.isEmpty() ) {
                checkRegis=true
            }

            if (!checkRegis) {
                return@OnClickListener
            }

            val user = User(0,username,nohandphone,email,tanggalLahir,password)
            userDao.addUser(user)

            intent.putExtra("register", mBundle)
            startActivity(intent)
        })
        moveLogin.setOnClickListener{
            val moveLog = Intent(this, MainActivity::class.java)
            startActivity(moveLog)
        }
    }

}