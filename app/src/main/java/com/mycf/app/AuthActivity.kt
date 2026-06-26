package com.mycf.app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val tabLogin = findViewById<TextView>(R.id.tab_login)
        val tabSignup = findViewById<TextView>(R.id.tab_signup)
        val containerLogin = findViewById<LinearLayout>(R.id.container_login)
        val containerSignup = findViewById<LinearLayout>(R.id.container_signup)

        val layoutLoginEmail = findViewById<TextInputLayout>(R.id.layout_login_email)
        val loginEmailInput = findViewById<TextInputEditText>(R.id.login_email_input)
        val loginPasswordInput = findViewById<TextInputEditText>(R.id.login_password_input)
        val cbRememberMe = findViewById<CheckBox>(R.id.cb_remember_me)
        val btnActionLogin = findViewById<MaterialButton>(R.id.btn_action_login)
        val btnGoogleAuth = findViewById<MaterialButton>(R.id.btn_google_auth)

        val layoutRegEmail = findViewById<TextInputLayout>(R.id.layout_reg_email)
        val layoutRegPassword = findViewById<TextInputLayout>(R.id.layout_reg_password)
        val layoutRegConfirmPassword = findViewById<TextInputLayout>(R.id.layout_reg_confirm_password)

        val regFirstNameInput = findViewById<TextInputEditText>(R.id.reg_first_name_input)
        val regLastNameInput = findViewById<TextInputEditText>(R.id.reg_last_name_input)
        val regEmailInput = findViewById<TextInputEditText>(R.id.reg_email_input)
        val regPasswordInput = findViewById<TextInputEditText>(R.id.reg_password_input)
        val regConfirmPasswordInput = findViewById<TextInputEditText>(R.id.reg_confirm_password_input)
        val btnActionRegister = findViewById<MaterialButton>(R.id.btn_action_register)

        // ======= 👑 椭圆药丸：点击时完美套用原本的圆角椭圆 selector，只改变文字颜色深度 =======
        tabLogin.setOnClickListener {
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#2C3E50"))
            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#6E7A91"))
            containerLogin.visibility = View.VISIBLE
            containerSignup.visibility = View.GONE
        }

        tabSignup.setOnClickListener {
            tabSignup.setBackgroundResource(R.drawable.auth_tab_selector)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#2C3E50"))
            tabLogin.setBackgroundResource(android.R.color.transparent)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#6E7A91"))
            containerLogin.visibility = View.GONE
            containerSignup.visibility = View.VISIBLE
        }

        // ======= 📡 智能动态监听中枢 =======

        loginEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                if (text.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    layoutLoginEmail.isErrorEnabled = true
                    layoutLoginEmail.error = "🚨 Invalid email format"
                } else {
                    layoutLoginEmail.error = null
                    layoutLoginEmail.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        regEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                if (text.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    layoutRegEmail.isErrorEnabled = true
                    layoutRegEmail.error = "🚨 Invalid email format"
                } else {
                    layoutRegEmail.error = null
                    layoutRegEmail.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        regPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pass = s.toString()
                if (pass.isEmpty()) {
                    layoutRegPassword.error = null
                    layoutRegPassword.isErrorEnabled = false
                    return
                }
                if (pass.length < 6) {
                    layoutRegPassword.isErrorEnabled = true
                    layoutRegPassword.error = "🚨 Must be at least 6 characters"
                } else {
                    layoutRegPassword.error = null
                    layoutRegPassword.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        regConfirmPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val confirm = s.toString()
                val original = regPasswordInput.text.toString()
                if (confirm.isNotEmpty() && confirm != original) {
                    layoutRegConfirmPassword.isErrorEnabled = true
                    layoutRegConfirmPassword.error = "🚨 Passwords do not match"
                } else {
                    layoutRegConfirmPassword.error = null
                    layoutRegConfirmPassword.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnActionLogin.setOnClickListener {
            val email = loginEmailInput.text.toString().trim()
            val password = loginPasswordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || layoutLoginEmail.getError() != null) {
                Toast.makeText(this, "Please check your email or password", Toast.LENGTH_SHORT).show()
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

        btnActionRegister.setOnClickListener {
            val firstName = regFirstNameInput.text.toString().trim()
            val lastName = regLastNameInput.text.toString().trim()
            val email = regEmailInput.text.toString().trim()
            val password = regPasswordInput.text.toString()
            val confirmPassword = regConfirmPasswordInput.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (layoutRegEmail.getError() != null || layoutRegPassword.getError() != null || layoutRegConfirmPassword.getError() != null) {
                Toast.makeText(this, "Please fix formatting errors", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Sign Up Successful! Please log in.", Toast.LENGTH_LONG).show()
            tabLogin.performClick()
        }

        btnGoogleAuth.setOnClickListener {
            Toast.makeText(this, "Google Sign In Successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}