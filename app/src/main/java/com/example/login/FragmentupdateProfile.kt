package com.example.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.login.databinding.FragmentUpdateProfileBinding
import com.example.login.room.User
import com.example.login.room.UserDB
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.flow.combine


class FragmentupdateProfile : Fragment() {

    private var bind: FragmentUpdateProfileBinding? = null
    private val binding get() = bind!!

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

        var cek = true


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
                updateData()
                transitionFragment(ProfileFragment())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bind = null
    }

    private fun updateData(){
        val sharedPreferences = (activity as HomeActivity).getSharedPreferences()

        val db by lazy { UserDB (activity as HomeActivity) }
        val userDao = db.userDao()

        val id = sharedPreferences.getInt("id", 0)

        val getUser = userDao.getUser(id)

        val user = User(id,
            binding.updtUsername.text.toString(),
            binding.updtnoTlp.text.toString(),
            binding.updtEmail.text.toString(),
            binding.updTgllahir.text.toString(),
            getUser.Password,

        )
        userDao.updateUser(user)
    }
    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.mainContainer, fragment)
            .addToBackStack(null).commit()
        transition.hide(FragmentupdateProfile())
    }

}