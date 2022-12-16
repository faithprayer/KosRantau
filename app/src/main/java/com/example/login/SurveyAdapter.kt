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
import com.example.login.models.Survey
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class SurveyAdapter (private var survei : List<Survey>, context : Context) :
    RecyclerView.Adapter<SurveyAdapter.NoteViewHolder>(), Filterable{

    private var filteredSurveyList: MutableList<Survey>
    private val context:Context

    init {
        filteredSurveyList = ArrayList(survei)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyAdapter.NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.adapter_survey, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredSurveyList.size
    }

    fun setSurveiList(survei: Array<Survey>) {
        this.survei = survei.toList()
        filteredSurveyList = survei.toMutableList()
    }

    override fun onBindViewHolder(holder: SurveyAdapter.NoteViewHolder, position: Int) {
        val survei = filteredSurveyList[position]
        holder.tvNamaKos.text = survei.namaKos_survei
        holder.tvNamaPenyewa.text = survei.namaPengguna_survei
        holder.tvNoTlp.text = survei.nomor_telepon_survei
        holder.tvTanggalSurvey.text = survei.tanggalSurvei
        holder.tvJamSurvey.text = survei.jamSurvei

        holder.iconDelete.setOnClickListener {
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus") { _, _ ->
                    if (context is ActivitySurvey) survei.id?.let { it1 ->
                        context.deleteSurvey(
                            it1
                        )
                    }
                }
                .show()
        }
        holder.cvSurvey.setOnClickListener {
            val i = Intent(context, AddEditSurveyActivity::class.java)
            i.putExtra("id", survei.id)
            if(context is ActivitySurvey)
                context.startActivityForResult(i, ActivitySurvey.LAUNCH_ADD_ACTIVITY)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<Survey> = java.util.ArrayList()
                if(charSequenceString.isEmpty()) {
                    filtered.addAll(survei)
                } else {
                    for (survey in survei) {
                        if(survey.namaKos_survei.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        ) filtered.add(survey)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredSurveyList.clear()
                filteredSurveyList.addAll((filterResults.values as List<Survey>))
                notifyDataSetChanged()
            }
        }
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNamaKos : TextView
        var tvNamaPenyewa: TextView
        var tvNoTlp: TextView
        var tvTanggalSurvey: TextView
        var tvJamSurvey : TextView
        var iconDelete: ImageButton
        var cvSurvey: CardView

        init {
            tvNamaKos = itemView.findViewById(R.id.namaKos)
            tvNamaPenyewa = itemView.findViewById(R.id.namaPenyewa)
            tvNoTlp = itemView.findViewById(R.id.nomorTelepon)
            tvTanggalSurvey = itemView.findViewById(R.id.tanggalSurvey)
            tvJamSurvey = itemView.findViewById(R.id.jamSurvey)
            iconDelete = itemView.findViewById(R.id.icon_delete)
            cvSurvey = itemView.findViewById(R.id.cv_survey)
        }
    }
}