package com.example.p3tchild.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.p3tchild.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.password_EditText

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = FirebaseDatabase.getInstance()
        dbReference = database.reference.child("Users")
        mAuth = FirebaseAuth.getInstance()
    }

    fun saveForm(view: View) {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = password_EditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        when {
            TextUtils.isEmpty(name) -> {
                nameEditText.error = "Digite seu nome"
                nameEditText.requestFocus()
            }
            TextUtils.isEmpty(name) || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                emailEditText.error = "Digite um email válido"
                emailEditText.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                password_EditText.error = "Digite sua senha"
                password_EditText.requestFocus()
            }
            password.length < 6 -> {
                password_EditText.error = "Sua senha precisa ter mais de 6 dígitos"
                password_EditText.requestFocus()
            }
            password != confirmPassword -> {
                confirmPasswordEditText.error = "As senhas digitadas não são iguais"
                confirmPasswordEditText.requestFocus()
            }
            else -> mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                val userId = mAuth.currentUser!!.uid
                                val currentUserDb = dbReference.child(userId)
                                currentUserDb.child("Name").setValue(name)
                                currentUserDb.child("Email").setValue(email)
                                val intent = Intent(this, PetFinderFormActivity::class.java)
                                startActivity(intent)
                            }else {
                                // If sign in fails, display a message to the user.
                                Log.i("ERROR", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Não foi possível cadastrar o usuário, tente mais tarde.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
        }
    }

    fun exitForm(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
