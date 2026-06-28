package com.mycf.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class EcoImpactFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 👑 完美关联新写的 1:1 账单样式布局文件
        return inflater.inflate(R.layout.fragment_eco_impact, container, false)
    }
}