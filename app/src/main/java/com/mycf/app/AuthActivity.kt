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
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()
        val credentialManager = CredentialManager.create(this)

        val tabLogin = findViewById<TextView>(R.id.tab_login)
        val tabSignup = findViewById<TextView>(R.id.tab_signup)
        val containerLogin = findViewById<LinearLayout>(R.id.container_login)
        val containerSignup = findViewById<LinearLayout>(R.id.container_signup)

        val layoutLoginEmail = findViewById<TextInputLayout>(R.id.layout_login_email)
        val loginEmailInput = findViewById<TextInputEditText>(R.id.login_email_input)
        val layoutLoginPassword = findViewById<TextInputLayout>(R.id.layout_login_password)
        val loginPasswordInput = findViewById<TextInputEditText>(R.id.login_password_input)
        val cbRememberMe = findViewById<CheckBox>(R.id.cb_remember_me)
        val btnActionLogin = findViewById<MaterialButton>(R.id.btn_action_login)

        // 👑 完美对接：指定类型为官方的原生组件 SignInButton
        val btnGoogleAuth = findViewById<com.google.android.gms.common.SignInButton>(R.id.btn_google_auth)

        val layoutRegEmail = findViewById<TextInputLayout>(R.id.layout_reg_email)
        val layoutRegPassword = findViewById<TextInputLayout>(R.id.layout_reg_password)
        val layoutRegConfirmPassword = findViewById<TextInputLayout>(R.id.layout_reg_confirm_password)

        val regFirstNameInput = findViewById<TextInputEditText>(R.id.reg_first_name_input)
        val regLastNameInput = findViewById<TextInputEditText>(R.id.reg_last_name_input)
        val regEmailInput = findViewById<TextInputEditText>(R.id.reg_email_input)
        val regPasswordInput = findViewById<TextInputEditText>(R.id.reg_password_input)
        val regConfirmPasswordInput = findViewById<TextInputEditText>(R.id.reg_confirm_password_input)
        val btnActionRegister = findViewById<MaterialButton>(R.id.btn_action_register)

        // ======= 药丸切换选择核心逻辑 =======
        tabLogin.setOnClickListener {
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#111111"))
            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#556B2F"))
            containerLogin.visibility = View.VISIBLE
            containerSignup.visibility = View.GONE
        }

        tabSignup.setOnClickListener {
            tabSignup.setBackgroundResource(R.drawable.auth_tab_selector)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#111111"))
            tabLogin.setBackgroundResource(android.R.color.transparent)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#556B2F"))
            containerLogin.visibility = View.GONE
            containerSignup.visibility = View.VISIBLE
        }

        // 初始化第一次载入药丸高亮
        tabLogin.post {
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#111111"))
            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#556B2F"))
        }

        // ======= 📡 弹性智能空间释放监听中枢 =======

        loginEmailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s.toString().trim()
                if (text.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    layoutLoginEmail.isErrorEnabled = true
                    layoutLoginEmail.error = "Invalid email format"
                } else {
                    layoutLoginEmail.error = null
                    layoutLoginEmail.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        loginPasswordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (layoutLoginPassword.error != null) {
                    layoutLoginPassword.error = null
                    layoutLoginPassword.isErrorEnabled = false
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
                    layoutRegEmail.error = "Invalid email format"
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
                    layoutRegPassword.error = "Must be at least 6 characters"
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
                    layoutRegConfirmPassword.error = "Passwords do not match"
                } else {
                    layoutRegConfirmPassword.error = null
                    layoutRegConfirmPassword.isErrorEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // ======= 🚀 Firebase 连线：拆分独立报错登录 =======
        btnActionLogin.setOnClickListener {
            val email = loginEmailInput.text.toString().trim()
            val password = loginPasswordInput.text.toString().trim()

            if (email.isEmpty()) {
                layoutLoginEmail.isErrorEnabled = true
                layoutLoginEmail.error = "Email not found"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                layoutLoginPassword.isErrorEnabled = true
                layoutLoginPassword.error = "Your password is invalid"
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Welcome to myCF!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        when (task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                layoutLoginEmail.isErrorEnabled = true
                                layoutLoginEmail.error = "Email not found"
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                layoutLoginPassword.isErrorEnabled = true
                                layoutLoginPassword.error = "Your password is invalid"
                            }
                            else -> {
                                Toast.makeText(this, "Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }

        // ======= 🚀 Firebase 连线：新用户一键建立云端数据 =======
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
            if (layoutRegEmail.error != null || layoutRegPassword.error != null || layoutRegConfirmPassword.error != null) {
                Toast.makeText(this, "Please fix formatting errors", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign Up Successful! Please log in.", Toast.LENGTH_LONG).show()
                        tabLogin.performClick()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }

        // ======= 🚀 Firebase 连线：拉起一键 Google 登录系统级弹窗 =======
        btnGoogleAuth.setOnClickListener {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("620786807271-8pl28rr3thob673koj2lja58jl9tnfb1.apps.googleusercontent.com")
                .setAutoSelectEnabled(false)
                .build()

            val credentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            lifecycleScope.launch {
                try {
                    val result = credentialManager.getCredential(
                        context = this@AuthActivity,
                        request = credentialRequest
                    )

                    val credential = result.credential
                    if (credential is GoogleIdTokenCredential) {
                        val googleIdTokenCredential = credential
                        val idToken = googleIdTokenCredential.idToken

                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this@AuthActivity) { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    Toast.makeText(this@AuthActivity, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@AuthActivity, "Firebase login link failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@AuthActivity, "Google sign-in cancelled", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }
}