package com.example.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.userApi
import com.example.login.model.userModel
import com.example.login.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var mainLayout: ConstraintLayout
    private lateinit var usernameInput : TextInputEditText
    private lateinit var PasswordInput : TextInputEditText
    lateinit var mBundle: Bundle

    private var queue: RequestQueue? = null

    var vUsername: String =""
    lateinit var vNoHandphone: String
    lateinit var vEmail: String
    lateinit var vTglLahir:String
    var vPassword: String =""

    var checkLogin = true

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun",true)

        if(isFirstRun){
            startActivity(Intent(this@MainActivity,SplashScreen :: class.java))
            finish()
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun",false).commit()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")
        supportActionBar?.hide()

        val db by lazy { UserDB(this) }
        val userDAO = db.userDao()

        sharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE)
        queue = Volley.newRequestQueue(this)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        mainLayout = findViewById(R.id.loginLayout)

        usernameInput = findViewById(R.id.TextInputUsername)
        PasswordInput = findViewById(R.id.textInputPassword)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val moveReg: TextView = findViewById(R.id.textMoveRegister)

        getBundle()

        btnLogin.setOnClickListener (View.OnClickListener{
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

//            val user = userDAO.cekUser(username, password)
//            if(user != null){
//                sharedPreferences.edit()
//                    .putInt("id",user.id)
//                    .apply()
//                checkLogin = true
//            }
//
//            if (username == "admin" && password == "admin") {
//                checkLogin = true
//            }
//
//            if(intent.getBundleExtra("register") !=null){
//                if(username == vUsername && password == vPassword){
//                    checkLogin =true
//                }
//            }
            if(!checkLogin){
                warningLogin()
                return@OnClickListener
            }else{
                Login()
            }
//            val moveHome = Intent(this@MainActivity, HomeActivity::class.java)
//            startActivity(moveHome)
        })

        moveReg.setOnClickListener(View.OnClickListener{
            val moverReg = Intent(this@MainActivity, RegisterActivity::class.java)
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

    fun warningLogin() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
        }
        builder.setMessage("Username dan Password Salah")
        builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
        builder.show()
    }

    private fun Login() {
        //  setLoading(true)

        val userModel = userModel(
            0,
            inputUsername.getEditText()?.getText().toString(),
            "",
            "",
            "",
            inputPassword.getEditText()?.getText().toString(),
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, userApi.LOGIN, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, userModel::class.java)

                if(user!=null) {
                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

                    Toast.makeText(this@MainActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("Username",userobj.getString("Username"))
                        .putString("Password",userobj.getString("Password"))
                        .apply()
                    startActivity(intent)
                }else {
                    Toast.makeText(this@MainActivity, "Login gagal", Toast.LENGTH_SHORT).show()
                    return@Listener
                }

            }, Response.ErrorListener { error ->
                // setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }

            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(userModel)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }
}