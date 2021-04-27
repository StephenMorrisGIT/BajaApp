package com.example.project2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var signUp: Button
    private lateinit var login2: Button
    private lateinit var switch: Switch
    private lateinit var language: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPrefs: SharedPreferences = getSharedPreferences("Project2", Context.MODE_PRIVATE)
        val savedCheck = sharedPrefs.getBoolean("SAVED_PREFERENCE", false)

        var languageCheck: Boolean = false

        firebaseAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.signInEmail)
        password = findViewById(R.id.signInPassword)
        login = findViewById(R.id.logInButton)
        login2 = findViewById(R.id.logInButton2)
        signUp = findViewById(R.id.signUp)
        progressBar = findViewById(R.id.loginProgressBar)
        switch = findViewById(R.id.rememberSwitch)
        language = findViewById(R.id.spanishCheckbox)

        login.isEnabled = false
        switch.isEnabled = false
        login2.isEnabled = true

        language.setOnCheckedChangeListener { CheckBox, isChecked ->
            languageCheck = true

            email.setHint("correo electrónico")
            password.setHint("contraseña")
            switch.setText("¿Recuerda las credenciales?")
            login.setText("iniciar sesión")
            signUp.setText("¿Nueva usuario? Regístrate")
        }

        if(savedCheck){
            switch.isChecked = true

            val savedEmail = sharedPrefs.getString("SAVED_EMAIL", "")
            val savedPassword = sharedPrefs.getString("SAVED_PASSWORD", "")
            email.setText(savedEmail)
            password.setText(savedPassword)

            login.isEnabled = true
            switch.isEnabled = true
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val inputtedEmail: String = email.text.toString()
                val inputtedPassword: String = password.text.toString()
                val enableButton = inputtedEmail.isNotEmpty() && inputtedPassword.isNotEmpty()

                login.isEnabled = enableButton
                switch.isEnabled = enableButton
            }
        }

        email.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)

        login.setOnClickListener {
            val inputtedEmail = email.text.toString().trim()
            val inputtedPassword = password.text.toString().trim()
            progressBar.visibility = View.VISIBLE

            firebaseAuth.signInWithEmailAndPassword(inputtedEmail, inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Logged in as: ${user!!.email}", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE

                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("language", languageCheck)
                        startActivity(intent)
                    }
                    else {
                        val exception = task.exception
                        Toast.makeText(this, "Failed: $exception", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.INVISIBLE
                    }
                }
        }

        signUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            intent.putExtra("language", languageCheck)
            startActivity(intent)
        }

        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPrefs.edit().putBoolean("SAVED_PREFERENCE", isChecked).apply()
            sharedPrefs.edit().putString("SAVED_EMAIL", email.text.toString()).apply()
            sharedPrefs.edit().putString("SAVED_PASSWORD", password.text.toString()).apply()
        }

        login2.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("language", languageCheck)
            startActivity(intent)
        }
    }
}