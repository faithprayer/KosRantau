package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.SurveyApi
import com.example.login.models.Survey
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_survey.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import java.nio.charset.StandardCharsets

class ActivitySurvey : AppCompatActivity() {
    private var srSurvey: SwipeRefreshLayout? = null
    private var svSurvey: SearchView? = null
    private var adapter: SurveyAdapter? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object {
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)
        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srSurvey = findViewById(R.id.sr_survey)
        svSurvey = findViewById(R.id.sv_survey)
        srSurvey?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allSurvey() })
        svSurvey?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String?): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_addSurvey)
        fabAdd.setOnClickListener {
            val  i = Intent(this@ActivitySurvey, AddEditSurveyActivity::class.java)
            startActivityForResult(i, ActivitySurvey.LAUNCH_ADD_ACTIVITY)
        }
        val rvSurvey = findViewById<RecyclerView>(R.id.rv_survey)
        adapter = SurveyAdapter(ArrayList(), this)
        rvSurvey.layoutManager = LinearLayoutManager(this)
        rvSurvey.adapter = adapter
        allSurvey()
    }

    private fun allSurvey(){
        srSurvey!!.isRefreshing = true
        val stringRequest: StringRequest = object : StringRequest(Method.GET, SurveyApi.GET_ALL_URL, Response.Listener { response ->
            val gson = Gson()
            val jsonObject = JSONObject(response)
            val jsonData = jsonObject.getJSONArray("data")

            var survei : Array<Survey> = gson.fromJson(jsonData.toString(), Array<Survey>::class.java)
            adapter!!.setSurveiList(survei)
            adapter!!.filter.filter(svSurvey!!.query)
            srSurvey!!.isRefreshing = false

            if(!survei.isEmpty())

            else
                Toast.makeText(this@ActivitySurvey, "Data Kosong!", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error ->
            srSurvey!!.isRefreshing = false
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@ActivitySurvey, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e:Exception) {
                Toast.makeText(this@ActivitySurvey, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteSurvey(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object : StringRequest(Method.DELETE, SurveyApi.DELETE_URL + id, Response.Listener { response ->
            setLoading(false)
            val gson = Gson()
            var survei = gson.fromJson(response, Survey::class.java)
            if(survei != null)
                MotionToast.Companion.darkToast(this@ActivitySurvey,
                    "Pengahapusan Berhasil",
                    "Data Berhasil Dihapus",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,www.sanju.motiontoast.R.font.helvetica_regular))
            allSurvey()
        }, Response.ErrorListener { error ->
            setLoading(false)
            try {
                val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                val errors = JSONObject(responseBody)
                Toast.makeText(this@ActivitySurvey, errors.getString("message"), Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ActivitySurvey, e.message, Toast.LENGTH_SHORT).show()
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
        if(resultCode == ActivitySurvey.LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allSurvey()
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