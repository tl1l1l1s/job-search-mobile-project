package com.appproject.project_jobsearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appproject.project_jobsearch.data.UserDto
import com.appproject.project_jobsearch.databinding.ActivityMainBinding
import com.appproject.project_jobsearch.ui.ItemAdapter
import com.appproject.project_jobsearch.ui.JobsViewModel
import com.appproject.project_jobsearch.ui.JobsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    var user: UserDto? = null

    private val jobsViewModel: JobsViewModel by viewModels {
        JobsViewModelFactory((application as ProjectApplication).jobsRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        val userRepo = (application as ProjectApplication).userRepo
        CoroutineScope(Dispatchers.IO).launch {
            user = userRepo.getCurrentUser()
            if (user == null) {
                user = UserDto(
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                )
                userRepo.newUser(user!!)
            } else {
                withContext(Dispatchers.Main) {
                    mainBinding.tvChat.text = "안녕하세요, ${user!!.nickname}님! 좋은 하루입니다."
                }
            }
        }

        val adapter = ItemAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        mainBinding.rvItems.adapter = adapter
        mainBinding.rvItems.layoutManager = layoutManager

        jobsViewModel.jobs.observe(this) { jobs ->

            if(jobs.count == null) {
                mainBinding.tvNoResult.visibility = View.VISIBLE
            } else {
                mainBinding.tvNoResult.visibility = View.INVISIBLE
                adapter.items = jobs.job
            }
            adapter.notifyDataSetChanged()
        }


        adapter.setOnItemClickListener(object : ItemAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val id = adapter.items?.get(position)?.id

                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })

        mainBinding.btnToProfile.setOnClickListener {
            val intent = Intent(this, MyActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnToScrap.setOnClickListener {
            val intent = Intent(this, ScrapActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnToSetting.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val userRepo = (application as ProjectApplication).userRepo

        CoroutineScope(Dispatchers.IO).launch {
            user = userRepo.getCurrentUser()
            jobsViewModel.getList(user!!)

            withContext(Dispatchers.Main) {
                mainBinding.tvChat.text = "안녕하세요, ${user!!.nickname}님! 좋은 하루입니다."
            }

        }
    }

}