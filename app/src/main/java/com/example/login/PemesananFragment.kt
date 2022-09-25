package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class PemesananFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pemesanan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnAddPeminjam: Button = view.findViewById(R.id.btnAddPeminjam)


        btnAddPeminjam.setOnClickListener(View.OnClickListener {
            val movePeminjam = Intent(this@PemesananFragment.context, ActivityPemesanan::class.java)
            startActivity(movePeminjam)
        })
    }
}