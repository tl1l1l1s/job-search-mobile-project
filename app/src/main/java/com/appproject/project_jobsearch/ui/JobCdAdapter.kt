package com.appproject.project_jobsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.appproject.project_jobsearch.data.JobCdData
import com.appproject.project_jobsearch.databinding.JobcdItemBinding

class JobCdAdapter(private val jobCdList : List<JobCdData>, private val selectedPositions: MutableList<Int>) : RecyclerView.Adapter<JobCdAdapter.JobCdViewHolder>() {

    override fun getItemCount(): Int {
        return jobCdList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobCdViewHolder {
        return JobCdViewHolder(JobcdItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: JobCdViewHolder, position: Int) {
        val isSelected = selectedPositions.contains(position)

        holder.jobcdItemBinding.apply {
            checkBoxJobCd.text = jobCdList[position].name
            checkBoxJobCd.isChecked = isSelected

            checkBoxJobCd.setOnClickListener {
                if(isSelected) {
                    selectedPositions.remove(position)
                    checkBoxJobCd.isChecked = false
                } else {
                    if(selectedPositions.size < 5) {
                        selectedPositions.add(position)
                        checkBoxJobCd.isChecked = true
                    } else {
                        Toast.makeText(it.context, "최대 5개까지 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                notifyDataSetChanged()
                onSelectedCountChanged?.invoke(selectedPositions.size)
            }
        }
    }

    var onSelectedCountChanged: ((Int) -> Unit)? = null

    inner class JobCdViewHolder(val jobcdItemBinding: JobcdItemBinding) : RecyclerView.ViewHolder(jobcdItemBinding.root)
}