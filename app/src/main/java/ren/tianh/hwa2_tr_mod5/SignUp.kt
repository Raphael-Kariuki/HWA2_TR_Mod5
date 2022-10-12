package ren.tianh.hwa2_tr_mod5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.lang.Exception
import java.util.*

class SignUp : AppCompatActivity() {

    //setup dbHelper variable
    lateinit var userDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //initialize the dbHelper class
        userDBHelper = DBHelper(this)

        //full name
        val userFullNameEntry = findViewById<TextInputEditText>(R.id.userFullNameEntry)
        val userFullNameEntryLayout = findViewById<TextInputLayout>(R.id.userFullNameLayout)

        //user name
        val userNameEntry = findViewById<TextInputEditText>(R.id.userNameEntry)
        val userNameLayout = findViewById<TextInputLayout>(R.id.userNameLayout)

        //password
        val userPasswordEntry = findViewById<TextInputEditText>(R.id.userPasswordEntry)
        val userPasswordLayout = findViewById<TextInputLayout>(R.id.userPasswordLayout)

        //confirm password
        val userConfirmPasswordEntry = findViewById<TextInputEditText>(R.id.userConfirmPasswordEntry)
        val userConfirmPasswordLayout = findViewById<TextInputLayout>(R.id.userConfirmPasswordLayout)

        //signup button
        val btnSignUp = findViewById<MaterialButton>(R.id.btnSignUp)

        //forgot password
        val forgotPasswordTextView = findViewById<MaterialTextView>(R.id.forgotPasswordTextView)

        //already have an account. SignIn
        val alreadyHaveAnAccountSignInTextView = findViewById<MaterialTextView>(R.id.alreadyHaveAnAccount_SignInTextView)

        btnSignUp.setOnClickListener{

            //generate a random UUID
            var userId = UUID.randomUUID().toString()

            //obtain text from input
            var fullName = userFullNameEntry.text.toString()
            var userName = userNameEntry.text.toString()
            var password = userPasswordEntry.text.toString()
            var confirmPassword = userConfirmPasswordEntry.text.toString()

            var results:Boolean = false

            //check that passwords match
            if (password == confirmPassword){
                results = addUser(userId,fullName,userName,password,userDBHelper)
            }else{
                //if passwords don't match throw error
                userConfirmPasswordLayout.isErrorEnabled = true
                userConfirmPasswordLayout.error = "Password doesn't match"
            }

            if (results){
                //show toast to inform user of succes
                Toast.makeText(applicationContext,"User $userName added successfully", Toast.LENGTH_SHORT).show()
                //clear the user input
                userFullNameEntry.setText("")
                userNameEntry.setText("")
                userPasswordEntry.setText("")
                userConfirmPasswordEntry.setText("")

                //redirect via an intent
                startActivity(Intent(applicationContext, Login::class.java))
            }
            else{
                //inform user of signup error
                Toast.makeText(applicationContext,"User $userName addition error", Toast.LENGTH_LONG).show()
            }
        }

        //if one already has an account, click to redirect to user login page
        alreadyHaveAnAccountSignInTextView.setOnClickListener{
            startActivity(Intent(applicationContext,Login::class.java))
        }

    }

    //function to add user separate from the buttonClick Listener
    private fun addUser(userId:String,fullName:String,userName:String,password:String,dbHelper: DBHelper): Boolean{

        //initialize variable for later use
        var results: Boolean = false
        try {
                results = dbHelper.insertUser(UserModel(userId,fullName,userName,password))
            }catch (e: Exception){
                e.printStackTrace()
            }
        //return true or false based on success or failure
        return results

    }
}