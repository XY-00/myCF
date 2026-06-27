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

class EcoImpactFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = requireContext()
        val root = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val titleView = TextView(context).apply {
            text = "Eco Impact"
            textSize = 22f
            setTextColor(android.graphics.Color.WHITE) // 👑 核心修复
            setTypeface(null, android.graphics.Typeface.BOLD) // 👑 核心修复
            gravity = Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#4A7A68"))
            setPadding(0, 24, 0, 24)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 0, 0, 24) }
        }
        root.addView(titleView)

        val farmerCard = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#FDFBF0"))
            radius = 16f
            cardElevation = 4f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 0, 0, 24) }
            val content = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 24, 32, 24)
                val name = TextView(context).apply { text = "Farmer: Lee Xin Yi"; textSize = 18f; setTextColor(android.graphics.Color.BLACK); setTypeface(null, android.graphics.Typeface.BOLD) }
                val uid = TextView(context).apply { text = "UserID: FARM0027"; textSize = 14f; setTextColor(android.graphics.Color.GRAY); setPadding(0, 4, 0, 16) }
                val grade = TextView(context).apply { text = "Eco Friendly Grade: A (Top 5%)"; textSize = 16f; setTextColor(android.graphics.Color.parseColor("#2E7D32")); setTypeface(null, android.graphics.Typeface.BOLD) }
                addView(name); addView(uid); addView(grade)
            }
            addView(content)
        }
        root.addView(farmerCard)

        val historyHeader = TextView(context).apply {
            text = "History Records"
            textSize = 16f
            setTextColor(android.graphics.Color.BLACK) // 👑 核心修复
            setTypeface(null, android.graphics.Typeface.BOLD) // 👑 核心修复
            setPadding(0, 0, 0, 12)
        }
        root.addView(historyHeader)

        val recordsCard = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"))
            radius = 12f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val listLayout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)

                val r1 = TextView(context).apply { text = "📅 2026/03  -  Saved: 146.0 mg CO₂e  [Grade A]"; textSize = 14f; setPadding(0, 8, 0, 8) }
                val r2 = TextView(context).apply { text = "📅 2026/02  -  Saved: 80.0 mg CO₂e   [Grade B]"; textSize = 14f; setPadding(0, 8, 0, 8) }
                val r3 = TextView(context).apply { text = "📅 2026/01  -  Saved: 66.0 mg CO₂e   [Grade B]"; textSize = 14f; setPadding(0, 8, 0, 8) }

                addView(r1); addView(r2); addView(r3)
            }
            addView(listLayout)
        }
        root.addView(recordsCard)

        return root
    }
}