package h.app.hackit.safegate

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpManager(private val signUpCompleteListener: FirebaseSignUpCompleteListener) : OnCompleteListener<AuthResult>, CreateUserProfileManager.OnUserProfileChangedListener {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var username: String? = null

    interface FirebaseSignUpCompleteListener {
        fun onSignUpSuccess(user: FirebaseUser?)

        fun onSignUpFailure(message: String)
    }

    init {
        //Get Firebase firebaseAuth instance
    }

    fun signUpUser(username: String, userEmail: String, password: String) {
        this.username = username
        //create user
        firebaseAuth.createUserWithEmailAndPassword(userEmail, password).addOnCompleteListener(this)
    }

    override fun onComplete(task: Task<AuthResult>) {
        val user = task.result?.user
        val createUserProfileManager = CreateUserProfileManager(this)
        if (user != null) {
            createUserProfileManager.createFirebaseUserProfile(user, username!!)
        }
    }

    override fun onUserProfileChanged(username: String?) {
        signUpCompleteListener.onSignUpSuccess(firebaseAuth.currentUser)
    }

    override fun onUserProfileChangeFailed(message: String) {
        signUpCompleteListener.onSignUpFailure(message)
    }

}
