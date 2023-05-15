package com.unity.mynativeapp.config

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.util.LoadingDialog
import com.unity.mynativeapp.util.X_ACCESS_TOKEN
import com.unity.mynativeapp.util.X_REFRESH_TOKEN
import kotlin.system.exitProcess

abstract class BaseActivity<B: ViewBinding>(private val inflate: (LayoutInflater) -> B):
    AppCompatActivity() {
    protected lateinit var binding: B
        private set

    lateinit var lodingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog(context: Context) {
        lodingDialog = LoadingDialog(context)
        lodingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (lodingDialog.isShowing) {
            lodingDialog.dismiss()
        }
    }

    fun logout() {
        MyApplication.prefUtil.removeString(X_ACCESS_TOKEN)
        MyApplication.prefUtil.removeString(X_REFRESH_TOKEN)

        try {
            startActivity(
                Intent.makeRestartActivityTask(
                    packageManager.getLaunchIntentForPackage(packageName)?.component
                )
            )
            exitProcess(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getRealPathFromUri(uri: Uri): String {
        val buildName = Build.MANUFACTURER
        if(buildName.equals("Xiaomi")){
            return uri.path!!
        }
        var columnIndex = 0
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, proj, null, null, null)
        if(cursor!!.moveToFirst()){
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        val result = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

}