package it.aliut.ilserpente.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import it.aliut.ilserpente.R
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsFragment : Fragment(), View.OnClickListener {

    private val RC_SIGN_IN = 42

    private val viewModel: SettingsViewModel by viewModel()

    private var loggedWithGoogle: Boolean = false

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestProfile()
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.settings_fragment, container, false)

        layout.button_google_signin.setOnClickListener(this)
        layout.button_google_logout.setOnClickListener(this)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            inputedit_user_name.setText(user.name)
            Glide.with(this)
                .load(user.photoUrl ?: R.drawable.ic_launcher_foreground)
                .into(image_user_picture)

            if (loggedWithGoogle) {
                button_google_signin.visibility = View.INVISIBLE
                button_google_logout.visibility = View.VISIBLE
            } else {
                button_google_signin.visibility = View.VISIBLE
                button_google_logout.visibility = View.INVISIBLE
            }
        })
    }

    override fun onStart() {
        super.onStart()

        GoogleSignIn.getLastSignedInAccount(context!!)
            ?.let {
                updateUser(it)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            onLoginCompleted(task)
        }
    }

    private fun performGoogleSignIn() {
        val client = GoogleSignIn.getClient(context!!, gso)

        startActivityForResult(client.signInIntent, RC_SIGN_IN)
    }

    private fun performGoogleLogout() {
        val client = GoogleSignIn.getClient(context!!, gso)

        client.signOut().addOnCompleteListener {
            showMessage("User signed out!")
            clearUser()
        }
    }

    private fun updateUser(account: GoogleSignInAccount) {
        loggedWithGoogle = true
        viewModel.updateUser(account)
    }

    private fun clearUser() {
        loggedWithGoogle = false
        viewModel.updateUser(null)
    }

    private fun onLoginCompleted(task: Task<GoogleSignInAccount>) {
        try {
            task.getResult(ApiException::class.java)
                ?.also { updateUser(it) }
                ?.also { showMessage(getString(R.string.google_login_success, it.displayName)) }
                ?: showMessage(getString(R.string.google_login_error))
        } catch (ex: ApiException) {
            showMessage(getString(R.string.google_login_error))
            Timber.d("Google SignIn failed: ${ex.statusCode}")
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            button_google_signin,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onClick(view: View) {
        when (view.id) {
            button_google_signin.id -> performGoogleSignIn()
            button_google_logout.id -> performGoogleLogout()
        }
    }
}
