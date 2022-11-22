package com.example.login

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.userApi
import com.example.login.databinding.ActivityMainBinding
import com.example.login.databinding.ActivityRegisterBinding
import com.example.login.model.userModel
import com.example.login.notifikasi.NotificationReceiver
import com.example.login.room.User
import com.example.login.room.UserDB
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101
    private var userId: Int = 0
    private var queue: RequestQueue? = null
    private var checkRegis = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db by lazy { UserDB(this) }
        val userDao = db.userDao()

        supportActionBar?.hide()

        val moveLogin: TextView = findViewById(R.id.textMoveLogin)

        createNotificationChannel()

        queue = Volley.newRequestQueue(this)

        binding.btnRegistrasi.setOnClickListener(View.OnClickListener  {
            var intent = Intent(this, MainActivity::class.java)


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
            }else{
                registerUser(mBundle)
            }

//            val user = User(0,username,nohandphone,email,tanggalLahir,password)
//            userDao.addUser(user)

//            intent.putExtra("register", mBundle)
//            startActivity(intent)
//
//            sendNotification()
        })
        moveLogin.setOnClickListener{
            val moveLog = Intent(this, MainActivity::class.java)
            startActivity(moveLog)
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }


            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel1)
        }
    }
    private fun sendNotification(){
        val intent: Intent = Intent(this, MainActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage","Selamat Datang " + binding.inputLayoutRegUsername.editText?.text.toString())
        val actionIntent = PendingIntent.getBroadcast(this,0,broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val picture = BitmapFactory.decodeResource(resources,R.drawable.logo)
        val builder = NotificationCompat.Builder(this,CHANNEL_ID_1)
            .setSmallIcon(R.drawable.logo)
            .setContentText("Berhasil Register")
            .setLargeIcon(picture)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigLargeIcon(null)
                .bigPicture(picture))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "Pesan", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1,builder.build())
        }
    }

    private fun registerUser(mBundle: Bundle) {

        val createdUser = userModel(
            0,
            binding.inputLayoutRegUsername.getEditText()?.getText().toString(),
            binding.inputLayoutNomorHandphone.getEditText()?.getText().toString(),
            binding.inputLayoutEmail.getEditText()?.getText().toString(),
            binding.inputLayoutTanggalLahir.getEditText()?.getText().toString(),
            binding.inputLayoutPassword.getEditText()?.getText().toString()
        )
        val stringRequest: StringRequest = object : StringRequest(Method.POST, userApi.ADD, Response.Listener { response ->
                val gson = Gson()
                var user = gson.fromJson(response, userModel::class.java)

                if(user != null) {
                    Toast.makeText(this@RegisterActivity, "User Berhasil Register", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    sendNotification()
                    intent.putExtra("Register", mBundle)
                    startActivity(intent)
                }
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@RegisterActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                    checkRegis = false
                }catch (e: Exception) {
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    val requestBody = gson.toJson(createdUser)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)
    }

}