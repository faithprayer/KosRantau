package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kos)
        setupView()
        setupListener()

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