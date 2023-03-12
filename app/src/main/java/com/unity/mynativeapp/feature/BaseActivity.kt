package com.unity.mynativeapp.feature


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityBaseBinding
import com.unity.mynativeapp.feature.community.CommunityFragment
import com.unity.mynativeapp.feature.home.HomeFragment
import com.unity.mynativeapp.feature.mypage.MyPageFragment

lateinit var homeFragment: HomeFragment
lateinit var communityFragment: CommunityFragment
lateinit var arFragment: com.unity.mynativeapp.feature.ar.ArFragment
lateinit var mypageFragment: MyPageFragment

class BaseActivity : AppCompatActivity() {
    lateinit var binding: ActivityBaseBinding
    var firstStart = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commitAllowingStateLoss()
        binding.btmNavView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menuHome -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragment).commitAllowingStateLoss()
                    true
                }
                R.id.menuCommunity -> {
                    communityFragment = CommunityFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, communityFragment).commitAllowingStateLoss()
                    true
                }
                R.id.menuAr -> {
                    arFragment = com.unity.mynativeapp.feature.ar.ArFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, arFragment).commitAllowingStateLoss()
                    true
                }
                R.id.menuMypage -> {
                    mypageFragment = MyPageFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, mypageFragment).commitAllowingStateLoss()
                    true
                }
                else -> false
            }
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