package com.example.livetolive

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class previousAdapter(private var resumenes: List<previousDataClass>) :
    RecyclerView.Adapter<previousAdapter.Holder>() {

    // Listener opcional para clics
    private var listener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(l: (Int) -> Unit) {
        listener = l
    }

    // ViewHolder unificado
    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val mes: TextView = view.findViewById(R.id.txtMes)
        private val dia: TextView = view.findViewById(R.id.txtDia)
        private val progreso: ProgressBar = view.findViewById(R.id.progressBarItem)

        fun bind(item: previousDataClass, position: Int) {
            // Mes
            mes.text = item.mes
            mes.setTextColor(ContextCompat.getColor(itemView.context, item.color))

            // Día
            dia.text = item.dia

            // Progreso
            progreso.progress = item.progreso
            progreso.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, item.color)
            )

            // Click
            itemView.setOnClickListener {
                listener?.invoke(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.date_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(resumenes[position], position)
    }

    override fun getItemCount(): Int = resumenes.size
}
