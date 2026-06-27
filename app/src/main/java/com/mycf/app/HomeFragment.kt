package com.mycf.app

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = requireContext()

        val root = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            gravity = Gravity.CENTER_HORIZONTAL // 👑 核心修复：更正对齐常量
        }

        val titleView = TextView(context).apply {
            text = "Dashboard"
            textSize = 22f
            setTextColor(android.graphics.Color.WHITE) // 👑 核心修复：更正颜色设置
            setTypeface(null, android.graphics.Typeface.BOLD) // 👑 核心修复：更正粗体设置
            gravity = Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#4A7A68"))
            setPadding(0, 24, 0, 24)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 32)
            }
        }
        root.addView(titleView)

        val subTitle = TextView(context).apply {
            text = "Monitoring the Carbon Footprint\nfor Soil and Plant"
            textSize = 16f
            setTextColor(android.graphics.Color.BLACK)
            setTypeface(null, android.graphics.Typeface.BOLD)
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 24)
            }
        }
        root.addView(subTitle)

        // 1. 碳排减免卡片
        val co2Card = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#F1F8E9"))
            radius = 24f
            cardElevation = 4f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 24)
            }
            val content = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 32, 32, 32)
                val t1 = TextView(context).apply { text = "Total Carbon Footprint Saved"; textSize = 16f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD) }
                val t2 = TextView(context).apply { text = "146.0 mg CO₂ e"; textSize = 26f; setTextColor(android.graphics.Color.parseColor("#2E7D32")); setTypeface(null, android.graphics.Typeface.BOLD); setPadding(0, 16, 0, 0) }
                addView(t1); addView(t2)
            }
            addView(content)
        }
        root.addView(co2Card)

        // 2. 植物水分百分比卡片
        val hydrationCard = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#E8F5E9"))
            radius = 24f
            cardElevation = 4f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 24)
            }
            val content = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                setPadding(32, 32, 32, 32)
                val t1 = TextView(context).apply { text = "62.9 %"; textSize = 24f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD) }
                val t2 = TextView(context).apply { text = "Plant Hydration (%)"; textSize = 15f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD); setPadding(0, 16, 0, 0) }
                addView(t1); addView(t2)
            }
            addView(content)
        }
        root.addView(hydrationCard)

        // 3. 碳碳稳定得分卡片
        val stabilityCard = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#FFEBEE"))
            radius = 24f
            cardElevation = 4f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                setMargins(0, 0, 0, 24)
            }
            val content = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                setPadding(32, 32, 32, 32)
                val t1 = TextView(context).apply { text = "90 / 100"; textSize = 24f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD) }
                val t2 = TextView(context).apply { text = "Carbon Stability Score"; textSize = 15f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD); setPadding(0, 16, 0, 0) }
                addView(t1); addView(t2)
            }
            addView(content)
        }
        root.addView(stabilityCard)

        // 4. 当前运行策略条
        val policyCard = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"))
            radius = 16f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val content = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 24, 24, 24)
                val t1 = TextView(context).apply { text = "Active Policy\nStatus: Carbon Guarding"; textSize = 14f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD) }
                val t2 = TextView(context).apply { text = "● GREEN"; textSize = 14f; setTextColor(android.graphics.Color.parseColor("#4CAF50")); setTypeface(null, android.graphics.Typeface.BOLD); setPadding(0, 8, 0, 0) }
                addView(t1); addView(t2)
            }
            addView(content)
        }
        root.addView(policyCard)

        return root
    }
}