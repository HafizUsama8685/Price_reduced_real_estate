package com.hutechnologies.pricereducedrealestate.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.models.User
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.notContains
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var sharedPrefs : SharedPreferences
    private val userRef = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val toolbar = supportActionBar
        toolbar?.title = "Sign In"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()
        sharedPrefs = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        btnSingIn.setOnClickListener {
            if(checkValidation()){
                signInProgressbar.visibility = View.VISIBLE
                signInUser(edtSignInEmail.text.toString(), edtSignInPassword.text.toString())
            }
        }
    }

    private  fun checkValidation(): Boolean{
        if(edtSignInEmail.nonEmpty()){
            if(edtSignInEmail.notContains(" ")){
                if(edtSignInEmail.validEmail()){
                    if(edtSignInPassword.nonEmpty()){
                        if(edtSignInPassword.notContains(" ")){
                            return true
                        }else{
                            edtSignInPassword.error = "Spaces are not allowed!"
                            edtSignInPassword.requestFocus()
                        }
                    }else{
                        edtSignInPassword.error = "Password is required!"
                        edtSignInPassword.requestFocus()
                    }
                }else{
                    edtSignInEmail.error = "Please enter valid email!"
                    edtSignInEmail.requestFocus()
                }
            }else{
                edtSignInEmail.error = "Spaces are not allowed!"
                edtSignInEmail.requestFocus()
            }
        }else{
            edtSignInEmail.error = "Email is required!"
            edtSignInEmail.requestFocus()
        }
        return false
    }


    private fun signInUser(email:String, password: String){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                userRef.addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            for(userList in snapshot.children){
                                var user = userList.getValue(User::class.java)
                                if(email == user?.email){
                                    sharedPrefs.edit().putString("email", email).apply()
                                    sharedPrefs.edit().putString("fName", user?.fname).apply()
                                    sharedPrefs.edit().putString("lName", user?.lname).apply()
                                    sharedPrefs.edit().putString("phone", user?.phoneNumber).apply()
                                    signInProgressbar.visibility = View.GONE
                                    Toast.makeText(baseContext, "Login Successful!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(baseContext, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    this@SignInActivity.finish()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
//                signInProgressbar.visibility = View.GONE
//                sharedPrefs.edit().putString("email", email).apply()
//                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//                startActivity(intent)
//                this.finish()
            }else{
                signInProgressbar.visibility = View.GONE
                Toast.makeText(this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}