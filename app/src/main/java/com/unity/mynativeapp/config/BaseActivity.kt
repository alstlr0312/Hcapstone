package com.unity.mynativeapp.config

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
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

    companion object {
        const val SHOW_LOADING = 0
        const val SHOW_TEXT_LOADING = 1
        const val DISMISS_LOADING = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    }

    fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog(context: Context, title: String = "") {
        loadingDialog = LoadingDialog(context, title)
        loadingDialog.show()
    }

    fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    fun logout(memberDelete: Boolean = false) {

        MyApplication.prefUtil.removeString(X_ACCESS_TOKEN)
        MyApplication.prefUtil.removeString(X_REFRESH_TOKEN)

        if(memberDelete){ // 회원 탈퇴
            MyApplication.prefUtil.removeString("id")
            MyApplication.prefUtil.removeString("username")
        }

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

    // 화면 터치시 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }
    fun hideKeyBoard(){
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    // 키보드 보이기
    fun showKeyBoard(edtText: EditText){
        keyBoardIsShowing = inputMethodManager.showSoftInput(edtText, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }

    // alert dialog
    fun showAlertDialog(message: String){
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.notification))
            .setMessage(message)
            .setPositiveButton(getString(R.string.confirm)) { p0, _ -> p0.dismiss()}
            .create()
        dialog.show()
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