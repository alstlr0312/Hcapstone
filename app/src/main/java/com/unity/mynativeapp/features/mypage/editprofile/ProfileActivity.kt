package com.unity.mynativeapp.features.mypage.editprofile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.decodeBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityProfileBinding
import com.unity.mynativeapp.features.diary.DiaryActivity
import com.unity.mynativeapp.features.postwrite.PostWriteActivity
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.network.util.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private var firstStart = true
    lateinit var viewModel: ProfileViewModel
    private var currentPw = ""
    private var changeToBaseImg: Boolean = false
    private var changedImgPath: String?= null // null: 기본 이미지, not null: 다른 이미지로 변경
    //private var changedImgUri: Uri? = null
    var mediaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let{
                changedImgPath = getRealPathFromUri(it)
                //changedImgPath = it.path
                binding.ivProfileImg.setImageURI(it)
                //val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, it))
                //saveProfileImg(bitmap)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ProfileViewModel(this)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){
        binding.tilChangedPw.visibility = View.GONE

        val userName = MyApplication.prefUtil.getString("username", "").toString()
        if(userName.isNotEmpty()){
            viewModel.myPageInfo(userName)
        }
    }

    private fun setUiEvent(){

        binding.ivBack.setOnClickListener {
            finish()
        }

        // 프로필 수정
        binding.cvProfileImg.setOnClickListener {
            // 기본 이미지로 변경 or 앨범에서 사진 선택
            registerForContextMenu(it)
            openContextMenu(it)
            unregisterForContextMenu(it)
        }

        // 닉네임 수정
        binding.edtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val name = binding.edtUsername.text.toString()
                binding.tvUsernameNum.text = "${name!!.length}/20"
            }
        })

        // 활동 지역
        binding.edtField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val field = binding.edtField.text.toString()
                binding.tvUsernameNum.text = "${field!!.length}/20"
            }
        })

        // 비밀번호 변경
        binding.tvChangePw.setOnClickListener {
            val dialog = ChangePwDialog(this)
            dialog.show()
            dialog.setOnDismissListener {
                if(dialog.resultCode == 1){ // 비밀 번호 변경
                    binding.edtChangedPw.setText(dialog.changedPw)
                    binding.tilChangedPw.visibility = View.VISIBLE
                }
            }
        }

        // 저장
        binding.btnSave.setOnClickListener {
            val dialog = SaveProfileDialog(this)
            dialog.show()
            dialog.setOnDismissListener {
                if(dialog.resultCode == 1){
                    currentPw = dialog.password
                    viewModel.checkPw(currentPw)
                }
            }
        }

        // 뒤로 가기
        binding.ivBack.setOnClickListener {
            showCancelDialog()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        showCancelDialog()

    }

    private fun showCancelDialog(){
        val dialog = SimpleDialog(this, "프로필 수정을 취소하시겠습니까?")
        dialog.show()
        dialog.setOnDismissListener {
            if(dialog.resultCode == 1){
                deleteProfileImgFile()
                finish()
            }
        }
    }
    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            showCustomToast(message)
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.logout.observe(this) { logout ->
            if(logout) logout()
        }

        // 회원 정보 가져오기
        viewModel.myPageData.observe(this) { data ->
            if (data == null) return@observe

            if (data.profileImage != null) {
                MainScope().async {
                    Glide.with(binding.ivProfileImg)
                        .load(data.profileImage)
                        .placeholder(R.color.main_black)
                        .error(R.drawable.ic_profile_photo_base)
                        .apply(RequestOptions.centerCropTransform())
                        .into(binding.ivProfileImg)

                    Glide.with(applicationContext)
                        .asBitmap()
                        .load(data.profileImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?){
                                val file = File(cacheDir, "profileImage.jpg")
                                changedImgPath = file.path
                                try {
                                    file.createNewFile()
                                    FileOutputStream(file).use { outputStream ->
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {
                                // Do nothing
                            }
                        })
                }
            } else {
                binding.ivProfileImg.setImageResource(R.drawable.ic_profile_photo_base)
            }

            binding.edtUsername.setText(data.username)
            binding.tvUsernameNum.text = "${data.username.length}/20"

            var field = data.field
            if (field == null) field = ""
            binding.edtField.setText(field)
            binding.tvFieldNum.text = "${field.length}/20"

        }
        // 비밀번호 검사 성공
        viewModel.getMemberId.observe(this) { id ->
            if(id==null) return@observe

            // 비밀 번호
            if(binding.tilChangedPw.visibility == View.VISIBLE){
                currentPw = binding.edtChangedPw.text.toString()
            }

            // 프로필 수정 요청
            viewModel.editProfile(
                username = binding.edtUsername.text.toString(),
                password = currentPw,
                field = binding.edtField.text.toString().ifEmpty { null },
                memberId = id,
                profileImgPath = changedImgPath
            )
        }

        // 프로필 수정 성공
        viewModel.editProfileSuccess.observe(this) { isSuccess ->
            viewModel._loading.postValue(false)

            if(!isSuccess)return@observe
            // 앱 내부 데이터 수정
            MyApplication.prefUtil.setString("username", binding.edtUsername.text.toString())
            //MyApplication.prefUtil.setString("field", binding.edtField.text.toString())



            // 프로필 수정 화면 종료
            showCustomToast(EDIT_COMPLETE)
            finish()

        }
    }

    override fun finish() {
        super.finish()
        deleteProfileImgFile()
    }


    companion object{
        const val TAG = "LogProfileActivity"
    }

    private fun saveProfileImg(bitmap: Bitmap): String {

        val tempFile = File(cacheDir, "profileImage.jpg")

        try {
            tempFile.createNewFile()

            val out = FileOutputStream(tempFile)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

            out.close()
        } catch (e: FileNotFoundException) {
            Log.e(DiaryActivity.TAG, "FileNotFoundException : " + e.message)
        } catch (e: IOException) {
            Log.e(DiaryActivity.TAG, "IOException : " + e.message)
        }
        return tempFile.absolutePath
    }

    private fun deleteProfileImgFile(){
        val file = File(cacheDir, "profileImage.jpg")
        if(file.exists()){
            file.delete()
        }
    }


    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }

    private fun getURIForResource(resId: Int): Uri {
        return Uri.parse("android.resource://$packageName/$resId")
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_edit_profile, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.menuBaseImg ->{ // 기본 이미지로 변경
                binding.ivProfileImg.setImageResource(R.drawable.ic_profile_photo_base)
                changedImgPath = null
            }
            R.id.menuSelectImg -> { // 앨범에서 사진 선택
                val intent = Intent(Intent.ACTION_PICK)
                intent.setDataAndType(MediaStore.AUTHORITY_URI, "image/*")
                mediaResult.launch(intent)
            }

        }
        return super.onContextItemSelected(item)
    }
}