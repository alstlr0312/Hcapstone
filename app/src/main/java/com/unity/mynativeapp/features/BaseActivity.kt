package com.unity.mynativeapp.features


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.unity.mynativeapp.features.ar.ArFragment
import com.unity.mynativeapp.features.community.CommunityFragment
import com.unity.mynativeapp.features.home.HomeFragment
import com.unity.mynativeapp.features.mypage.MyPageFragment
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityBaseBinding
import com.unity.mynativeapp.features.postwrite.PostWriteActivity

lateinit var homeFragment: HomeFragment
lateinit var communityFragment: CommunityFragment
lateinit var arFragment: ArFragment
lateinit var mypageFragment: MyPageFragment

class BaseActivity : AppCompatActivity() {
    val binding by lazy {ActivityBaseBinding.inflate(layoutInflater)}
    var firstStart = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commitAllowingStateLoss()
        binding.btmNavView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menuHome -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss()
                    binding.btnWritePost.visibility = View.INVISIBLE
                    true
                }
                R.id.menuCommunity -> {
                    communityFragment = CommunityFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, communityFragment).commitAllowingStateLoss()
                    binding.btnWritePost.visibility = View.VISIBLE
                    true
                }
                R.id.menuAr -> {
                    arFragment = ArFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, arFragment).commitAllowingStateLoss()
                    binding.btnWritePost.visibility = View.INVISIBLE
                    true
                }
                R.id.menuMypage -> {
                    mypageFragment = MyPageFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, mypageFragment).commitAllowingStateLoss()
                    binding.btnWritePost.visibility = View.INVISIBLE
                    true
                }
                else -> false
            }
        }

        binding.btnWritePost.setOnClickListener {
            startActivity(Intent(this, PostWriteActivity::class.java))
        }


    }

    override fun onResume() {
        super.onResume()
        if(!firstStart){
            overridePendingTransition(R.drawable.anim_slide_in_left, R.drawable.anim_slide_out_right)
        }
        firstStart = false
    }
}