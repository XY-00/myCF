package com.mycf.app

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        auth = FirebaseAuth.getInstance()

        // 1. 注入选项（Exposed Dropdown 完美支持标准数组适配器）
        val durationOptions = arrayOf("5 sec", "10 sec (Recommand)", "15 sec")
        val intervalOptions = arrayOf("10 min", "20 min (Recommand)", "30 min")
        val freqOptions = arrayOf("1 Hours", "2 Hours", "3 Hours")
        val qualityOptions = arrayOf("Low", "Medium", "High")

        val autoDuration = view.findViewById<AutoCompleteTextView>(R.id.auto_duration)
        val autoInterval = view.findViewById<AutoCompleteTextView>(R.id.auto_interval)
        val autoFreq = view.findViewById<AutoCompleteTextView>(R.id.auto_freq)
        val autoQuality = view.findViewById<AutoCompleteTextView>(R.id.auto_quality)

        activity?.let { ctx ->
            val adapterDuration = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, durationOptions)
            val adapterInterval = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, intervalOptions)
            val adapterFreq = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, freqOptions)
            val adapterQuality = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, qualityOptions)

            autoDuration.setAdapter(adapterDuration)
            autoInterval.setAdapter(adapterInterval)
            autoFreq.setAdapter(adapterFreq)
            autoQuality.setAdapter(adapterQuality)
        }

        // ====================================================================
        // 👑 2. 核心修复：滑动条通过 0-1000 步长转换，完美划出 69.5%、78.3% 这样精准的小数位
        // ====================================================================
        val seekbarCarbon = view.findViewById<SeekBar>(R.id.seekbar_carbon)
        val tvSeekbarValue = view.findViewById<TextView>(R.id.tv_seekbar_value)

        seekbarCarbon.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val finalValue = progress.toFloat() / 10.0f
                tvSeekbarValue.text = String.format("%.1f %%", finalValue)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 3. 顶层三大分栏模式按键切换
        val btnFull = view.findViewById<TextView>(R.id.btn_mode_full)
        val btnSemi = view.findViewById<TextView>(R.id.btn_mode_semi)
        val btnPower = view.findViewById<TextView>(R.id.btn_mode_power)

        fun updateModeButtons(selected: TextView) {
            btnFull.setBackgroundColor(Color.parseColor("#F4F3E3"))
            btnSemi.setBackgroundColor(Color.parseColor("#F4F3E3"))
            btnPower.setBackgroundColor(Color.parseColor("#F4F3E3"))
            selected.setBackgroundColor(Color.parseColor("#C2D3B4")) // 选中的草绿色
        }

        updateModeButtons(btnFull) // 默认 Full Auto 高亮

        btnFull.setOnClickListener { updateModeButtons(btnFull); Toast.makeText(activity, "Switched to Full Auto Mode", Toast.LENGTH_SHORT).show() }
        btnSemi.setOnClickListener { updateModeButtons(btnSemi); Toast.makeText(activity, "Switched to Semi Auto Mode", Toast.LENGTH_SHORT).show() }
        btnPower.setOnClickListener { updateModeButtons(btnPower); Toast.makeText(activity, "Switched to Power Save Mode", Toast.LENGTH_SHORT).show() }

        // 4. 手动覆盖拦截逻辑
        val switchManual = view.findViewById<Switch>(R.id.switch_manual)
        val btnPump = view.findViewById<View>(R.id.btn_pump)

        switchManual?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(activity, "Manual Mode Enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Manual Mode Disabled", Toast.LENGTH_SHORT).show()
            }
        }

        btnPump.setOnClickListener {
            if (switchManual?.isChecked == true) {
                Toast.makeText(activity, "Command Sent: Water Pump Activated!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Access Denied: Please turn on Enable Manual Mode first!", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. LOG OUT 安全返回
        val btnLogoutCard = view.findViewById<View>(R.id.btn_logout_card)
        btnLogoutCard.setOnClickListener {
            auth.signOut()
            Toast.makeText(activity, "Successfully Logged Out", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}