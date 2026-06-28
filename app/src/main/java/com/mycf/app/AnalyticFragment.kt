package com.mycf.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AnalyticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 👑 无缝挂载全新 1:1 精修重构的 Analysis Report 看板大底图
        return inflater.inflate(R.layout.fragment_analytic, container, false)
    }
}