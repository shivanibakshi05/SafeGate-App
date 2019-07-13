package h.app.hackit.safegate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : BaseFragment(), SignUpManager.FirebaseSignUpCompleteListener {
    override fun onSignUpFailure(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        loginListener.onLoginEnd()
    }

    override fun onSignUpSuccess(user: FirebaseUser?) {
        startActivity(Intent(context, MainActivity::class.java))
        loginListener.onLoginStart()
    }

    private lateinit var signUpManager: SignUpManager
    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        btnLogin.isSelected = etEmail.text.toString().length > 5
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(getLayoutID(), container, false)
        view.etEmail.addTextChangedListener(this)
        view.btnLogin.setOnClickListener(this)
        return view
    }

    companion object {

        fun newInstance(): SignUpFragment {

            val args = Bundle()

            val fragment = SignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onButtonClicked() {
        if (btnLogin.isSelected) {
            signUpManager = SignUpManager(this)
            loginListener.onLoginStart()
            signUpManager.signUpUser(etEmail.text.toString(), etEmail.text.toString(), etPass.text.toString())
        }
    }

    override fun init() {
    }

    override fun getLayoutID(): Int {
        return R.layout.fragment_sign_up
    }
}