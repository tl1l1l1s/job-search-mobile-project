package com.appproject.project_jobsearch.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appproject.project_jobsearch.data.ScrapDto
import com.appproject.project_jobsearch.databinding.ListItemBinding
import com.appproject.project_jobsearch.ui.ItemAdapter.OnItemClickListener

class ScrapAdapter : RecyclerView.Adapter<ScrapAdapter.ScrapHolder>() {

    var scraps : List<ScrapDto>? = null

    override fun getItemCount(): Int {
        return scraps?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapAdapter.ScrapHolder {
        val itemBinding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ScrapAdapter.ScrapHolder, position: Int) {
        holder.itemBinding.tvItemCompany.text = scraps?.get(position)?.companyName
        holder.itemBinding.tvItemJobCd.text = "(${scraps?.get(position)?.jobCdName?.split(",")!!.get(0)}...)"

        if(scraps?.get(position)?.closeTypeCode == "1") {
            holder.itemBinding.tvItemDay.text = scraps?.get(position)?.expirationDate?.subSequence(0, 10)
        } else {
            holder.itemBinding.tvItemDay.text = scraps?.get(position)?.closeTypeName + " 마감"
        }

        holder.itemBinding.clItem.setOnClickListener {
            clickListener?.onItemClick(it, position)
        }
    }

    class ScrapHolder(val itemBinding: ListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }
}