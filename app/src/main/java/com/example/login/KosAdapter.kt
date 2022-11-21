package com.example.login

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.login.models.Kos
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class KosAdapter (private var kost: List<Kos>, context: Context) :
    RecyclerView.Adapter<KosAdapter.NoteViewHolder>(), Filterable {

    private var filteredKosList: MutableList<Kos>
    private val context: Context

    init {
        filteredKosList = ArrayList(kost)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_kos, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredKosList.size
    }

    fun setKostList(kost: Array<Kos>) {
        this.kost = kost.toList()
        filteredKosList = kost.toMutableList()
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val kos = filteredKosList[position]
        holder.tvNamaKos.text = kos.namaKos
        holder.tvNamaPemesan.text = kos.namaPengguna
        holder.tvTanggalPesan.text = kos.tanggalPesan
        holder.tvTanggalMasuk.text = kos.tanggalMasuk

        holder.iconDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is ActivityPemesanan) kos.id?.let { it1 ->
                        context.deletePesanan(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvKos.setOnClickListener {
            val i = Intent(context, EditKosActivity::class.java)
            i.putExtra("id", kos.id)
            if(context is ActivityPemesanan)
                context.startActivityForResult(i, ActivityPemesanan.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Kos> = java.util.ArrayList()
                if(charSequenceString.isEmpty()) {
                    filtered.addAll(kost)
                } else {
                    for (kos in kost) {
                        if(kos.namaKos.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(kos)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredKosList.clear()
                filteredKosList.addAll((filterResults.values as List<Kos>))
                notifyDataSetChanged()
            }
        }
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNamaKos : TextView
        var tvNamaPemesan: TextView
        var tvTanggalPesan: TextView
        var tvTanggalMasuk: TextView
        var iconDelete: ImageButton
        var cvKos: CardView

        init {
            tvNamaKos = itemView.findViewById(R.id.kos_title)
            tvNamaPemesan = itemView.findViewById(R.id.pemilik_title)
            tvTanggalPesan = itemView.findViewById(R.id.tittle_pemesanan)
            tvTanggalMasuk = itemView.findViewById(R.id.tittle_masuk)
            iconDelete = itemView.findViewById(R.id.icon_delete)
            cvKos = itemView.findViewById(R.id.cv_kos)
        }
    }
}
