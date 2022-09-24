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
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.setFragmentResult
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

class ProfileFragment : Fragment() {
    val db by lazy { UserDB(requireContext())}
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences: SharedPreferences? = null
    private var bind: FragmentProfileBinding? = null
    private val binding get() = bind!!

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

        sharedPreferences = activity?.getSharedPreferences(preference, Context.MODE_PRIVATE)


            val user = db.userDao().getUser(sharedPreferences!!.getString(id, "")!!.toInt())?.get(0)
            binding.textNama.setText(user?.Username)
            binding.textEmail.setText(user?.Email)
            binding.textPhone.setText(user?.NomorHandphone)
            binding.textTanggal.setText(user?.TanggalLahir)

            binding.btnedit.setOnClickListener{
                val move = Intent(activity, EditActivity::class.java)
                startActivity(move)
                activity?.finish()
            }
        }



/*
    val db by lazy { UserDB(requireContext())}
    private val id = "id"
    private val preference = "myPref"
    var sharedPreferences: SharedPreferences? = null
    private var bind: FragmentProfileBinding? = null
    private val binding get() = bind!!

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

        val sharedPreferences = activity?.getSharedPreferences(preference, Context.MODE_PRIVATE)
    val userDao = db.userDao()
    val user = userDao.getUser(sharedPreferences.getInt("id", 0))
    binding.textNama.setText(user?.Username)
    binding.textEmail.setText(user?.Email)
    binding.textPhone.setText(user?.NomorHandphone)
    binding.textTanggal.setText(user?.TanggalLahir)

    binding.btnedit.setOnClickListener {
        val move = Intent(activity, EditActivity::class.java)
        startActivity(move)
        activity?.finish()
    }



    }


 */

}