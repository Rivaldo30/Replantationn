package com.sobi.replantation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sobi.replantation.R
import com.sobi.replantation.adapter.TaskListAdapter
import com.sobi.replantation.databinding.ActivityTaskListBinding
import com.sobi.replantation.domain.model.Assignment
import com.sobi.replantation.viewmodel.TaskListViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class TaskListActivity : AppCompatActivity(), CoroutineScope {

    companion object {
        const val paramAccountName = "param_account_name"
    }

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    var tasks = mutableListOf<Assignment>()

    private var userName = ""
    private var enableUpload = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val vm: TaskListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TaskListViewModel::class.java)
    }

    lateinit var taskListAdapter: TaskListAdapter
    private var _binding: ActivityTaskListBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        _binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        userName = intent.getStringExtra(paramAccountName) ?: ""

        binding.tbTaskList.title = "Serah Terima"
        binding.tbTaskList.subtitle = userName

        taskListAdapter = TaskListAdapter(this)
        binding.rvAssignment.apply {
            adapter = taskListAdapter
            layoutManager = LinearLayoutManager(this@TaskListActivity)
        }

        binding.fabRefresh.setOnClickListener {
            launch {
                vm.refreshAssignments()
            }
        }

        vm.assignments.observe(this, Observer(::updateAssignments))

        launch {
            try {
                vm.loadAssignments()
            } catch (e: Exception) {
                Toast.makeText(this@TaskListActivity, "gagal load data", Toast.LENGTH_SHORT).show()

            }
        }

        binding.searchData.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(query: String?): Boolean {
                if (query == null || query.isEmpty() || query.length >= 0) {
                    launch {
                        vm.searchAssignments(query)
                    }
                }
                return false
            }

        })
    }

    private fun updateAssignments(assignments: List<Assignment>?) {
        tasks = assignments?.toMutableList() ?: mutableListOf()
        taskListAdapter.addData(tasks)
        taskListAdapter.notifyDataSetChanged()
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_edit_item, menu)
//    }

    override fun onResume() {
        super.onResume()
        launch {
            vm.loadAssignments()
        }
    }

    fun popUpPdf() {

        val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
            .create()

        val view = layoutInflater.inflate(R.layout.pop_up_input_pdf_layout, null)
        builder.setView(view)
        val buttonOk = view.findViewById<Button>(R.id.btn_ok)
        val buttonNo = view.findViewById<Button>(R.id.btn_cancel)

        buttonOk.setOnClickListener {
            builder.dismiss()
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()

        buttonNo.setOnClickListener {
            builder.dismiss()
        }
    }


    fun goToDetailMember(memberId : Int, memberName : String){

        val intent = Intent(this, TaskDetailActivity::class.java)
        intent.putExtra(TaskDetailActivity.paramMemberName, memberName)
        intent.putExtra(TaskDetailActivity.paramMemberId, memberId)
        Log.d("member","id = $memberId")
        startActivity(intent)
    }
}