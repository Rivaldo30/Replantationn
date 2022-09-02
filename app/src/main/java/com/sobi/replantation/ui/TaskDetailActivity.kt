package com.sobi.replantation.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sobi.replantation.R
import com.sobi.replantation.adapter.AreaListAdapter
import com.sobi.replantation.databinding.ActivityTaskDetailBinding
import com.sobi.replantation.domain.model.Area
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.domain.model.BuktiSerahTerima
import com.sobi.replantation.viewmodel.TaskDetailViewModel
import com.sobi.replantation.viewmodel.TaskListViewModel
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TaskDetailActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val paramMemberId = "param_member_id"
        const val paramMemberName = "param_member_name"
    }

    val REQUEST_CODE = 200
    var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654


    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val vm: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TaskDetailViewModel::class.java)
    }

    private var memberName = ""
    private var memberId = 0
    private var totalPhotos = 0
    private var totalBibit = 0
    private var totalTerima = 0
    private var statusSerahTerima = 0

    var areas = mutableListOf<Area>()

    private var _binding: ActivityTaskDetailBinding? = null
    lateinit var alertBinding: TaskListViewModel
    private val binding get() = _binding!!

    lateinit var areaListAdapter: AreaListAdapter

    val logConditionList = listOf("Sengon", "Mahoni", "Jati")

    val dateFormat = SimpleDateFormat("dd-MM-yyyy")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        _binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        memberId = intent.getIntExtra(paramMemberId, 0)
        memberName = intent.getStringExtra(paramMemberName) ?: ""

        Log.d("member", "ids= $memberId")
        job = Job()

        binding.tbDetailMember.title = memberName
        binding.tbDetailMember.setNavigationIcon(R.drawable.ic_arrow_back_black)
        binding.tbDetailMember.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.tbDetailMember.title = memberName

        binding.tvDetailMember.paintFlags =
            binding.tvDetailMember.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.toDetailBibit.paintFlags =
            binding.toDetailBibit.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        areaListAdapter = AreaListAdapter(this)
        binding.rvAreas.apply {
            adapter = areaListAdapter
            layoutManager = LinearLayoutManager(this@TaskDetailActivity)
        }

        vm.assignmentData.observe(this, Observer(::updateDetail))
        vm.areaData.observe(this, Observer(::updateAreaDetails))
        vm.totalTerima.observe(this, Observer(::showTerima))
        vm.buktiDokumenData.observe(this, Observer(::showDokumentasi))

        permission()

        launch {
            vm.getTotalTerima(memberId)
            vm.loadDetail(memberId)
        }


        binding.addSerahTerima.setOnClickListener {
            showSerahTerimaPopUp()
        }

        binding.ambilFoto.setOnClickListener {
            showPopUpPickPicture()
        }

        binding.finishSerahTerima.setOnClickListener {
            finishSerahTerimaPopUp()
        }

        binding.tvDetailMember.setOnClickListener {
            startActivity(Intent(this, MemberDetailActivity::class.java).apply {
                putExtra(MemberDetailActivity.paramMemberId, memberId)
            })
        }

        binding.toDetailBibit.setOnClickListener {
            startActivity(Intent(this, ListSerahTerimaActivity::class.java).apply {
                putExtra(ListSerahTerimaActivity.paramMemberId, memberId)
            })
        }


    }

    private fun updateDetail(assignment: Assignment?) {
        if (assignment == null) return

        totalBibit = assignment.totalBibit
        statusSerahTerima = assignment.finishedSerahTerima

        val photoUrl =
            if (assignment.photoUrl != null) "https://s3-ap-southeast-1.amazonaws.com/sobi-server/prod/image/${assignment.photoUrl}"
            else null
        Picasso.get().load(photoUrl)
            .placeholder(R.drawable.avatar)
            .resize(300, 100)
            .onlyScaleDown()
            .error(R.drawable.avatar).into(binding.ivMember)

        binding.tvNameMember.text = assignment.memberName
        binding.tvNumberMember.text = assignment.memberNo
        binding.tvCountAreas.text = "${assignment.areaCount}"
        binding.tvCountBibit.text = "${assignment.totalBibit.toString()}"

        if (assignment.finishedSerahTerima==1){
            binding.addSerahTerima.backgroundTintList  = AppCompatResources.getColorStateList(this, R.color.grey)
            binding.addSerahTerima.isEnabled = false
            binding.ambilFoto.setBackgroundResource(R.drawable.background_nonaktif)
            binding.ambilFoto.setTextColor(Color.GRAY)
            binding.ambilFoto.isEnabled = false
            binding.finishSerahTerima.setBackgroundResource(R.drawable.background_blue)
            binding.finishSerahTerima.text = "Batalkan Selesai\nSerah Terima"
            binding.finishSerahTerima.isEnabled = true
            binding.finishSerahTerima.setTextColor(Color.WHITE)
    } else{
            binding.addSerahTerima.backgroundTintList  = AppCompatResources.getColorStateList(this, R.color.colorGreen)
            binding.addSerahTerima.isEnabled = true
            binding.ambilFoto.setBackgroundResource(R.drawable.background_red)
            binding.ambilFoto.setTextColor(Color.WHITE)
            binding.ambilFoto.isEnabled = true
        }
    }

    override fun onResume() {
        super.onResume()
        launch {
            vm.loadDetail(memberId)
            vm.getTotalTerima(memberId)
        }
    }

    private fun updateAreaDetails(listAreas: List<Area>?) {
        areas = listAreas?.toMutableList() ?: mutableListOf()
        areaListAdapter.addData(areas)
        areaListAdapter.notifyDataSetChanged()
    }

    private fun showTerima(result: Int?) {
        result ?: return
        totalTerima = result
        binding.tvCountTerima.text = result.toString()
        if (totalTerima >= totalBibit && totalPhotos > 0
        ) {
            binding.finishSerahTerima.setBackgroundResource(R.drawable.background_yellow)
            binding.finishSerahTerima.setTextColor(Color.WHITE)
            binding.finishSerahTerima.isEnabled = true
        } else {
            binding.finishSerahTerima.setBackgroundResource(R.drawable.background_nonaktif)
            binding.finishSerahTerima.setTextColor(Color.GRAY)
            binding.finishSerahTerima.isEnabled = false
        }
    }

    private fun showDokumentasi(result: List<BuktiSerahTerima>?) {
        result ?: return
        totalPhotos = result.size
    }

    private fun showSerahTerimaPopUp() {
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.alert_isian_serah_terima, null)
        val button = view.findViewById<Button>(R.id.btn_save)
        val spinner = view.findViewById<Spinner>(R.id.species)
        val date = view.findViewById<TextView>(R.id.date)
        date.text = dateFormat.format(Date(System.currentTimeMillis()))
        val jumlah = view.findViewById<EditText>(R.id.jumlah_bibit)
        val keterangan = view.findViewById<EditText>(R.id.keterangan)
        val warning = view.findViewById<TextView>(R.id.tv_warning)


            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, logConditionList)
            spinner.adapter = adapter

        builder.setView(view)
        button.setOnClickListener {
           if (jumlah.text.toString()==null||jumlah.text.toString().isEmpty()){
               warning.text = "Mohon isi jumlah bibitnya"
           }else{
               launch {
                   vm.saveSerahTerima(
                       memberId,
                       spinner.selectedItemPosition,
                       jumlah.text.toString().toInt(),
                       keterangan.text.toString(),
                       date.text.toString()
                   )
               }
               builder.dismiss()
           }

        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }

    private fun finishSerahTerimaPopUp() {

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()

        val view = layoutInflater.inflate(R.layout.alert_konfirmasi_layout, null)
        builder.setView(view)
        val buttonOk = view.findViewById<Button>(R.id.btn_ok)
        val buttonNo = view.findViewById<Button>(R.id.btn_cancel)

        buttonOk.setOnClickListener {
            if (statusSerahTerima==1){
                launch {
                    vm.updateFinishedSerahTerima(0, memberId)
                }
                builder.dismiss()
            } else{
                launch {
                    vm.updateFinishedSerahTerima(1, memberId)
                }
                builder.dismiss()
                onBackPressed()
            }


        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }

    fun showPopUpPickPicture() {
        val dialog = BottomSheetDialog(this, R.style.ButtomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.pop_up_pick_picture, null)
        dialog.setContentView(view)

        val toGaleri = view.findViewById<CardView>(R.id.toGallery)
        val toCamera = view.findViewById<CardView>(R.id.toCamera)

        toGaleri.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId){
                    R.id.bukti -> openGalleryForImages()
                    R.id.dokumen -> openGalleryForImages()

                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()

        }
        toCamera.setOnClickListener {
            val popupMenu = PopupMenu(this, it)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId){
                    R.id.bukti -> openCamera()
                    R.id.dokumen -> openCamera()

                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }




        dialog.show()

    }

    fun goToDetailArea(areaId: Int, areaName: String) {
        startActivity(Intent(this, AreaDetailActivity::class.java).apply {
            putExtra(AreaDetailActivity.paramAreaId, areaId)
            putExtra(AreaDetailActivity.paramAreaName, areaName)
            putExtra(AreaDetailActivity.paramMemberId, memberId)
        })
    }

    private fun openGalleryForImages() {

        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures"), REQUEST_CODE
            )
        } else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    fun permission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permission, 112)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {

            // if multiple images are selected
            if (data?.getClipData() != null) {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Apakah anda yakin akan menambahkan dokumentasi ini ?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialog, id ->
                        launch {
                            vm.saveImage(memberId)
                        }
                        dialog.dismiss()
                    })
                    .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Input Dokumentasi")
                alert.show()
                var count = data.clipData?.itemCount

                for (i in 0..count!! - 1) {
                    var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }
                launch {
                    vm.saveImage(memberId)
                }

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                launch {
                    vm.saveImage(memberId)
                }
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

            }
        }
        else  if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            launch {
                vm.saveImage(memberId)
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Ambil gambar")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Dari kamera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

}