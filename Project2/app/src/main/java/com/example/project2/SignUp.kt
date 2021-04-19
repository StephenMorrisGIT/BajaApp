package com.example.project2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.view.isGone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import androidx.core.view.isVisible as isVisible

class SignUp : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var createAccount: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val intent = intent
        val language = intent.getBooleanExtra("language", false)

        firebaseAuth = FirebaseAuth.getInstance()

        email = findViewById(R.id.signUpEmail)
        password = findViewById(R.id.signUpPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        createAccount = findViewById(R.id.createAccount)
        progressBar = findViewById(R.id.signUpProgressBar)
        textView= findViewById(R.id.signUpTextView)



        createAccount.isEnabled = false

        if(language){
            textView.text = "crear una nueva cuenta"
            email.hint = "correo electrónico"
            password.hint = "contraseña"
            confirmPassword.hint = "confirmar contraseña"
            createAccount.hint = "crear una cuenta"
        }

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val inputtedEmail: String = email.text.toString()
                val inputtedPassword: String = password.text.toString()
                val inputtedPassword2: String = confirmPassword.text.toString()
                var enableButton = inputtedEmail.isNotEmpty() && inputtedPassword.isNotEmpty() && inputtedPassword2.isNotEmpty()

                if (inputtedPassword != inputtedPassword2){
                    enableButton = false
                }

                createAccount.isEnabled = enableButton
            }
        }
        email.addTextChangedListener(textWatcher)
        password.addTextChangedListener(textWatcher)
        confirmPassword.addTextChangedListener(textWatcher)


        createAccount.setOnClickListener{
            val inputtedEmail: String = email.text.toString().trim()
            val inputtedPassword: String = password.text.toString().trim()
            progressBar.visibility = View.VISIBLE

            firebaseAuth
                .createUserWithEmailAndPassword(inputtedEmail,inputtedPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this, "Created user: ${user!!.email}", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        progressBar.visibility = View.INVISIBLE
                    }
                    else {
                        val exception = task.exception

                        if (exception is FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                        else if(exception is FirebaseAuthUserCollisionException){
                            Toast.makeText(this, "There is already an account associated with this email", Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                        else{
                            Toast.makeText(this, "Failed to sign up: $exception", Toast.LENGTH_LONG).show()
                            progressBar.visibility = View.INVISIBLE
                        }
                    }
                }
        }
    }
}