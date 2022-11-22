package com.example.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.api.userApi
import com.example.login.databinding.FragmentUpdateProfileBinding
import com.example.login.model.userModel
import com.example.login.room.User
import com.example.login.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.flow.combine
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws


class FragmentupdateProfile : Fragment() {

    private var bind: FragmentUpdateProfileBinding? = null
    private val binding get() = bind!!
    private var queue: RequestQueue? = null
    private var layoutloading: LinearLayout? = null
    private lateinit var sharedPreferences: SharedPreferences

    var cek = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        queue = Volley.newRequestQueue(activity)
        layoutloading = view.findViewById(R.id.layout_loading)
        sharedPreferences = (activity as HomeActivity).getSharedPreferences("login", Context.MODE_PRIVATE)

        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
        var id = sharedPreferences.getInt("id",0)
        var Password = sharedPreferences.getString("Password",null)

        binding.btnUpdateProfile.setOnClickListener {
            if (binding.updtUsername.text.toString().isEmpty()){
                binding.updtUsername.setError("Username Kosong")
                cek =false
            }

            if (binding.updtnoTlp.text.toString().isEmpty()){
                binding.updtnoTlp.setError("Nomor Handphone Kosong")
                cek =false
            }

            if (binding.updtEmail.text.toString().isEmpty()){
                binding.updtEmail.setError("Email Kosong")
                cek =false
            }

            if (binding.updTgllahir.text.toString().isEmpty()){
                binding.updTgllahir.setError("Tanggal Lahir Kosong")
                cek =false
            }
            if(!cek){
                cek = true
                return@setOnClickListener
            }else{
//                updateData()
                if(Password!=null){
                    updateData(id,Password)
                }
                transitionFragment(ProfileFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bind = null
    }

//    private fun updateData(){
//        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()
//
//        val db by lazy { UserDB (activity as HomeActivity) }
//        val userDao = db.userDao()
//
//        val id = sharedPreferences.getInt("id", 0)
//
//        val getUser = userDao.getUser(id)
//
//        val user = User(id,
//            binding.updtUsername.text.toString(),
//            binding.updtnoTlp.text.toString(),
//            binding.updtEmail.text.toString(),
//            binding.updTgllahir.text.toString(),
//            getUser.Password,
//
//        )
//        userDao.updateUser(user)
//    }

    private fun updateData(id: Int, Password: String) {
        setLoading(true)
        val user= userModel(
            id,
            binding.updtUsername.text.toString(),
            binding.updtnoTlp.text.toString(),
            binding.updtEmail.text.toString(),
            binding.updTgllahir.text.toString(),
            Password
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.PUT, userApi.UPDATE_USER + id, Response.Listener { response ->

                val gson = Gson()
                var user = gson.fromJson(response, userModel::class.java)

                if(user != null) {

                    var resJO = JSONObject(response.toString())
                    val  userobj = resJO.getJSONObject("data")

                    sharedPreferences.edit()
                        .putInt("id",userobj.getInt("id"))
                        .putString("Username",userobj.getString("Username"))
                        .putString("Password",userobj.getString("Password"))
                        .apply()
                    Toast.makeText(activity, "User Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                }
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

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        queue!!.add(stringRequest)

    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.mainContainer, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentupdateProfile())
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

}