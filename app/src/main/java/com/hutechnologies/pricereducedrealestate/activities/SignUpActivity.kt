package com.hutechnologies.pricereducedrealestate.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hutechnologies.pricereducedrealestate.R
import com.hutechnologies.pricereducedrealestate.models.User
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.notContains
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    private  lateinit var auth : FirebaseAuth
    private val userRef = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val toolbar = supportActionBar
        toolbar?.title = "SignUp"
        toolbar?.setDisplayHomeAsUpEnabled(true)
        auth = FirebaseAuth.getInstance()


        btnSignUp.setOnClickListener{
            if(checkValidation()){
                signUpProgressBar.visibility = View.VISIBLE
                val user = User("", edtSignUpFName.text.toString(), edtSignUpLName.text.toString(), edtSignUpPhoneNumber.text.toString(), edtSignUpEmail.text.toString(), edtSignUpPassword.text.toString())
                signUpUser(user)
            }
        }
        tvSignIn.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            this.finish()
        }
    }

    private fun checkValidation(): Boolean{
        if(edtSignUpFName.nonEmpty()){
            if(edtSignUpLName.nonEmpty()){
                if(edtSignUpPhoneNumber.nonEmpty()){
                    if(edtSignUpEmail.nonEmpty()){
                        if(edtSignUpEmail.notContains(" ")){
                            if(edtSignUpEmail.validEmail()){
                                if(edtSignUpPassword.nonEmpty()){
                                    if(edtSignUpPassword.notContains(" ")){
                                        if(edtSignUpPassword.text!!.length >= 6){
                                            if(edtSignUpConfirmPassword.nonEmpty()){
                                                if(edtSignUpConfirmPassword.notContains(" ")){
                                                    if(edtSignUpPassword.text.toString() == edtSignUpConfirmPassword.text.toString()){
                                                        return true
                                                    }else{
                                                        edtSignUpConfirmPassword.error = "Password and confirm password must be same!"
                                                        edtSignUpConfirmPassword.requestFocus()
                                                    }
                                                }else{
                                                    edtSignUpConfirmPassword.error = "Spaces are not allowed!"
                                                    edtSignUpConfirmPassword.requestFocus()
                                                }
                                            }else{
                                                edtSignUpConfirmPassword.error = "Please confirm your password!"
                                                edtSignUpConfirmPassword.requestFocus()
                                            }
                                        }else{
                                            edtSignUpPassword.error= "Password must contains at least 6 characters!"
                                        }
                                    }else{
                                        edtSignUpPassword.error = "Spaces are not allowed!"
                                        edtSignUpPassword.requestFocus()
                                    }
                                }else{
                                    edtSignUpPassword.error = "Password is required!"
                                    edtSignUpPassword.requestFocus()
                                }
                            }else{
                                edtSignUpEmail.error = "Please enter valid email!"
                                edtSignUpEmail.requestFocus()
                            }
                        }else{
                            edtSignUpEmail.error = "Spaces are not allowed!"
                            edtSignUpEmail.requestFocus()
                        }
                    }else{
                        edtSignUpEmail.error = "Email is required!"
                        edtSignUpEmail.requestFocus()
                    }
                }else{
                    edtSignUpPhoneNumber.error = "Phone number is required!"
                    edtSignUpPhoneNumber.requestFocus()
                }
            }else{
                edtSignUpLName.error = "Last name is required!"
                edtSignUpLName.requestFocus()
            }
        }else{
            edtSignUpFName.error = "First name is required!"
            edtSignUpFName.requestFocus()
        }
        return false
    }

    private fun signUpUser(user: User){
        auth.createUserWithEmailAndPassword(user.email!!, user.password!!).addOnCompleteListener {
            if (it.isSuccessful){
                saveUserData(user)
            } else {
                signUpProgressBar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(user: User){
//        user.id = userRef.push().key!!
        user.id = FirebaseAuth.getInstance().currentUser!!.uid
        userRef.child(user.id!!).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                signUpProgressBar.visibility = View.GONE
                Toast.makeText(this, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                val intent  = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                this.finish()
            }else{
                signUpProgressBar.visibility = View.GONE
                Toast.makeText(this, "Something went wrong please try again later!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return super.onSupportNavigateUp()
    }
}