package com.mycf.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 👑 完美绑定全新重构的 1:1 Plant Profile 界面布局
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}