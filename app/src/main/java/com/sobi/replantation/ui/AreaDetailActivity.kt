package com.sobi.replantation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sobi.replantation.R
import com.sobi.replantation.databinding.ActivityAreaDetailBinding
import com.sobi.replantation.databinding.ActivityMemberDetailBinding
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.fitur.PhotoViewActivity
import com.sobi.replantation.viewmodel.AreaDetailViewModel
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AreaDetailActivity : AppCompatActivity(), CoroutineScope {
    companion object {
        const val paramAreaName = "param_area_name"
        const val paramAreaId = "param_area_id"
        const val paramMemberId = "param_member_id"

    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val vm: AreaDetailViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(AreaDetailViewModel::class.java) }


    var areaId = -1
    var areaName = ""
    var memberId = -1

    private var _binding: ActivityAreaDetailBinding? = null
    private val binding get() = _binding!!

    lateinit var sertifImage: ImageView
    lateinit var sketsaImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAreaDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()
        AndroidInjection.inject(this)

        memberId = intent.getIntExtra(paramMemberId, 0)
        areaId = intent.getIntExtra(paramAreaId, 0)
        areaName = intent.getStringExtra(paramAreaName) ?: ""

        binding.tbDetailArea.title = "Detail Lahan"
        binding.tbDetailArea.subtitle = areaName
        binding.tbDetailArea.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.tbDetailArea.setNavigationOnClickListener {
            onBackPressed()
        }

        vm.areaData.observe(this, Observer(::onDetailLoaded))
        launch {
            vm.loadDetail(memberId, areaId)
        }
    }

    fun onDetailLoaded(area: Area?) {
        if (area == null) return
        val photoSertif = if (area.photoSertifUrl != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${area.photoSertifUrl}"
        else null
        Picasso.get().load(photoSertif)
            .placeholder(R.drawable.avatar)
            .resize(300, 300)
            .error(R.drawable.avatar).into(binding.imgSertif)
        if (photoSertif != null) {
            binding.imgSertif.setOnClickListener {
                startActivity(Intent(this, PhotoViewActivity::class.java).apply {
                    putExtra(PhotoViewActivity.paramImageUrl, photoSertif)
                })
            }
        }
        val photoSketsa = if (area.photoSketsaUrl != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${area.photoSketsaUrl}"
        else null
        Picasso.get().load(photoSketsa)
            .placeholder(R.drawable.avatar)
            .resize(300, 300)
            .error(R.drawable.avatar).into(binding.imgSketsa)
        if (photoSketsa != null) {
            binding.imgSketsa.setOnClickListener {
                startActivity(Intent(this, PhotoViewActivity::class.java).apply {
                    putExtra(PhotoViewActivity.paramImageUrl, photoSketsa)
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (job.isActive) {
            job.cancel()
        }
    }

}