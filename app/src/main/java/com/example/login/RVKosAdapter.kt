package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.entity.Kos

class RVKosAdapter(private val data: Array<Kos>) : RecyclerView.Adapter<RVKosAdapter.viewHolder>(){

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int) :viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_kos, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.tvnamaKos.text = currentItem.namaKos
        holder.tvalamatKos.text = currentItem.alamatKos
        holder.tvnamaPemilik.text = currentItem.namaPemilik
    }

    override fun getItemCount(): Int{
        return data.size
    }

    class viewHolder(itemvView: View) : RecyclerView.ViewHolder(itemvView){
        val tvnamaKos : TextView = itemView.findViewById(R.id.tv_namaKos)
        val tvalamatKos : TextView = itemView.findViewById(R.id.tv_alamatKos)
        val tvnamaPemilik : TextView = itemView.findViewById(R.id.tv_namaPemilik)
    }
}