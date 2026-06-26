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

    // 👑 引入真正的 Firebase 认证神经元
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        // 初始化 Firebase Auth
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

        // ======= 椭圆药丸互切状态机逻辑 =======
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

        // 默认开屏状态配置
        tabLogin.post {
            tabLogin.setBackgroundResource(R.drawable.auth_tab_selector)
            tabLogin.setTextColor(android.graphics.Color.parseColor("#111111"))
            tabSignup.setBackgroundResource(android.R.color.transparent)
            tabSignup.setTextColor(android.graphics.Color.parseColor("#556B2F"))
        }

        // ======= 📡 动态实时智能审查监听中枢 =======
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


        // ======= 🚀 1. 与 Firebase 真实连线：精准拆分定位错误登录 =======
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

            // 调用 Firebase 真实数据校验库
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Welcome to myCF!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // 👑 核心：捕获 Firebase 扔出来的异常，并智能翻译转换输出
                        when (task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                // Firebase 判定账号不存在
                                layoutLoginEmail.isErrorEnabled = true
                                layoutLoginEmail.error = "Email not found"
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                // Firebase 判定密码不匹配
                                layoutLoginPassword.isErrorEnabled = true
                                layoutLoginPassword.error = "Your password is invalid"
                            }
                            else -> {
                                Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
        }


        // ======= 🚀 2. 与 Firebase 真实连线：一键往云端数据库注册新用户 =======
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

            // 发射到 Firebase Auth 云端建立数据
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Sign Up Successful! Please log in.", Toast.LENGTH_LONG).show()
                        // 注册成功自动归位到登录页面
                        tabLogin.performClick()
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }


        // ======= 🚀 3. 与 Firebase 真实连线：一键拉起 Google 原生系统弹窗并进行 Firebase 绑定账户 =======
        btnGoogleAuth.setOnClickListener {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("729227563124-mockid.apps.googleusercontent.com") // 💡 提示：项目上线时可以在 Firebase Console 的 Google Provider 下复制 Web Client ID 粘贴到这里
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

                        // 👑 把系统返回的 Google ID 令牌传入 Firebase 凭据中转站进行云端一键登录绑定！
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(this@AuthActivity) { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    Toast.makeText(this@AuthActivity, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@AuthActivity, "Firebase link failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
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