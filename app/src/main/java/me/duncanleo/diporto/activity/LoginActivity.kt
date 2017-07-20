package me.duncanleo.diporto.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import me.duncanleo.diporto.R

/**
 * Created by duncanleo on 20/7/17.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            if (usernameTextInputLayout.editText?.text.isNullOrEmpty()) {
                usernameTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            if (passwordTextInputLayout.editText?.text.isNullOrEmpty()) {
                passwordTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
        }
    }
}
