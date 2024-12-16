package com.appproject.project_jobsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.databinding.ActivityScrapBinding

class ScrapActivity : AppCompatActivity() {

    val scrapBinding by lazy {
        ActivityScrapBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(scrapBinding.root)

        scrapBinding.btnCancel.setOnClickListener {
            finish()
        }
    }
}