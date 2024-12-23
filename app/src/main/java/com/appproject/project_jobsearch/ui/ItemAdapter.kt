package com.appproject.project_jobsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appproject.project_jobsearch.databinding.ListItemBinding
import com.appproject.project_jobsearch.data.network.Job
import com.appproject.project_jobsearch.data.network.Jobs

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemHolder>() {
    var items : List<Job>? = null

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ItemHolder, position: Int) {
            holder.itemBinding.tvItemCompany.text =
                (items?.get(position) as Job).company?.companyDetail?.companyName.toString()

            holder.itemBinding.tvItemJobCd.text = "(${(items?.get(position) as Job)?.position?.jobCode?.jobCdName?.split(",")!!.get(0)}...)"
            if((items?.get(position) as Job)?.closeType?.code == "1") {
                holder.itemBinding.tvItemDay.text = (items?.get(position) as Job).expiration?.subSequence(0, 10)
            } else {
                holder.itemBinding.tvItemDay.text = (items?.get(position) as Job)?.closeType?.closeTypeName + " 마감"
            }
            holder.itemBinding.clItem.setOnClickListener {
                clickListener?.onItemClick(it, position)
            }

    }

    class ItemHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)


    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }

}