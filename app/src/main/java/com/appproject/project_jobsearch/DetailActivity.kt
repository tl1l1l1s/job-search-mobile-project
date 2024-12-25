package com.appproject.project_jobsearch

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.appproject.project_jobsearch.data.ScrapDto
import com.appproject.project_jobsearch.databinding.ActivityDetailBinding
import com.appproject.project_jobsearch.ui.JobsViewModel
import com.appproject.project_jobsearch.ui.JobsViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private val detailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val scrapRepo by lazy {
        (application as ProjectApplication).scrapRepo
    }

    private var isScraped = false
    lateinit var id: String
    private var locName: String = ""
    private lateinit var targetLoc: LatLng;
    private var employmentLink: String = ""

    val geocoder by lazy {
        Geocoder(this, Locale.getDefault())
    }
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(detailBinding.root)

        id = intent.getStringExtra("id")!!

        val jobsViewModel: JobsViewModel by viewModels {
            JobsViewModelFactory((application as ProjectApplication).jobsRepo)
        }

        jobsViewModel.getDetail(id)
        jobsViewModel.job.observe(this) { job ->
            if (job == null) {
                detailBinding.tvNoDetail.visibility = View.VISIBLE
                detailBinding.lnDetail.visibility = View.INVISIBLE
            } else {
                val dueDate = job?.expiration!!.split("T")[0]
                val leftDay = ChronoUnit.DAYS.between(ZonedDateTime.now().toLocalDate(), ZonedDateTime.parse(job?.expiration!!, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")).toLocalDate())

                detailBinding.apply {
                    tvTitle.text = job.position?.title
                    if (job?.closeType?.code.equals("1")) {
                        tvDday.text = "D - ${leftDay}"
                        tvExpiration.text = "${dueDate}+ ${job?.expiration!!.split("T")[1].substring(0, 4)}"
                    } else {
                        tvDday.text = "${job?.closeType?.closeTypeName}"
                        tvExpiration.text = "${job?.closeType?.closeTypeName}"
                    }
                    tvCompany.text = job.company?.companyDetail?.companyName
                    tvJobCd.text = job.position?.jobCode?.jobCdName
                    tvJobType.text = job.position?.jobType?.jobTypeName
                    tvSalary.text = job.salary?.salaryName
                    tvKeyword.text = job.keyword
                }

                employmentLink = "${job?.url}"
                locName = job.position?.location?.locationName.toString().split(",").last()
                    .replace(" &gt; ", " ")
                geocoder.getFromLocationName(locName, 1) { address ->
                    targetLoc = LatLng(address.get(0).latitude, address.get(0).longitude)

                    CoroutineScope(Dispatchers.Main).launch {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLoc, 10F))
                        addMarker(targetLoc, job.company?.companyDetail?.companyName ?: "회사", locName)
                    }
                }
            }
        }

        val mapReadyCallback = object : OnMapReadyCallback {
            override fun onMapReady(map: GoogleMap) {
                googleMap = map
            }
        }

        val mapFragment: SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapReadyCallback)

        CoroutineScope(Dispatchers.IO).launch {
            if (!scrapRepo.isNotScraped(id)) {
                isScraped = true

                withContext(Dispatchers.Main) {
                    detailBinding.btnScrap.setImageResource(R.drawable.scraped)
                }
            }
        }

        detailBinding.btnScrap.setOnClickListener {
            isScraped = !isScraped
            CoroutineScope(Dispatchers.IO).launch {
                if (isScraped) {
                    jobsViewModel.job.value?.let { job ->
                        scrapRepo.addScrap(
                            ScrapDto(
                                0, id,
                                job.company?.companyDetail?.companyName,
                                job.position?.jobCode?.jobCdName,
                                job.closeType?.code,
                                job.closeType?.closeTypeName,
                                job.expiration
                            )
                        )

                    }
                } else {
                    scrapRepo.deleteScrap(id)
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                updateBtnScrapImage()
            }
        }

        detailBinding.btnShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"

            intent.putExtra(Intent.EXTRA_TEXT, employmentLink)
            startActivity(Intent.createChooser(intent, null))

        }

        detailBinding.btnToInternet.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(employmentLink)))
        }

        detailBinding.btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun updateBtnScrapImage() {
        detailBinding.btnScrap.setImageResource(
            if (isScraped) R.drawable.scraped else R.drawable.scrap
        )
    }

    private lateinit var markerOptions: MarkerOptions
    private var centerMarker: Marker? = null

    private fun addMarker(targetLoc: LatLng, companyName: String, targetLocName: String) {
        markerOptions = MarkerOptions().apply {
            position(targetLoc)
            title(companyName)
            snippet("$targetLocName")
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        }

        centerMarker = googleMap.addMarker(markerOptions)
        centerMarker?.showInfoWindow()
    }

}