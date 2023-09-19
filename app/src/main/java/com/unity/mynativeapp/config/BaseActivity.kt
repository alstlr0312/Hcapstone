package com.unity.mynativeapp.config

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.network.util.LoadingDialog
import com.unity.mynativeapp.network.util.X_ACCESS_TOKEN
import com.unity.mynativeapp.network.util.X_REFRESH_TOKEN
import kotlin.system.exitProcess

abstract class BaseActivity<B: ViewBinding>(private val inflate: (LayoutInflater) -> B):
    AppCompatActivity() {
    protected lateinit var binding: B
        private set
    lateinit var loadingDialog: LoadingDialog
    lateinit var inputMethodManager: InputMethodManager
    var keyBoardIsShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    }

    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog(context: Context) {
        loadingDialog = LoadingDialog(context)
        loadingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
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

    // 키보드 보이기
    fun hideKeyBoard(){
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun showKeyBoard(edtText: EditText){
        keyBoardIsShowing = inputMethodManager.showSoftInput(edtText, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }


    // 키보드 숨기기


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