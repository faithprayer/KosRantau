package com.example.login

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.login.room.Constant
import com.example.login.room.Kos
import com.example.login.room.KosDB
import kotlinx.android.synthetic.main.activity_pemesanan.*
import kotlinx.android.synthetic.main.fragment_pemesanan.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityPemesanan : AppCompatActivity() {
    val db by lazy { KosDB(this) }
    lateinit var kosAdapter: KosAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemesanan)
        setupListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        kosAdapter = KosAdapter(arrayListOf(), object :
            KosAdapter.OnAdapterListener{
            override fun onClick(kos: Kos) {

                intentEdit(kos.id, Constant.TYPE_READ)
            }
            override fun onUpdate(kos: Kos) {
                intentEdit(kos.id, Constant.TYPE_UPDATE)
            }
            override fun onDelete(kos: Kos) {
                deleteDialog(kos)
            }
        })
        list_kos.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = kosAdapter
        }
    }
    private fun deleteDialog(kos: Kos){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From${kos.namaKos}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener
            { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.kosDao().deleteKos(kos)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val kos = db.kosDao().getKos()
            Log.d("FragmentPemesanan","dbResponse: $kos")
            withContext(Dispatchers.Main){
                kosAdapter.setData( kos )
            }
        }
    }
    fun setupListener() {
        button_create.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditKosActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }
}