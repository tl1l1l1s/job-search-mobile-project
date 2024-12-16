package com.appproject.project_jobsearch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val mainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)

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
}