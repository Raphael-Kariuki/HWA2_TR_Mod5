package ren.tianh.hwa2_tr_mod5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textview.MaterialTextView
import java.util.*

class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //obtain the view where to render the user details
        val homeFullUserNameView = findViewById<MaterialTextView>(R.id.homeFullUserNameView)
        val homeUserNameView = findViewById<MaterialTextView>(R.id.homeUserNameView)
        val homeUserPasswordView = findViewById<MaterialTextView>(R.id.homeUserPasswordView)
        val homeUserIdView = findViewById<MaterialTextView>(R.id.homeUserIdView)


        //obtain intent values sent from login
        val bundle: Bundle? = intent.extras
        val fullUserName:String = bundle?.get("UserName").toString()
        val userName:String = bundle?.get("FullName").toString()
        val password:String = bundle?.get("Password").toString()
        val userId: String = bundle?.get("UserId").toString()


        //set text on views
        homeFullUserNameView.text = String.format(Locale.US,"%s : %s","User full names ",fullUserName)
        homeUserNameView.text = String.format(Locale.US,"%s : %s","User name  ",userName)
        homeUserPasswordView.text = String.format(Locale.US,"%s : %s","User password ",password)
        homeUserIdView.text = String.format(Locale.US,"%s : %s","UserId ",userId)


    }
}