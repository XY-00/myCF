package com.mycf.app

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var currentMoisture: Int = 0
    private var currentThreshold: Int = 40
    private var isAutoMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化 UI 组件
        val tvSoilMoisture = findViewById<TextView>(R.id.tvSoilMoisture)
        val tvCurrentMode = findViewById<TextView>(R.id.tvCurrentMode)
        val tvPumpStatus = findViewById<TextView>(R.id.tvPumpStatus) // 新增
        val rgMode = findViewById<RadioGroup>(R.id.rgMode)
        val rbManual = findViewById<RadioButton>(R.id.rbManual)
        val rbAuto = findViewById<RadioButton>(R.id.rbAuto)
        val btnPumpOn = findViewById<Button>(R.id.btnPumpOn)
        val btnPumpOff = findViewById<Button>(R.id.btnPumpOff)
        val etThreshold = findViewById<EditText>(R.id.etThreshold)
        val btnSaveThreshold = findViewById<Button>(R.id.btnSaveThreshold)

        // 连接新加坡 Firebase 数据库
        val database = FirebaseDatabase.getInstance("https://mycf-9adeb-default-rtdb.asia-southeast1.firebasedatabase.app")
        val moistureRef = database.getReference("soil_moisture")
        val pumpRef = database.getReference("pump_status")
        val modeRef = database.getReference("irrigation_mode")
        val thresholdRef = database.getReference("moisture_threshold")

        // 1. 监听本地模式切换并上传
        rgMode.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbAuto) {
                modeRef.setValue("Auto")
            } else {
                modeRef.setValue("Manual")
            }
        }

        // 2. 监听云端模式同步
        modeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val mode = snapshot.getValue(String::class.java) ?: "Manual"
                tvCurrentMode.text = "Current Mode: $mode"

                if (mode == "Auto") {
                    isAutoMode = true
                    rbAuto.isChecked = true
                    btnPumpOn.isEnabled = false
                    btnPumpOff.isEnabled = false
                    checkAutoIrrigation(pumpRef)
                } else {
                    isAutoMode = false
                    rbManual.isChecked = true
                    btnPumpOn.isEnabled = true
                    btnPumpOff.isEnabled = true
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // 3. 监听湿度变化（处理自动逻辑）
        moistureRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentMoisture = snapshot.getValue(Int::class.java) ?: 0
                tvSoilMoisture.text = "Soil Moisture: $currentMoisture %"

                if (isAutoMode) {
                    checkAutoIrrigation(pumpRef)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // 4. 监听自动阈值
        thresholdRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentThreshold = snapshot.getValue(Int::class.java) ?: 40
                etThreshold.setText(currentThreshold.toString())

                if (isAutoMode) {
                    checkAutoIrrigation(pumpRef)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // 🎯 5. 新增：实时监听水泵开关状态，让 UI 跟着变色
        pumpRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue(String::class.java) ?: "OFF"
                if (status == "ON") {
                    tvPumpStatus.text = "Pump Status: RUNNING 💧"
                    tvPumpStatus.setTextColor(Color.GREEN) // 运行中显示绿色
                } else {
                    tvPumpStatus.text = "Pump Status: STOPPED 🛑"
                    tvPumpStatus.setTextColor(Color.RED)   // 停止显示红色
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })

        // 6. 点击保存新阈值
        btnSaveThreshold.setOnClickListener {
            val input = etThreshold.text.toString()
            if (input.isNotEmpty()) {
                val newThreshold = input.toInt()
                thresholdRef.setValue(newThreshold)
                    .addOnSuccessListener {
                        Toast.makeText(this@MainActivity, "Threshold updated!", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // 7. 手动泵开关控制
        btnPumpOn.setOnClickListener { pumpRef.setValue("ON") }
        btnPumpOff.setOnClickListener { pumpRef.setValue("OFF") }
    }

    // 自动化控制算法核心
    private fun checkAutoIrrigation(pumpRef: com.google.firebase.database.DatabaseReference) {
        if (currentMoisture < currentThreshold) {
            pumpRef.setValue("ON")
        } else {
            pumpRef.setValue("OFF")
        }
    }
}