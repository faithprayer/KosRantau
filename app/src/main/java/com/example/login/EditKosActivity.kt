package com.example.login

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.login.databinding.ActivityEditKosBinding
import com.example.login.room.Constant
import com.example.login.room.Kos
import com.example.login.room.KosDB
import kotlinx.android.synthetic.main.activity_edit_kos.*
import kotlinx.android.synthetic.main.adapter_kos.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditKosActivity : AppCompatActivity() {
    val db by lazy { KosDB(this) }
    private var kosId: Int = 0
    private var binding: ActivityEditKosBinding? = null
    private var PELANGGAN_ID_1 = "pelanggan_notification_01"
    private val notificationId2 = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditKosBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        setupView()
        setupListener()

    }

    private fun createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notif"
            val descriptionText = "Desc"

            val channel1 = NotificationChannel(
                PELANGGAN_ID_1,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = descriptionText
            }

            val notificationManager:NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }
    private fun sendNotifications() {
        val builder = NotificationCompat.Builder(this,PELANGGAN_ID_1)
            .setSmallIcon(R.drawable.ic_person)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setColor(Color.BLUE)
            .setContentTitle("Pelanggan Kos")
            .setContentText("Data Pelanggan Penyewa Kos")
            .setStyle(
                NotificationCompat.InboxStyle()
                    .addLine("Nama Kos : "+ binding?.editKos?.text.toString())
                    .addLine("Nama Peminjam : "+ binding?.editPengguna?.text.toString())
                    .addLine("Tanggal Pinjam : "+ binding?.editTanggalMasuk?.text.toString())
                    .addLine("Tanggal Kembali : "+ binding?.editTanggalPesan?.text.toString())
            )


        with(NotificationManagerCompat.from(this)) {
            notify(notificationId2, builder.build())
        }
    }
    fun setupView(){
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
            Constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getKos()
            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getKos()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.kosDao().addKos(
                    Kos(0,edit_kos.text.toString(),
                        edit_pengguna.text.toString(),
                        edit_tanggalMasuk.text.toString(),edit_tanggalMasuk.text.toString())
                )
                createNotificationChannels()
                sendNotifications()
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.kosDao().updateKos(
                    Kos(kosId, edit_kos.text.toString(),
                        edit_pengguna.text.toString(),
                        edit_tanggalPesan.text.toString(),edit_tanggalMasuk.text.toString())
                )
                createNotificationChannels()
                sendNotifications()
                finish()
            }
        }
    }
    fun getKos() {
        kosId = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val kos = db.kosDao().getKos(kosId)[0]
            edit_kos.setText(kos.namaKos)
            edit_pengguna.setText(kos.namaPengguna)
            edit_tanggalMasuk.setText(kos.tanggalMasuk)
            edit_tanggalPesan.setText(kos.tanggalPesan)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}