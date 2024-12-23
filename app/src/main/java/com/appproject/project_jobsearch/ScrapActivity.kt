package com.appproject.project_jobsearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appproject.project_jobsearch.databinding.ActivityScrapBinding
import com.appproject.project_jobsearch.ui.ScrapAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScrapActivity : AppCompatActivity() {

    private val scrapBinding by lazy {
        ActivityScrapBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        ScrapAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(scrapBinding.root)

        val scrapRepo = (application as ProjectApplication).scrapRepo

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        scrapBinding.rvScraps.adapter = adapter
        scrapBinding.rvScraps.layoutManager = layoutManager

        CoroutineScope(Dispatchers.IO).launch {
            val items = scrapRepo.getAllScraped()
            if(items.isNotEmpty()) {
                scrapBinding.tvNoScrap.visibility = View.INVISIBLE
                adapter.scraps = items
            } else {
                scrapBinding.tvNoScrap.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        }

        adapter.setOnItemClickListener(object : ScrapAdapter.OnItemClickListener {

            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@ScrapActivity, DetailActivity::class.java)
                intent.putExtra("id", adapter.scraps?.get(position)?.employmentId)
                startActivity(intent)
            }
        })

        scrapBinding.btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}