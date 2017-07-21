package me.duncanleo.diporto.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*

import me.duncanleo.diporto.R
import me.duncanleo.diporto.network.Network

class RegisterActivity : AppCompatActivity() {
    val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
            if (usernameTextInputLayout.editText?.text.isNullOrEmpty()) {
                usernameTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            nameTextInputLayout.error = null
            if (nameTextInputLayout.editText?.text.isNullOrEmpty()) {
                nameTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            nameTextInputLayout.error = null
            if (emailTextInputLayout.editText?.text.isNullOrEmpty()) {
                emailTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            emailTextInputLayout.error = null
            if (passwordTextInputLayout.editText?.text.isNullOrEmpty()) {
                passwordTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            passwordTextInputLayout.error = null
            if (confirmPasswordTextInputLayout.editText?.text.isNullOrEmpty()) {
                confirmPasswordTextInputLayout.error = getString(R.string.label_cannot_empty)
                return@setOnClickListener
            }
            confirmPasswordTextInputLayout.error = null

            Network.getDiportoService().register(
                    name = nameTextInputLayout.editText!!.text.toString(),
                    username = usernameTextInputLayout.editText!!.text.toString(),
                    password = passwordTextInputLayout.editText!!.text.toString(),
                    email = emailTextInputLayout.editText!!.text.toString(),
                    confirmPassword = confirmPasswordTextInputLayout!!.editText!!.text.toString()
            ).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ _ ->
                        MaterialDialog.Builder(this@RegisterActivity)
                                .title(R.string.label_registered)
                                .positiveText(R.string.label_ok)
                                .onPositive { _, _ ->
                                    finish()
                                }
                                .show()
                    }, { error ->
                        Log.d(TAG, "Error registering", error)
                        MaterialDialog.Builder(this@RegisterActivity)
                                .title(R.string.label_error)
                                .content(R.string.description_error_register)
                                .show()
                    })
        }
    }
}
