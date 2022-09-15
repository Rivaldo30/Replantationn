package com.sobi.replantation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appsobi.pendataan.widget.hideKeyboard
import com.sobi.replantation.R
import com.sobi.replantation.databinding.TaskListRowBinding
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.ui.TaskListActivity
import com.squareup.picasso.Picasso


class TaskListAdapter(val context: Context) :
    RecyclerView.Adapter<TaskListAdapter.ListViewHolder>() {

    var tasks = mutableListOf<Assignment>()

    fun addData(list: List<Assignment>) {
        tasks.clear()
        this.tasks = list.toMutableList()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = TaskListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            with(tasks[position]) {
                (context as TaskListActivity).hideKeyboard()
                binding.memberName.text = this.memberName
                binding.memberNumber.text = this.memberNo
                val photoUrl =
                    if (this.photoUrl != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${this.photoUrl}"
                    else null
                Picasso.get().load(photoUrl)
                    .placeholder(R.drawable.avatar)
                    .resize(200, 100)
                    .error(R.drawable.avatar).into(binding.imgPhotoMember)

                binding.detailTaskBtn.setOnClickListener {
                    (context as TaskListActivity).goToDetailMember(this.memberId, this.memberName)
                }
                binding.detailTaskUpload.setOnClickListener {
                    binding.layout.visibility = View.GONE
                }

                if (this.totalTerima == 0) {
                    binding.detailTaskBtn.setBackgroundResource(R.drawable.background_yellow)
                    binding.detailTaskBtn.text = "Mulai"
                    binding.detailTaskUpload.visibility = View.GONE
                }else if (this.finishedSerahTerima == 1) {
                    binding.detailTaskBtn.setBackgroundResource(R.drawable.background_red)
                    binding.detailTaskBtn.text = "Ubah"
                    binding.detailTaskUpload.visibility = View.VISIBLE
                } else {
                    binding.detailTaskBtn.setBackgroundResource(R.drawable.background_green)
                    binding.detailTaskBtn.text = "Lanjut"
                    binding.detailTaskUpload.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    inner class ListViewHolder(val binding: TaskListRowBinding) :
        RecyclerView.ViewHolder(binding.root)

}


