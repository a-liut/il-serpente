package it.aliut.ilserpente.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import it.aliut.ilserpente.R
import it.aliut.ilserpente.user.User
import kotlinx.android.synthetic.main.settings_fragment.*
import kotlinx.android.synthetic.main.settings_fragment.view.*
import timber.log.Timber

class SettingsFragment : Fragment(), View.OnClickListener {

    private val RC_SIGN_IN = 42

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val layout = inflater.inflate(R.layout.settings_fragment, container, false)

        layout.button_google_signin.setOnClickListener(this)

        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            inputedit_user_name.setText(user.name)
            Glide.with(this)
                .load(user.photoUrl ?: R.drawable.ic_launcher_foreground)
                .into(image_user_picture)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.setUserName(inputedit_user_name.text.toString())
    }

    private fun performGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .build()

        val client = GoogleSignIn.getClient(context!!, gso)

        startActivityForResult(client.signInIntent, RC_SIGN_IN)
    }

    private fun onLoginCompleted(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)

            updateUser(account!!)
        } catch (ex: ApiException) {
            Snackbar.make(
                button_google_signin,
                "Cannot sign in with Google. Retry later.",
                Snackbar.LENGTH_LONG
            ).show()
            Timber.d("Google SignIn failed: ${ex.statusCode}")
        }
    }

    private fun updateUser(account: GoogleSignInAccount) {
        viewModel.setUserName(
            account.displayName ?: account.givenName ?: account.familyName ?: User.DEFAULT_NAME
        )
        viewModel.setUserPhotoUrl(account.photoUrl.toString())
    }

    override fun onClick(view: View) {
        if (view.id == button_google_signin.id) {
            performGoogleLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            onLoginCompleted(task)
        }
    }
}
