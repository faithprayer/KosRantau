package com.example.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.setFragmentResult
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.databinding.ActivityMainBinding
import com.example.login.databinding.FragmentProfileBinding
import com.example.login.entity.Kos
import com.example.login.room.User
import com.example.login.room.UserDAO
import com.example.login.room.UserDB
import kotlinx.android.synthetic.main.activity_register.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.*
import kotlin.system.exitProcess

import com.example.login.*
import com.example.login.api.userApi
import com.example.login.camera.CameraActivity
import kotlinx.android.synthetic.main.activity_edit_kos.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.prefs.Preferences

class ProfileFragment : Fragment() {
    private var bind: FragmentProfileBinding? = null
    private val binding get() = bind!!
    private var queue: RequestQueue? = null
    private var layoutloading: LinearLayout? = null

    val db by lazy { UserDB(requireActivity())}
    private var Userid: Int= 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getdata()

        queue = Volley.newRequestQueue(activity)
        layoutloading = view.findViewById(R.id.layout_loading)

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id",0)


        getUser(id)

        binding.btnUpdate.setOnClickListener {
            transitionFragment(FragmentupdateProfile())
        }

        binding.btnLogout.setOnClickListener {
            val move = Intent(activity, MainActivity::class.java)
            startActivity(move)
            activity?.finish()
        }

        binding.logoProfile.setOnClickListener {
            val move = Intent(activity, CameraActivity::class.java)
            startActivity(move)
            activity?.finish()
        }
    }

    private fun setLoading(isLoading: Boolean) {
        if(isLoading) {
            activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.VISIBLE
        }else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutloading!!.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bind = null
    }

//    private fun getdata(){
//        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
//
//        val db by lazy { UserDB(activity as HomeActivity)}
//        val userDao = db.userDao()
//
//        val user = userDao.getUser(sharedPreferences.getInt("id",0))
//        binding.viewUsername.setText(user.Username)
//        binding.viewNomorTelepon.setText(user.NomorHandphone)
//        binding.viewEmail.setText(user.Email)
//        binding.viewTanggalLahir.setText(user.TanggalLahir)
//    }

    private fun getUser(id: Int) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, userApi.GET_BY_ID_URL + id, Response.Listener { response ->
                var joUser = JSONObject(response.toString())
                val userdata = joUser.getJSONObject("data")

                binding.viewUsername.setText(userdata.getString("Username"))
                binding.viewNomorTelepon.setText(userdata.getString("NomorHandphone"))
                binding.viewEmail.setText(userdata.getString("Email"))
                binding.viewTanggalLahir.setText(userdata.getString("TanggalLahir"))

                Toast.makeText(activity, "Data User berhasil diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)

            }, Response.ErrorListener { error ->
                setLoading(false)
                try {

                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)

                    Toast.makeText(
                        activity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception) {
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.mainContainer, fragment)
            .addToBackStack(null).commit()
        transition.hide(ProfileFragment())
    }

}