package com.appproject.project_jobsearch

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.data.JobCd
import com.appproject.project_jobsearch.data.Region
import com.appproject.project_jobsearch.data.UserDto
import com.appproject.project_jobsearch.databinding.ActivityMyBinding
import com.appproject.project_jobsearch.ui.JobCdDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyActivity : AppCompatActivity() {

    private val myBinding by lazy {
        ActivityMyBinding.inflate(layoutInflater)
    }

    private lateinit var user: UserDto;
    private var selectedStock = 0;
    private var selectedJobType = 0;
    private var selectedRegion = 0;
    private var selectedEducation = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(myBinding.root)
        val userRepo = (application as ProjectApplication).userRepo

        CoroutineScope(Dispatchers.IO).launch {
            user = userRepo.getCurrentUser()!!

            withContext(Dispatchers.Main) {
                if (!user.nickname.isNullOrEmpty()) {
                    myBinding.editNickname.setText(user.nickname)
                }
                if (!user.stock.isNullOrEmpty()) {
                    myBinding.btnStock.text = "현재 ${user.stock}에 상장한 기업을 선택 중 입니다."
                }
                if (!user.jobType.isNullOrEmpty()) {
                    myBinding.btnJobType.text =
                        "현재 ${resources.getStringArray(R.array.jobTypes)[Integer.parseInt(user.jobType!!)]}의 근무 형태를 선택 중 입니다."
                }
                if (!user.locCd.isNullOrEmpty()) {
                    myBinding.btnLocCd.text =
                        "${Region.regionList[Region.regionList.indexOfFirst { it.id == user.locCd }].name} 지역을 선택 중입니다."
                } else if(user.locCd.equals("null")) {
                    myBinding.btnLocCd.text = "전체 지역을 선택 중입니다."
                }
                if (!user.jobCd.isNullOrEmpty()) {
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
                    selectedStock
                ) { dialogInterface: DialogInterface?, idx: Int ->
                    selectedStock = idx
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    if (selectedStock != null) {
                        if (selectedStock == 0) {
                            user.stock = null
                        } else {
                            user.stock = resources.getStringArray(R.array.stocks)[selectedStock!!]
                            myBinding.btnStock.text =
                                "현재 ${resources.getStringArray(R.array.stocks)[selectedStock!!]}에 상장한 기업을 선택 중 입니다."

                        }
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
                    selectedJobType
                ) { dialogInterface: DialogInterface?, idx: Int ->
                    selectedJobType = idx
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    if(selectedJobType > 0) {
                        user.jobType = (selectedJobType).toString()
                        myBinding.btnJobType.text =
                            "현재 ${resources.getStringArray(R.array.jobTypes)[selectedJobType!!]}의 근무 형태를 선택 중 입니다."
                    } else {
                        user.jobType = null
                    }
                }

                setNegativeButton("취소", null)
            }.show()
        }

        myBinding.btnLocCd.setOnClickListener {
            val regionNameArray = Region.regionList.map { it.name }.toTypedArray()

            AlertDialog.Builder(this).apply {
                title = "근무 지역 선택"
                setCancelable(true)

                setSingleChoiceItems(
                    regionNameArray,
                    selectedRegion
                ) { dialogInterface: DialogInterface?, idx: Int ->
                    selectedRegion = idx
                }

                setPositiveButton("확인") { dialogInterface: DialogInterface?, idx: Int ->
                    user.locCd = Region.regionList[selectedRegion].id
                    myBinding.btnLocCd.text = "${Region.regionList[selectedRegion].name} 지역을 선택 중입니다."
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
                userRepo.updateUser(user)
            }
            finish()
        }

        myBinding.btnCancel.setOnClickListener {
            finish()
        }
    }
}