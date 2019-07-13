package h.app.hackit.safegate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

class SignInFragment : BaseFragment(), SignInManager.FirebaseSignInCompleteListener {
    override fun onSignInSuccess(user: FirebaseUser) {
        Toast.makeText(context, "Welcome Back" + user.email, Toast.LENGTH_SHORT).show()
        startActivity(Intent(context, MainActivity::class.java))
        loginListener.onLoginEnd()
    }

    override fun onSignInFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        loginListener.onLoginEnd()
    }

    private lateinit var signInManager: SignInManager

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        btnLogin.isSelected = etEmail.text.toString().length > 5
    }

    companion object {

        fun newInstance(): SignInFragment {

            val args = Bundle()

            val fragment = SignInFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onButtonClicked() {
        if (btnLogin.isSelected) {
            signInManager = SignInManager(firebaseAuth, this)
            loginListener.onLoginStart()
            signInManager.signInUser(etEmail.text.toString().trim(), etPass.text.toString().trim())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(getLayoutID(), container, false)
        v.etEmail.addTextChangedListener(this)
        v.btnLogin.setOnClickListener(this)
        return v
    }

    override fun init() {
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_sign_in
    }
}