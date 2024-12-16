package com.appproject.project_jobsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        detailBinding.btnCancel.setOnClickListener {
            finish()
        }
    }
}