package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.entity.Kos

class RVKosAdapter(private val data: Array<Kos>) : RecyclerView.Adapter<RVKosAdapter.viewHolder>(){
    private val images = intArrayOf(
        R.drawable.kos1,
        R.drawable.kos2,
        R.drawable.kos3,
        R.drawable.kos4,
        R.drawable.kos5,
        R.drawable.kos6,
        R.drawable.kos7,
        R.drawable.kos8,
        R.drawable.kos9,
        R.drawable.kos10)
    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int) :viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_kos, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int){
        val currentItem = data[position]
        holder.tvnamaKos.text = currentItem.namaKos
        holder.tvalamatKos.text = currentItem.alamatKos
        holder.tvnamaPemilik.text = currentItem.namaPemilik
        holder.ivKos.setImageResource(images[position])
    }

    override fun getItemCount(): Int{
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvnamaKos : TextView = itemView.findViewById(R.id.tv_namaKos)
        val tvalamatKos : TextView = itemView.findViewById(R.id.tv_alamatKos)
        val tvnamaPemilik : TextView = itemView.findViewById(R.id.tv_namaPemilik)
        val ivKos : ImageView = itemView.findViewById(R.id.IVkos)
    }
}