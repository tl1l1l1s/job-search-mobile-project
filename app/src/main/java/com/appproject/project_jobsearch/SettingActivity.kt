package com.appproject.project_jobsearch

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.appproject.project_jobsearch.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private val settingBinding by lazy {
        ActivitySettingBinding.inflate(layoutInflater)
    }

    private val alarmManager : AlarmManager? by lazy {
        getSystemService(ALARM_SERVICE) as AlarmManager
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var pendingIntent : PendingIntent;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(settingBinding.root)

        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        val isAlarmEnabled = sharedPreferences.getBoolean("alarm_enabled", true)
        settingBinding.switchAlarm.isChecked = isAlarmEnabled

        checkPermission()
        createNotificationChannel()
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 8)
        }
        val alarmIntent = Intent(this, AlarmBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        settingBinding.btnSave.setOnClickListener {
            if(!settingBinding.switchAlarm.isChecked) {
                alarmManager?.cancel(pendingIntent)
            } else {
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            sharedPreferences.edit().putBoolean("alarm_enabled", settingBinding.switchAlarm.isChecked).apply()
            finish()
        }

        settingBinding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel("notification channel", "Job search", NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(applicationContext, "권한이 승인되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "권한이 거절되었습니다. 알림을 받아보시길 원한다면 권한을 추가해주세요.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}