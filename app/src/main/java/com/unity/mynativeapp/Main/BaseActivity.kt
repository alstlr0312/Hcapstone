package com.unity.mynativeapp.Main


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unity.mynativeapp.Main.ar.ArFragment
import com.unity.mynativeapp.Main.community.CommunityFragment
import com.unity.mynativeapp.Main.home.HomeFragment
import com.unity.mynativeapp.Main.mypage.MypageFragment
import com.unity.mynativeapp.R
import com.unity.mynativeapp.databinding.ActivityBaseBinding

lateinit var homeFragment: HomeFragment
lateinit var communityFragment: CommunityFragment
lateinit var arFragment: ArFragment
lateinit var mypageFragment: MypageFragment

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
                    arFragment = ArFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout, arFragment).commitAllowingStateLoss()
                    true
                }
                R.id.menuMypage -> {
                    mypageFragment = MypageFragment()
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