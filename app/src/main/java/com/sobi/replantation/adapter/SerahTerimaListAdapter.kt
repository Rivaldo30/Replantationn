package com.sobi.replantation.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobi.replantation.databinding.ListAreaBinding
import com.sobi.replantation.databinding.ListSerahTerimaBinding
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.domain.model.SerahTerima
import com.sobi.replantation.ui.ListSerahTerimaActivity
import com.sobi.replantation.ui.TaskDetailActivity


class SerahTerimaListAdapter(val context: Context) : RecyclerView.Adapter<SerahTerimaListAdapter.ListViewHolder>(){

    var serahTerimas = mutableListOf<SerahTerima>()

    fun addData(list: List<SerahTerima>){
        serahTerimas.clear()
        this.serahTerimas = list.toMutableList()
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListSerahTerimaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       with(holder){
           with(serahTerimas[position]){
               binding.spnSpecies.text = this.speciesId.toString()
               //binding.spnSpecies.text = if (this.speciesId==1) "Mahoni" else if(this.speciesId==2) "Jati" else "Sengon"
               binding.jumlahBibit.text = "Jumlah Bibit : ${this.totalBibit}"
               binding.detailTree.setOnClickListener {
                   (context as ListSerahTerimaActivity).editSerahTerima(this.id)
               }

           }
       }
    }

    override fun getItemCount(): Int = serahTerimas.size

    inner class ListViewHolder(val binding: ListSerahTerimaBinding) : RecyclerView.ViewHolder(binding.root)

    }


