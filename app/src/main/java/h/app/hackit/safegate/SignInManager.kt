package h.app.hackit.safegate

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

open class SignInManager(private var firebaseAuth: FirebaseAuth, private val mListener: FirebaseSignInCompleteListener) : OnCompleteListener<AuthResult>, OnFailureListener {
    override fun onFailure(p0: Exception) {
        mListener.onSignInFailure(p0.message!!)
    }

    interface FirebaseSignInCompleteListener {
        fun onSignInSuccess(user: FirebaseUser)

        fun onSignInFailure(message: String)
    }

    override fun onComplete(task: Task<AuthResult>) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            mListener.onSignInSuccess(user)
        } else {
            mListener.onSignInFailure(task.exception!!.message!!)
        }
    }

    fun signInUser(userEmail: String, password: String) {
        //create user
        firebaseAuth.signInWithEmailAndPassword(userEmail, password).addOnCompleteListener(this)
                .addOnFailureListener(this)
    }
}
