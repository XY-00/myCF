package com.mycf.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // 1. 绑定药丸标签切换控件与双表单面板容器
        val tabLogin = findViewById<TextView>(R.id.tab_login)
        val tabSignup = findViewById<TextView>(R.id.tab_signup)
        val containerLogin = findViewById<LinearLayout>(R.id.container_login)
        val containerSignup = findViewById<LinearLayout>(R.id.container_signup)

        // 2. 绑定输入输入框
        val loginEmailInput = findViewById<TextInputEditText>(R.id.login_email_input)
        val loginPasswordInput = findViewById<TextInputEditText>(R.id.login_password_input)

        // 3. 绑定动作执行按钮与 Google 快捷认证 icon
        val btnActionLogin = findViewById<MaterialButton>(R.id.btn_action_login)
        val btnActionRegister = findViewById<MaterialButton>(R.id.btn_action_register)
        val btnGoogleAuth = findViewById<ImageButton>(R.id.btn_google_auth)

        // ======= 👑 药丸双标签点击来回完美切换面板逻辑 =======
        tabLogin.setOnClickListener {
            // 切回 Log In 表单状态
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#2E7D32"))

            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#757575"))

            containerLogin.visibility = View.VISIBLE
            containerSignup.visibility = View.GONE
        }

        tabSignup.setOnClickListener {
            // 切到 Sign Up 表单状态
            tabSignup.setBackgroundResource(R.drawable.auth_tab_selector)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#2E7D32"))

            tabLogin.setBackgroundResource(android.R.color.transparent)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#757575"))

            containerLogin.visibility = View.GONE
            containerSignup.visibility = View.VISIBLE
        }

        // ======= 🚀 处理 1:1 点击登录逻辑并跳转进入主系统 =======
        btnActionLogin.setOnClickListener {
            val email = loginEmailInput.text.toString().trim()
            val password = loginPasswordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Welcome to myCF!", Toast.LENGTH_SHORT).show()

                // 🎯 核心通电：直接调用 Intent 跨入搭载 5 个图标底栏的 MainActivity！
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // 销毁登录注册卡片，防止用户按返回键回流
            }
        }

        // ======= 🚀 处理注册按钮模拟成功逻辑 =======
        btnActionRegister.setOnClickListener {
            Toast.makeText(this, "Sign Up Successful! Switching to Login.", Toast.LENGTH_LONG).show()
            tabLogin.performClick() // 自动模拟点击切回登录界面让用户输入刚才注册的账号
        }

        // ======= 👑 处理 Google 图标点击快捷登录验证 =======
        btnGoogleAuth.setOnClickListener {
            Toast.makeText(this, "Connecting with Google Account...", Toast.LENGTH_SHORT).show()
            // 自动模拟登录成功，直接进入主厅
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}