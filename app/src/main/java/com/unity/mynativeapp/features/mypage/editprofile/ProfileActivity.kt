package com.unity.mynativeapp.features.mypage.editprofile

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.graphics.decodeBitmap
import com.unity.mynativeapp.MyApplication
import com.unity.mynativeapp.R
import com.unity.mynativeapp.config.BaseActivity
import com.unity.mynativeapp.databinding.ActivityProfileBinding
import com.unity.mynativeapp.features.diary.DiaryActivity
import com.unity.mynativeapp.model.MediaRvItem
import com.unity.mynativeapp.network.util.FIELD_EMPTY_ERROR
import com.unity.mynativeapp.network.util.NICKNAME_EMPTY_ERROR
import com.unity.mynativeapp.network.util.NICKNAME_PATTERN_ERROR
import com.unity.mynativeapp.network.util.PreferenceUtil
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private var firstStart = true
    private val viewModel by viewModels<ProfileViewModel>()
    private var currentPw = ""
    private var changeToBaseImg: Boolean = false
    private var changedImgUri: Uri?= null // null: 이미지 변경 x, not null: 기본 or 다른 이미지로 변경
    var mediaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == RESULT_OK){
            val imageUri = result.data?.data
            imageUri?.let{
                changedImgUri = it
                binding.ivProfileImg.setImageURI(it)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
        setUiEvent()
        subscribeUI()
    }

    private fun setView(){

        // 프로필 이미지
        val path = MyApplication.prefUtil.getString("profileImgPath", "")
        if(path != ""){
            val bitmap = BitmapFactory.decodeFile(path);
            binding.ivProfileImg.setImageBitmap(bitmap)
            Log.d("ProfileActivity", "base image setting")
        }else{
            binding.ivProfileImg.setImageResource(R.drawable.ic_profile_photo_base)
        }

        // 닉네임
        val username = intent.getStringExtra("username")
        binding.edtUsername.setText(username)
        binding.tvUsernameNum.text = "${username!!.length}/20"

        // 활동 지역
        val field = intent.getStringExtra("field")
        binding.edtField.setText(field)
        binding.tvFieldNum.text = "${field!!.length}/20"

        binding.tilChangedPw.visibility = View.GONE
    }

    private fun setUiEvent(){

        binding.ivBack.setOnClickListener {
            finish()
        }

        // 프로필 수정
        binding.cvProfileImg.setOnClickListener {
            // 기본 이미지로 변경 or 앨범에서 사진 선택
            val intent = Intent(Intent.ACTION_PICK)
            intent.setDataAndType(MediaStore.AUTHORITY_URI, "image/*")
            mediaResult.launch(intent)
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

        binding.btnSave.setOnClickListener {
            val dialog = SaveProfileDialog(this)
            dialog.show()
            dialog.setOnDismissListener {
                if(dialog.resultCode == 1){
                    currentPw = dialog.password
                }
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



        // 비밀번호 검사 성공
        viewModel.getMemberId.observe(this) { id ->
            if(id==null) return@observe

            // 비밀 번호
            if(binding.tilChangedPw.visibility == View.VISIBLE){
                currentPw = binding.edtChangedPw.text.toString()
            }

            // 프로필 이미지
            if(changeToBaseImg){
                changedImgUri = getURLForResource(R.drawable.ic_profile_photo_base)
            }

            viewModel.editProfile(
                username = binding.edtUsername.text.toString(),
                password = currentPw,
                field = binding.edtField.text.toString().ifEmpty { null },
                memberId = id,
                profileImgUri = changedImgUri
            )
        }

        viewModel.editProfileSuccess.observe(this) { isSuccess ->
            if(!isSuccess)return@observe
            // 앱 내부 데이터 수정
            MyApplication.prefUtil.setString("username", binding.edtUsername.text.toString())
            MyApplication.prefUtil.setString("field", binding.edtField.text.toString())

            if(changedImgUri != null){
                val source = ImageDecoder.createSource(application.contentResolver, changedImgUri!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                saveProfileImg(bitmap)
            }
        }
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


    override fun onResume() {
        super.onResume()
        if(firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_right, R.drawable.anim_slide_out_left)
            firstStart = false
        }
    }

    private fun getURLForResource(resId: Int): Uri {
        return Uri.parse("android.resource://$packageName/$resId")
    }
}