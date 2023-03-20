package com.sobi.replantation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sobi.replantation.R
import com.sobi.replantation.adapter.SerahTerimaListAdapter
import com.sobi.replantation.databinding.ActivityListSerahTerimaBinding
import com.sobi.replantation.domain.model.SerahTerima
import com.sobi.replantation.viewmodel.ListSerahTerimaViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ListSerahTerimaActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val paramMemberId = "param_member_id"
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var serahTerimas = mutableListOf<SerahTerima>()

    private val vm: ListSerahTerimaViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(ListSerahTerimaViewModel::class.java)
    }

    lateinit var serahTerimaListAdapter: SerahTerimaListAdapter
    private var _binding: ActivityListSerahTerimaBinding? = null
    private val binding get() = _binding!!

    private var memberId = 0

    val logFormList = listOf("Sengon", "Mahoni", "Jati")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        _binding = ActivityListSerahTerimaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        memberId = intent.getIntExtra(paramMemberId, 0)

        serahTerimaListAdapter= SerahTerimaListAdapter(this)
        binding.rvListTerima.apply {
            adapter = serahTerimaListAdapter
            layoutManager = LinearLayoutManager(this@ListSerahTerimaActivity)
        }

        binding.tbListSerahTerima.setNavigationIcon(R.drawable.ic_arrow_back_white)
        binding.tbListSerahTerima.setNavigationOnClickListener {
            onBackPressed()
        }



        vm.serahTerimaData.observe(this, Observer(::loadAllData))

        launch {
            vm.loadAllSerahTerima(memberId)
        }
    }

    private fun loadAllData(serahTerima: List<SerahTerima>?){
        serahTerimas = serahTerima?.toMutableList() ?: mutableListOf()
        serahTerimaListAdapter.addData(serahTerimas)
        serahTerimaListAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        launch {
            vm.loadAllSerahTerima(memberId)
        }
    }

    fun editSerahTerima(serahTerimaId: Int){
        launch {
            val serahTerima = vm.loadDataDetailTerima(memberId, serahTerimaId)
                showSerahTerimaPopUp(serahTerima)
        }
    }

    private fun showSerahTerimaPopUp(serahTerima: SerahTerima?) {
        serahTerima ?: return
        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()
        val view = layoutInflater.inflate(R.layout.alert_isian_serah_terima, null)
        val button = view.findViewById<Button>(R.id.btn_save)
        val spinner = view.findViewById<Spinner>(R.id.spn_species)
        val date = view.findViewById<TextView>(R.id.date)
        val jumlah = view.findViewById<EditText>(R.id.jumlah_bibit)
        val keterangan = view.findViewById<EditText>(R.id.keterangan)
        val warning = view.findViewById<TextView>(R.id.tv_warning)
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, logFormList)
        spinner.adapter = adapter


        jumlah.setText(serahTerima.totalBibit.toString())
        keterangan.setText(serahTerima.description)
        date.text = serahTerima.createdOn
        spinner.setSelection(serahTerima.speciesId)




        builder.setView(view)
        button.setOnClickListener {
            if (jumlah.text.toString()==null||jumlah.text.toString().isEmpty()){
                warning.text = "Mohon isi jumlah bibitnya"
            }else{
                launch {
                    vm.updateData(serahTerima.copy(totalBibit = jumlah.text.toString().toInt(), description = keterangan.text.toString(), speciesId = spinner.selectedItemPosition),memberId)
                }
                builder.dismiss()
            }

        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()

    }

}