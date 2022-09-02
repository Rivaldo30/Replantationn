package com.sobi.replantation.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.sobi.replantation.R
import com.sobi.replantation.databinding.ActivityLoginBinding
import com.sobi.replantation.viewmodel.LoginViewModel
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val vm: LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }
    private var deviceId = ""

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        job = Job()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deviceId = getDeviceId(this)

        binding.etUsername.filters = arrayOf(InputFilter.LengthFilter(20))
        binding.etPassword.filters = arrayOf(InputFilter.LengthFilter(20))

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            login(username, password)
        }
    }

    override fun onStart() {
        super.onStart()
        val userData = vm.getAccountData()
        if (userData != null) {
            startActivity(Intent(this, TaskListActivity::class.java).apply {
                putExtra(
                    TaskListActivity.paramAccountName,
                    "${userData.firstName} ${userData.lastName}"
                )
            })
            finish()
        }
    }

    private fun login(username: String, password: String) {
        launch {
            if (!vm.isUsernamePasswordValid(username, password)) {
                AlertDialog.Builder(this@LoginActivity)
                    .setTitle("Username/Password Kurang")
                    .setMessage("Harap isi username dan password anda")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })

                    .show()
                return@launch
            }
            val loadingView = getAlertDialog(this@LoginActivity, R.layout.progressbar_layout, false)
            loadingView.show()
            try {
                val userData = vm.login(username, password, deviceId)
                if (userData?.firstName != "") {
                    startActivity(
                        Intent(this@LoginActivity, TaskListActivity::class.java).apply {
                            putExtra(
                                TaskListActivity.paramAccountName,
                                "${userData?.firstName} ${userData?.lastName}"
                            )
                        }
                    )
                    loadingView.dismiss()
                    finish()
                } else {
                    AlertDialog.Builder(this@LoginActivity)
                        .setTitle("Login gagal")
                        .setMessage("Akun ini telah digunakan di device lain. Mohon login dengan akun lain")
                        .setPositiveButton(
                            "Ya",
                            DialogInterface.OnClickListener { dialogInterface, i ->

                            })

                        .show()
                }


            } catch (e: Exception) {
                e.printStackTrace()
                loadingView.dismiss()
                AlertDialog.Builder(this@LoginActivity)
                    .setTitle("Login gagal")
                    .setMessage("Gagal login ke dalam sistem, Cobalah sekali lagi ${e.message}")
                    .setPositiveButton("Ya", DialogInterface.OnClickListener { dialogInterface, i ->

                    })
                    .show()
            } finally {
                loadingView.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (job.isActive) {
            job.cancel()
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getAlertDialog(
        context: Context,
        layout: Int,
        setCancellationOnTouchOutside: Boolean
    ): android.app.AlertDialog {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        val customLayout: View =
            layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        return dialog
    }
}

class LoginFailedExeption : Exception()