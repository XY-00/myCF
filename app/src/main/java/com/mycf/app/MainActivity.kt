package com.mycf.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.mycf_bottom_navigation)

        // 👑 默认初始化第一站：立刻渲染加载 Dashboard 仪表盘（HomeFragment）
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        // 📡 实时接收底栏 5 大精美卡片点击，点哪个图标秒切换哪个 Fragment 布局
        bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()          // 图三 Dashboard
                R.id.nav_analytic -> AnalyticFragment()  // 图二 Analytic
                R.id.nav_eco -> EcoImpactFragment()      // 图五 Carbon Report
                R.id.nav_profile -> ProfileFragment()    // 图六 Plant Profile
                R.id.nav_setting -> SettingFragment()    // 图四 Setting
                else -> HomeFragment()
            }
            loadFragment(selectedFragment)
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_fragment_container, fragment)
            .commit()
        return true
    }
}