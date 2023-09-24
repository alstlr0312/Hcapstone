package com.You.haveto.features.login


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.You.haveto.MyApplication
import com.You.haveto.R
import com.You.haveto.databinding.ActivityLoginBinding
import com.You.haveto.features.BaseActivity
import com.You.haveto.features.login.find.FindIdActivity
import com.You.haveto.features.login.find.FindPwActivity
import com.You.haveto.features.login.onboarding.OnBoardingFragment
import com.You.haveto.features.signup.SignUpActivity
import com.You.haveto.network.util.ON_BOARDING

lateinit var onBoardingFragment: OnBoardingFragment
class LoginActivity : com.You.haveto.config.BaseActivity<ActivityLoginBinding>(
    ActivityLoginBinding::inflate) {
    private val viewModel by viewModels<LoginViewModel>()
    private var firstStart = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBoarding = MyApplication.prefUtil.getBoolean(ON_BOARDING, false)
        if(!onBoarding){
            onBoardingFragment = OnBoardingFragment()
            supportFragmentManager.beginTransaction().add(R.id.onBoardingLayout, onBoardingFragment).commitAllowingStateLoss()
        }
        setView()

        // viewModel의 Data를 Observe하는 이벤트 모음 함수
        subscribeUI()

        // UI Event를 정리한 함수
        setUiEvent()
    }

    private fun setView(){
        val id = MyApplication.prefUtil.getString("id", null)
        if(id!=null){
            binding.edtId.setText(id)
        }
    }
    private fun setUiEvent() {
        binding.btnLogin.setOnClickListener {
            val id = binding.edtId.text.toString()
            val password = binding.edtPassword.text.toString()
            viewModel.login(id, password)
        }

        // 회원가입
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // 아이디 찾기
        binding.tvFindId.setOnClickListener {
            val intent = Intent(this, FindIdActivity::class.java)
            startActivity(intent)
        }
        // 비밀번호 찾기
        binding.tvFindPassword.setOnClickListener {
            val intent = Intent(this, FindPwActivity::class.java)
            intent.putExtra("mode", "find")
            startActivity(intent)
        }
    }

    private fun subscribeUI() {
        viewModel.toastMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        viewModel.loading.observe(this) { isLoading ->
            if (isLoading) showLoadingDialog(this) else dismissLoadingDialog()
        }

        viewModel.loginSuccess.observe(this) { isSuccess ->
            if (!isSuccess) return@observe

            startActivity(Intent(this, BaseActivity::class.java))
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        if(!firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }
        firstStart = false
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }
}