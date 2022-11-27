package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.KosApi
import com.example.login.models.Kos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_pemesanan.*
import kotlinx.android.synthetic.main.fragment_pemesanan.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import java.nio.charset.StandardCharsets

class ActivityPemesanan : AppCompatActivity() {
    private var srPemesanan: SwipeRefreshLayout? = null
    private var svPemesanan: SearchView? = null
    private var adapter: KosAdapter? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pemesanan)
        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srPemesanan = findViewById(R.id.sr_pemesanan)
        svPemesanan = findViewById(R.id.sv_pemesanan)
        srPemesanan?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allPesanan() })
        svPemesanan?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val  i = Intent(this@ActivityPemesanan, EditKosActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }
        val rvKos = findViewById<RecyclerView>(R.id.rv_pemesanan)
        adapter = KosAdapter(ArrayList(), this)
        rvKos.layoutManager = LinearLayoutManager(this)
        rvKos.adapter = adapter
        allPesanan()

    }

    private fun allPesanan(){
        srPemesanan!!.isRefreshing = true
        val stringRequest: StringRequest = object : StringRequest(Method.GET, KosApi.GET_ALL_URL, Response.Listener { response ->
            val gson = Gson()
            val jsonObject = JSONObject(response)
            val jsonData = jsonObject.getJSONArray("data")

            var kos : Array<Kos> = gson.fromJson(jsonData.toString(), Array<Kos>::class.java)
            adapter!!.setKostList(kos)
            adapter!!.filter.filter(svPemesanan!!.query)
            srPemesanan!!.isRefreshing = false

            if(!kos.isEmpty())

            else
                Toast.makeText(this@ActivityPemesanan, "Data Kosong!", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error ->
            srPemesanan!!.isRefreshing = false
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@ActivityPemesanan, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e:Exception) {
                Toast.makeText(this@ActivityPemesanan, e.message, Toast.LENGTH_SHORT).show()
            }
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    fun deletePesanan(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object : StringRequest(Method.DELETE, KosApi.DELETE_URL + id, Response.Listener { response ->
            setLoading(false)
            val gson = Gson()
            var kos = gson.fromJson(response, Kos::class.java)
            if(kos != null)
                MotionToast.Companion.darkToast(this@ActivityPemesanan,
                    "Pengahapusan Berhasil",
                    "Data Berhasil Dihapus",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,www.sanju.motiontoast.R.font.helvetica_regular))
            allPesanan()
        }, Response.ErrorListener { error ->
            setLoading(false)
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@ActivityPemesanan, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ActivityPemesanan, e.message, Toast.LENGTH_SHORT).show()
            }
        }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allPesanan()
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.GONE
        }
    }
}