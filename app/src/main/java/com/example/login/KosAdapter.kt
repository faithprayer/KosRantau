package com.example.login

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.room.Kos
import kotlinx.android.synthetic.main.adapter_kos.view.*

class KosAdapter (private val kost: ArrayList<Kos>, private val
listener: OnAdapterListener) :
    RecyclerView.Adapter<KosAdapter.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            NoteViewHolder {
        return NoteViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.adapter_kos,parent, false)
        )
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position:
    Int) {
        val kos = kost[position]
        holder.view.kos_title.text = kos.namaKos
        holder.view.pemilik_title.text = kos.namaPengguna
        holder.view.tittle_pemesanan.text = kos.tanggalPesan
        holder.view.tittle_masuk.text = kos.tanggalMasuk

        holder.view.kos_title.setOnClickListener{
            listener.onClick(kos)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(kos)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(kos)
        }
    }
    override fun getItemCount() = kost.size
    inner class NoteViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Kos>){
        kost.clear()
        kost.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(kos: Kos)
        fun onUpdate(kos: Kos)
        fun onDelete(kos: Kos)
    }
}
