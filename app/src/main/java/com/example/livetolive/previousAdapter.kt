package com.example.livetolive

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class previousAdapter(private var resumenes:List<previousDataClass>):
    RecyclerView.Adapter<previousAdapter.previousViewHolder>(){
    class previousViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val mes: TextView=itemView.findViewById(R.id.txtMes)
        val dia: TextView=itemView.findViewById(R.id.txtDia)
        val progreso: ProgressBar=itemView.findViewById(R.id.progressBarPhy)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): previousAdapter.previousViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.date_item,parent,false)
        return previousViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: previousAdapter.previousViewHolder,
        position: Int
    ) {
        val fecha=resumenes[position]
        holder.mes.text=fecha.mes
        holder.mes.setTextColor(ContextCompat.getColor(holder.itemView.context,fecha.color))
        holder.dia.text=fecha.dia
        holder.progreso.setProgress(fecha.progreso)
        holder.progreso.progressTintList= ColorStateList.valueOf(ContextCompat.getColor(holder.itemView.context,fecha.color))
    }

    override fun getItemCount()=resumenes.size
}