package me.duncanleo.diporto.activity

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import me.duncanleo.diporto.R
import me.duncanleo.diporto.network.Network
import me.duncanleo.diporto.network.payload.RequestTokenPayload
import me.duncanleo.diporto.prefs

/**
 * Created by duncanleo on 20/7/17.
 */
class LoginActivity : AppCompatActivity() {
    val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            if (usernameTextInputLayout.editText?.text.isNullOrEmpty()) {
                usernameTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            usernameTextInputLayout.error = null
            if (passwordTextInputLayout.editText?.text.isNullOrEmpty()) {
                passwordTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            passwordTextInputLayout.error = null

            usernameTextInputLayout.error = null
            passwordTextInputLayout.error = null

            val username = usernameTextInputLayout.editText!!.text.toString()

            MaterialDialog.Builder(this@LoginActivity)
                    .title(R.string.label_logging_in)
                    .content(R.string.label_please_wait)
                    .progress(true, 0)
                    .show()
                    .setOnShowListener { dialog ->
                        Network.getDiportoService().requestToken(RequestTokenPayload(
                                username = username,
                                password = passwordTextInputLayout.editText!!.text.toString(),
                                grantType = "access_token"
                        )).subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({ (token, refreshToken) ->
                                    Log.d(TAG, "Logged in")
                                    prefs.refreshToken = refreshToken
                                    prefs.accessToken = token

                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    dialog.cancel()
                                    finish()
                                }, { error ->
                                    error.printStackTrace()
                                    dialog.cancel()
                                    MaterialDialog.Builder(this@LoginActivity)
                                            .title(R.string.label_error)
                                            .content(R.string.description_error_login)
                                            .show()
                                })
                    }
        }
    }
}
