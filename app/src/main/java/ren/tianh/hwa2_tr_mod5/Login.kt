package ren.tianh.hwa2_tr_mod5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import java.lang.Exception

class Login : AppCompatActivity() {
    lateinit var userDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        userDBHelper = DBHelper(this)

        val loginUserNameLayout = findViewById<TextInputLayout>(R.id.loginUserNameLayout)
        val loginPasswordLayout = findViewById<TextInputLayout>(R.id.loginPasswordLayout)

        val loginUserNameEntry = findViewById<TextInputEditText>(R.id.loginUserNameEntry)
        val loginPasswordEntry = findViewById<TextInputEditText>(R.id.loginPasswordEntry)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)

        val homeUserNameView = findViewById<MaterialTextView>(R.id.homeUserNameView)

        btnLogin.setOnClickListener {
            val loginUserName = loginUserNameEntry.text.toString()
            val loginUserPassword = loginPasswordEntry.text.toString()

            var user: ArrayList<UserModel> = ArrayList<UserModel>()
            try {
                 user = userDBHelper.readUserDetails(loginUserName)
                if(user.isNullOrEmpty()){
                    //redirect user to the login without showing errors, this complicates the password stuffing process(automated attacks)
                    startActivity(Intent(applicationContext, SignUp::class.java))
                }else{
                    //if username exists then check that password matches that in the db
                    user.forEach {
                        //if password matches that in the db then redirect to the home page
                        if (loginUserPassword == it.password.toString()){

                            //create intent to sent use details to the home page
                            val redirect2Home = Intent(applicationContext, Home::class.java)
                            redirect2Home.putExtra("UserName", it.username.toString())
                            redirect2Home.putExtra("FullName", it.fullUserNames.toString())
                            redirect2Home.putExtra("Password", it.password.toString())
                            redirect2Home.putExtra("UserId", it.userId.toString())
                            startActivity(redirect2Home)
                        }else{
                            //if password doesn't match that in the db inform user with an error
                            loginPasswordLayout.isErrorEnabled = true
                            loginPasswordLayout.error = "Wrong password"
                        }

                    }
                }
            }catch (e: Exception){
                e.printStackTrace()

            }


        }


        val notHaveAnAccountSignInTextView = findViewById<MaterialTextView>(R.id.notHaveAnAccount_SignInTextView)

        notHaveAnAccountSignInTextView.setOnClickListener{
            startActivity(Intent(applicationContext,SignUp::class.java))
        }
    }
}