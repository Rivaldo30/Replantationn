package com.sobi.replantation.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobi.replantation.databinding.ListAreaBinding
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.ui.TaskDetailActivity


class AreaListAdapter(val context: Context) : RecyclerView.Adapter<AreaListAdapter.ListViewHolder>(){

    var areas = mutableListOf<Area>()

    fun addData(list: List<Area>){
        areas.clear()
        this.areas = list.toMutableList()
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ListAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       with(holder){
           with(areas[position]){
               binding.tvAreaName.text = this.name
               binding.tvAreaMeasure.text = "${this.areaMeasure} m2"
               binding.sertifNumber.text = this.certificateNumber
               binding.jumlahPohon.text = "Sengon : ${this.jumlahTebang}"
               binding.detailArea.paintFlags = binding.detailArea.paintFlags or Paint.UNDERLINE_TEXT_FLAG
               binding.detailArea.setOnClickListener {
                   (context as TaskDetailActivity).goToDetailArea(this.id, this.name)

               }


           }
       }
    }

    override fun getItemCount(): Int = areas.size

    inner class ListViewHolder(val binding: ListAreaBinding) : RecyclerView.ViewHolder(binding.root)

    }


