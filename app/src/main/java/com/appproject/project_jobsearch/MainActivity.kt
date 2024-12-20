package com.appproject.project_jobsearch

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.appproject.project_jobsearch.data.UserDao
import com.appproject.project_jobsearch.data.UserDatabase
import com.appproject.project_jobsearch.data.UserDto
import com.appproject.project_jobsearch.databinding.ActivityMainBinding
import com.appproject.project_jobsearch.ui.ItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var user : UserDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

        val userRepo = (application as UserApplication).userRepo

        // 애플리케이션 첫 실행 시 자동으로 user 데이터 생성
        CoroutineScope(Dispatchers.IO).launch {
            if (userRepo.currentUser == null) {
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
                userRepo.newUser(user)
            } else {
                // 아닐 경우 데이터 불러오기 진행
                    user = userRepo.currentUser

                    withContext(Dispatchers.Main) {
                        mainBinding.tvChat.text = "안녕하세요, ${user.nickname}님! 좋은 하루입니다."
                    }
            }
        }

        val adapter = ItemAdapter()
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        mainBinding.rvItems.adapter = adapter
        mainBinding.rvItems.layoutManager = layoutManager

        adapter.setOnItemClickListener(object: ItemAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val id = adapter.items?.jobs?.get(position)?.id

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
            val userRepo = (application as UserApplication).userRepo

            CoroutineScope(Dispatchers.IO).launch {
                user = userRepo.currentUser

                withContext(Dispatchers.Main) {
                    mainBinding.tvChat.text = "안녕하세요, ${user.nickname}님! 좋은 하루입니다."
                }
            }
        }
}