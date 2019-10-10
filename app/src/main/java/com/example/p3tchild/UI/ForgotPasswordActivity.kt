package com.example.p3tchild.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.p3tchild.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        mAuth = FirebaseAuth.getInstance()
    }

    fun sendEmail(view: View) {
        val email = email_EditText.text.toString()

        if(!TextUtils.isEmpty(email)){
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        val message = "E-mail enviado"
                        Log.d("SUCCESS",message)
                        updateUI()
                    }else {
                        Log.w("ERROR", it.exception!!.message!!)
                        Toast.makeText(this, "Nenhum usuário encontrado com este email.", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            email_EditText.error = "Digite seu email"
            email_EditText.requestFocus()
        }
    }

    private fun updateUI() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}
