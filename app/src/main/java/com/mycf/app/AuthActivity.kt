package com.mycf.app

import android.content.Intent
import android.os.Bundle
import android.view.View
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

        // 1. 找到药丸标签跟两个大表单卡片容器
        val tabLogin = findViewById<TextView>(R.id.tab_login)
        val tabSignup = findViewById<TextView>(R.id.tab_signup)
        val containerLogin = findViewById<LinearLayout>(R.id.container_login)
        val containerSignup = findViewById<LinearLayout>(R.id.container_signup)

        // 2. 找到表单内需要的输入控件和按钮
        val loginEmail = findViewById<TextInputEditText>(R.id.login_email)
        val loginPassword = findViewById<TextInputEditText>(R.id.login_password)
        val btnLogin = findViewById<MaterialButton>(R.id.btn_login)
        val regFirstName = findViewById<TextInputEditText>(R.id.reg_first_name)
        val btnRegister = findViewById<MaterialButton>(R.id.btn_register)

        // ======= 👑 药丸双标签点击动态切表面逻辑 =======
        tabLogin.setOnClickListener {
            // 选回 Log In 状态
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#2E7D32"))

            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#757575"))

            containerLogin.visibility = View.VISIBLE
            containerSignup.visibility = View.GONE
        }

        tabSignup.setOnClickListener {
            // 选到 Sign Up 状态
            tabSignup.setBackgroundResource(R.drawable.auth_tab_selector)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#2E7D32"))

            tabLogin.setBackgroundResource(android.R.color.transparent)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#757575"))

            containerLogin.visibility = View.GONE
            containerSignup.visibility = View.VISIBLE
        }

        // ======= 3. 处理登录点击跳转大厅 =======
        btnLogin.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val password = loginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // ======= 4. 处理注册点击响应 =======
        btnRegister.setOnClickListener {
            val name = regFirstName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your first name", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registration successful! Please Log In.", Toast.LENGTH_LONG).show()
                // 模拟点击自动切回第一页让用户去登录
                tabLogin.performClick()
            }
        }
    }
}