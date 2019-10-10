package com.example.p3tchild.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import com.example.p3tchild.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.progress_bar
import kotlinx.android.synthetic.main.activity_pet_finder_form.*


class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_login)

        progress_bar.visibility = View.INVISIBLE

        mAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        fbLogin()
    }

    fun login(view: View) {
        progress_bar.visibility = View.VISIBLE
        val email = login_EditText.text.toString()
        val password = password_EditText.text.toString()

        when {
            TextUtils.isEmpty(email) -> {
                login_EditText.error = "Digite seu email"
                login_EditText.requestFocus()
            }
            TextUtils.isEmpty(password) -> {
                password_EditText.error = "Digite sua senha"
                password_EditText.requestFocus()
            }
            else -> mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful)
                        updateUI(mAuth.currentUser)
                    else
                        Toast.makeText(baseContext, "Verifique se o email e senha estão corretos", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun register(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    fun passwordForgot(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun fbLogin() {
        fb_login_button.setPermissions("email", "public_profile")
        fb_login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("SUCCESS", "facebook:onSuccess:$loginResult")
                Toast.makeText(baseContext, "facebook:onSuccess:$loginResult", Toast.LENGTH_LONG).show()
                handleFacebookAccessToken(loginResult!!.accessToken)
            }
            override fun onCancel() {
                Log.d("CANCEL", "facebook:onCancel")
                Toast.makeText(baseContext, "facebook:onCancel", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: FacebookException) {
                Log.d("ERROR", "facebook:onError", error)
                Toast.makeText(baseContext, "facebook:onError$error", Toast.LENGTH_LONG).show()
            }
        })


    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SUCCESS", "signInWithCredential:success")
                    updateUI(mAuth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FAILURE", "signInWithCredential:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, PetFinderFormActivity::class.java)
            progress_bar.visibility = View.INVISIBLE
            startActivity(intent)
        }
    }
}
