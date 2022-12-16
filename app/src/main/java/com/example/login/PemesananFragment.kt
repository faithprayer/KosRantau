package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.login.map.FragmentLokasi
import kotlinx.android.synthetic.main.fragment_pemesanan.*

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
        val btnSurvey : Button = view.findViewById(R.id.btnSurvey)

        btnAddPeminjam.setOnClickListener(View.OnClickListener {
            val movePeminjam = Intent(this@PemesananFragment.context, ActivityPemesanan::class.java)
            startActivity(movePeminjam)
        })

        btnLokasi.setOnClickListener {
            transitionFragment(FragmentLokasi());
        }

        btnSurvey.setOnClickListener(View.OnClickListener {
            val moveSurvey = Intent(this@PemesananFragment.context, ActivitySurvey::class.java)
            startActivity(moveSurvey)
        })

    }

    private fun transitionFragment(fragment: Fragment) {
        val transition = requireActivity().supportFragmentManager.beginTransaction()
        transition.replace(R.id.mainContainer, fragment)
            .addToBackStack(null).commit()
        transition.hide(PemesananFragment())
    }
}