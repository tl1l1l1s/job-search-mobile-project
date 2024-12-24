package com.appproject.project_jobsearch.ui

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appproject.project_jobsearch.data.JobCd
import com.appproject.project_jobsearch.data.JobCdData
import com.appproject.project_jobsearch.databinding.JobcdDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class JobCdDialog(context: Context, private val onSubmitListener: (List<JobCdData>) -> Unit) : BottomSheetDialog(context) {

    val jobDialogBinding by lazy {
        JobcdDialogBinding.inflate(layoutInflater)
    }

    val jobCds = JobCd.jobCdList
    private val selectedCds = mutableListOf<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(jobDialogBinding.root)

        val width = WindowManager.LayoutParams.MATCH_PARENT
        val height = WindowManager.LayoutParams.MATCH_PARENT
        window?.setLayout(width, height)

        val adapter = JobCdAdapter(jobCds, selectedCds)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        jobDialogBinding.rvJobCd.layoutManager = layoutManager
        jobDialogBinding.rvJobCd.adapter = adapter

        adapter.onSelectedCountChanged = { selectedCount ->
            jobDialogBinding.tvSelected.text = "현재 ${selectedCount}/5개 선택하였습니다."
        }

        jobDialogBinding.btnReset.setOnClickListener {
            selectedCds.clear()
            adapter.notifyDataSetChanged()
            jobDialogBinding.tvSelected.text = "현재 0/5개 선택하였습니다."
        }

        jobDialogBinding.btnSubmit.setOnClickListener {
            val selectedJobCds = selectedCds.map { jobCds[it] }
            onSubmitListener(selectedJobCds)
            dismiss()
        }
    }
}