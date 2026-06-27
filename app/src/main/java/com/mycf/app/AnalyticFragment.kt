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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AnalyticFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = requireContext()
        val root = LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val titleView = TextView(context).apply {
            text = "Analysis Report"
            textSize = 22f
            setTextColor(android.graphics.Color.WHITE) // 👑 核心修复
            setTypeface(null, android.graphics.Typeface.BOLD) // 👑 核心修复
            gravity = Gravity.CENTER
            setBackgroundColor(android.graphics.Color.parseColor("#4A7A68"))
            setPadding(0, 24, 0, 24)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { setMargins(0, 0, 0, 24) }
        }
        root.addView(titleView)

        val chartCard = CardView(context).apply {
            radius = 16f
            cardElevation = 4f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400).apply { setMargins(0, 0, 0, 24) }

            val chart = LineChart(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                description.text = "24-Hour Moisture Trend Analysis"
                setPadding(16, 16, 16, 16)
            }

            val entries = ArrayList<Entry>()
            entries.add(Entry(9f, 68f))
            entries.add(Entry(13f, 66f))
            entries.add(Entry(17f, 64f))
            entries.add(Entry(19f, 61f))
            entries.add(Entry(20f, 75f))
            entries.add(Entry(23f, 73f))
            entries.add(Entry(24f, 70f))

            val dataSet = LineDataSet(entries, "Soil Moisture %").apply {
                color = android.graphics.Color.parseColor("#4CAF50")
                setCircleColor(android.graphics.Color.parseColor("#4CAF50"))
                lineWidth = 2.5f
                circleRadius = 4f
                setDrawCircleHole(false)
                valueTextSize = 9f
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
            addView(chart)
        }
        root.addView(chartCard)

        val protectionCard = CardView(context).apply {
            setCardBackgroundColor(android.graphics.Color.parseColor("#E8F5E9"))
            radius = 20f
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val content = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(24, 24, 24, 24)
                val h = TextView(context).apply { text = "Carbon Protection"; textSize = 16f; setTextColor(android.graphics.Color.parseColor("#2E7D32")); setTypeface(null, android.graphics.Typeface.BOLD) }
                val details = TextView(context).apply { text = "Successful Interventions: 3\nProtection Rate: 100 %"; textSize = 15f; setTextColor(android.graphics.Color.BLACK) }
                addView(h); addView(details)
            }
            addView(content)
        }
        root.addView(protectionCard)

        return root
    }
}