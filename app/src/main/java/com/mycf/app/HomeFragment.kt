package com.mycf.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 👑 一键渲染上方的完美精美设计图布局，彻底摆脱凌乱的后台硬编码代码
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}