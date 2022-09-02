package com.sobi.replantation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sobi.replantation.R
import com.sobi.replantation.databinding.ActivityLoginBinding
import com.sobi.replantation.databinding.ActivityMemberDetailBinding
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.fitur.PhotoViewActivity
import com.sobi.replantation.viewmodel.AreaDetailViewModel
import com.sobi.replantation.viewmodel.MemberDetailViewModel
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.reflect.Member
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MemberDetailActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val paramMemberId = "param_member_id"

    }

    private var _binding: ActivityMemberDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val vm: MemberDetailViewModel by lazy { ViewModelProviders.of(this, viewModelFactory).get(
        MemberDetailViewModel::class.java) }

    var memberId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMemberDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        job = Job()
        AndroidInjection.inject(this)

        memberId = intent.getIntExtra(paramMemberId, 0)

        binding.tbDetailMember.title = "Detail Anggota"
        binding.tbDetailMember.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.tbDetailMember.setNavigationOnClickListener {
            onBackPressed()
        }

        vm.assignmentData.observe(this, Observer(::onDetailMember))
        launch {
            vm.loadDetail(memberId)
        }
    }

    fun onDetailMember(member: Assignment?) {
        if (member == null) return

        val photoMember = if (member.photoUrl!= null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${member.photoUrl}"
        else null
        Picasso.get().load(photoMember)
            .placeholder(R.drawable.avatar)
            .resize(300, 300)
            .error(R.drawable.avatar).into(binding.imgPhotoMember)
        if (photoMember != null) {
            binding.imgPhotoMember.setOnClickListener {
                startActivity(Intent(this, PhotoViewActivity::class.java).apply {
                    putExtra(PhotoViewActivity.paramImageUrl, photoMember)
                })
            }
        }

        val photoId = if (member.idPhotoUrl != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${member.idPhotoUrl}"
        else null
        Picasso.get().load(photoId)
            .placeholder(R.drawable.avatar)
            .resize(300, 300)
            .error(R.drawable.avatar).into(binding.imgPhotoIdcard)
        if (photoId!= null) {
            binding.imgPhotoIdcard.setOnClickListener {
                startActivity(Intent(this, PhotoViewActivity::class.java).apply {
                    putExtra(PhotoViewActivity.paramImageUrl, photoId)
                })
            }
        }

        val photoStatement = if (member.statementPhotoUrl != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${member.statementPhotoUrl}"
        else null
        Picasso.get().load(photoStatement)
            .placeholder(R.drawable.avatar)
            .resize(300, 300)
            .error(R.drawable.avatar).into(binding.imgPhotoDesc)
        if (photoId!= null) {
            binding.imgPhotoDesc.setOnClickListener {
                startActivity(Intent(this, PhotoViewActivity::class.java).apply {
                    putExtra(PhotoViewActivity.paramImageUrl, photoStatement)
                })
            }
        }

        val photoMembership = if (member.membershipPhoto != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${member.membershipPhoto}"
        else null
        Picasso.get().load(photoMembership)
            .placeholder(R.drawable.avatar)
            .resize(300, 300)
            .error(R.drawable.avatar).into(binding.imgPhotoMembership)
        if (photoMembership!= null) {
            binding.imgPhotoMembership.setOnClickListener {
                startActivity(Intent(this, PhotoViewActivity::class.java).apply {
                    putExtra(PhotoViewActivity.paramImageUrl, photoMembership)
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