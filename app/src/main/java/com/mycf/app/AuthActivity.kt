package com.mycf.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
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

        // 1. 绑定药丸切换状态标签
        val tabLogin = findViewById<TextView>(R.id.tab_login)
        val tabSignup = findViewById<TextView>(R.id.tab_signup)
        val containerLogin = findViewById<LinearLayout>(R.id.container_login)
        val containerSignup = findViewById<LinearLayout>(R.id.container_signup)

        // 2. 绑定登录区输入框与动作按钮
        val loginEmailInput = findViewById<TextInputEditText>(R.id.login_email_input)
        val loginPasswordInput = findViewById<TextInputEditText>(R.id.login_password_input)
        val cbRememberMe = findViewById<CheckBox>(R.id.cb_remember_me)
        val btnActionLogin = findViewById<MaterialButton>(R.id.btn_action_login)
        val btnGoogleAuth = findViewById<MaterialButton>(R.id.btn_google_auth)

        // 3. 绑定注册区组件（全面清除了旧的 Phone 节点组件）
        val regFirstNameInput = findViewById<TextInputEditText>(R.id.reg_first_name_input)
        val regLastNameInput = findViewById<TextInputEditText>(R.id.reg_last_name_input)
        val regEmailInput = findViewById<TextInputEditText>(R.id.reg_email_input)
        val regPasswordInput = findViewById<TextInputEditText>(R.id.reg_password_input)
        val regConfirmPasswordInput = findViewById<TextInputEditText>(R.id.reg_confirm_password_input) // 👑 绑定新确认密码框
        val btnActionRegister = findViewById<MaterialButton>(R.id.btn_action_register)

        // ======= 药丸卡片左右互切逻辑 =======
        tabLogin.setOnClickListener {
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#757575"))
            containerLogin.visibility = View.VISIBLE
            containerSignup.visibility = View.GONE
        }

        tabSignup.setOnClickListener {
            tabSignup.setBackgroundResource(R.drawable.auth_tab_selector)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
            tabLogin.setBackgroundResource(android.R.color.transparent)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#757575"))
            containerLogin.visibility = View.GONE
            containerSignup.visibility = View.VISIBLE
        }

        // ======= 🚀 4. 处理具备防乱填校验的邮箱登录 =======
        btnActionLogin.setOnClickListener {
            val email = loginEmailInput.text.toString().trim()
            val password = loginPasswordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in email and password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmailInput.error = "Invalid Email Address Format!"
                Toast.makeText(this, "Please check your email formatting!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (cbRememberMe.isChecked) {
                Toast.makeText(this, "Remember me enabled", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(this, "Welcome to myCF!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // ======= 🚀 5. 核心看齐：注册表单全能校验（加持确认密码一致性安全锁） =======
        btnActionRegister.setOnClickListener {
            val firstName = regFirstNameInput.text.toString().trim()
            val lastName = regLastNameInput.text.toString().trim()
            val email = regEmailInput.text.toString().trim()
            val password = regPasswordInput.text.toString().trim()
            val confirmPassword = regConfirmPasswordInput.text.toString().trim()

            // A. 空白拦截
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Error: All spaces are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // B. 邮箱地址防乱填拦截
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                regEmailInput.error = "Email address formatting error!"
                Toast.makeText(this, "Format Error: Please input a real email format!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // C. 密码长度拦截（至少6位）
            if (password.length < 6) {
                regPasswordInput.error = "Password must be at least 6 characters long!"
                Toast.makeText(this, "Failed: Password is too short (At least 6 chars)!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // D. 👑 核心安全锁：强力判断确认密码和原始密码是否长得一模一样
            if (password != confirmPassword) {
                regConfirmPasswordInput.error = "Passwords do not match!"
                Toast.makeText(this, "Registration Error: Two passwords are not the same!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // E. 全部校验完美通关
            Toast.makeText(this, "Registration successful! Welcome to myCF.", Toast.LENGTH_LONG).show()
            tabLogin.performClick()
        }

        // ======= Google 快捷大按钮验证事件 =======
        btnGoogleAuth.setOnClickListener {
            Toast.makeText(this, "Google Sign In Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}