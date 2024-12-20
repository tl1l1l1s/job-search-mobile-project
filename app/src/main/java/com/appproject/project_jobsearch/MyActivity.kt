package com.appproject.project_jobsearch

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.data.JobCd
import com.appproject.project_jobsearch.data.JobCdData
import com.appproject.project_jobsearch.data.Region
import com.appproject.project_jobsearch.data.UserDao
import com.appproject.project_jobsearch.data.UserDatabase
import com.appproject.project_jobsearch.data.UserDto
import com.appproject.project_jobsearch.databinding.ActivityMyBinding
import com.appproject.project_jobsearch.ui.JobCdDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyActivity : AppCompatActivity() {

    val myBinding by lazy {
        ActivityMyBinding.inflate(layoutInflater)
    }

    val userDB: UserDatabase by lazy {
        UserDatabase.getDatabase(this)
    }

    val userDao: UserDao by lazy {
        userDB.userDao()
    }

    lateinit var user: UserDto;
    var selectedStock: Int? = null;
    var selectedJobType: Int? = null;
    var selectedRegion = BooleanArray(Region.regionList.size) { false }
    var selectedEducation = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(myBinding.root)

        CoroutineScope(Dispatchers.IO).launch {
            user = userDao.getUser()

            withContext(Dispatchers.Main) {
                if (!user.nickname.isNullOrEmpty()) {
                    myBinding.editNickname.setText(user.nickname)
                }
                if (!user.stock.isNullOrEmpty()) {
                    myBinding.btnStock.text = "현재 ${user.stock}에 상장한 기업을 선택 중 입니다."
                }
                if (!user.jobType.isNullOrEmpty()) {
                    myBinding.btnJobType.text =
                        "현재 ${resources.getStringArray(R.array.jobTypes)[Integer.parseInt(user.jobType!!) - 1]}의 근무 형태를 선택 중 입니다."
                }
                if (!user.locCd.isNullOrEmpty()) {
                    val selectedRegionNames = user.locCd!!.split("+").filter { it.isNotEmpty() }
                    Region.regionList.forEachIndexed { index, region ->
                        selectedRegion[index] = selectedRegionNames.contains(region.name)
                    }
                    myBinding.btnLocCd.text = "${selectedRegionNames.joinToString(" ")} 지역을 선택 중입니다."
                }
                if(!user.jobCd.isNullOrEmpty()) {
                    val selectedJobCdIds = user.jobCd!!.split("+").filter { it.isNotEmpty() }
                    val selectedIndices = mutableListOf<Int>()

                    JobCd.jobCdList.forEachIndexed { index, jobCdData ->
                        if (selectedJobCdIds.contains(jobCdData.id)) {
                            selectedIndices.add(index)
                        }
                    }
                    if (selectedIndices.isNotEmpty()) {
                        val selectedNames = selectedIndices.map { JobCd.jobCdList[it].name }
                        myBinding.btnJobCd.text = "${selectedNames.joinToString(" ")} 직무를 선택 중입니다."
                    }
                }
                if (user.eduLv != null) {
                    myBinding.btnEduLv.text =
                        "현재 선택한 학력 조건은 ${resources.getStringArray(R.array.educations)[user.eduLv!!]}입니다."
                }
                if (!user.sr.isNullOrEmpty()) {
                    myBinding.radioBtnExcept.setChecked(true)
                }
            }
        }

        myBinding.btnStock.setOnClickListener {
            AlertDialog.Builder(this).apply {
                title = "기업 형태 선택"
                setCancelable(true)

                setSingleChoiceItems(
                    resources.getStringArray(R.array.stocks),
                    selectedEducation
                ) { dialogInterface: DialogInterface?, idx: Int ->
                    selectedStock = idx
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    if (selectedStock != null) {
                        user.stock = resources.getStringArray(R.array.stocks)[selectedStock!!]
                        myBinding.btnStock.text =
                            "현재 ${resources.getStringArray(R.array.stocks)[selectedStock!!]}에 상장한 기업을 선택 중 입니다."
                    }
                }

                setNegativeButton("취소", null)
            }.show()
        }

        myBinding.btnJobType.setOnClickListener {
            AlertDialog.Builder(this).apply {
                title = "근무 형태/고용 형태 선택"
                setCancelable(true)

                setSingleChoiceItems(
                    resources.getStringArray(R.array.jobTypes),
                    selectedEducation
                ) { dialogInterface: DialogInterface?, idx: Int ->
                    selectedJobType = idx
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    if (selectedJobType != null) {
                        user.jobType = (selectedJobType!! + 1).toString()
                        myBinding.btnJobType.text =
                            "현재 ${resources.getStringArray(R.array.jobTypes)[selectedJobType!!]}의 근무 형태를 선택 중 입니다."
                    }
                }

                setNegativeButton("취소", null)
            }.show()
        }

        myBinding.btnLocCd.setOnClickListener {
            val regionNameArray = Region.regionList.map { it.name }.toTypedArray()

            AlertDialog.Builder(this).apply {
                title = "근무 지역 선택 (최대 3개)"
                setCancelable(true)

                setMultiChoiceItems(
                    regionNameArray,
                    selectedRegion
                ) { dialogInterface: DialogInterface?, idx: Int, isChecked: Boolean ->
                    if (isChecked && selectedRegion.count { it } > 3) {
                        selectedRegion[idx] = false
                        (dialogInterface as AlertDialog).listView.setItemChecked(idx, false)
                        Toast.makeText(this@MyActivity, "최대 3개까지 선택 가능합니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    if (selectedRegion.count { it } > 0) {
                        var data = StringBuilder()
                        var result = StringBuilder()

                        val selectedRegions = Region.regionList.filterIndexed { index, _ ->
                            selectedRegion[index]
                        }

                        selectedRegions.forEach { selectedReg ->
                            data.append(selectedReg.name + "+")
                            result.append(selectedReg.name + " ")
                        }
                        user.locCd = data.toString()
                        myBinding.btnLocCd.text = "${result} 지역을 선택 중입니다."
                    }
                }
            }.show()
        }

        myBinding.btnJobCd.setOnClickListener {
            val data = StringBuilder()
            val result = StringBuilder()

            JobCdDialog(this) { selectedJobCds ->
                selectedJobCds.listIterator().apply {
                    while (hasNext()) {
                        var temp = next()
                        data.append(temp.id + "+")
                        result.append(temp.name + " ")
                    }
                }

                user.jobCd = data.toString()
                myBinding.btnJobCd.text = "${result} 직무를 선택 중입니다."
            }.show()
        }

        myBinding.btnEduLv.setOnClickListener {
            AlertDialog.Builder(this).apply {
                title = "최종 학력 입력"
                setCancelable(true)

                setSingleChoiceItems(
                    resources.getStringArray(R.array.educations),
                    selectedEducation
                ) { dialogInterface: DialogInterface?, idx: Int ->
                    selectedEducation = idx
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    user.eduLv = selectedEducation
                    myBinding.btnEduLv.text =
                        "현재 선택한 학력 조건은 ${resources.getStringArray(R.array.educations)[selectedEducation]}입니다."
                }

                setNegativeButton("취소", null)
            }.show()
        }

        myBinding.btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                user.nickname = myBinding.editNickname.text.toString()
                if (myBinding.radioBtnExcept.isChecked) {
                    user.sr = "directhire"
                } else {
                    user.sr = null
                }
                userDao.updateUser(user)
            }
            finish()
        }

        myBinding.btnCancel.setOnClickListener {
            finish()
        }
    }
}