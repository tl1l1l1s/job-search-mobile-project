package com.appproject.project_jobsearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    val settingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(settingBinding.root)

        settingBinding.btnSave.setOnClickListener {
            // 저자왼 거 기반으로
        }

        settingBinding.btnCancel.setOnClickListener {
            finish()
        }
    }
}