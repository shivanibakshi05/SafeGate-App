package h.app.hackit.safegate

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

import java.util.Objects

class CreateUserProfileManager(private val onUserProfileChangedListener: OnUserProfileChangedListener) : OnCompleteListener<Void> {

    private var user: FirebaseUser? = null

    interface OnUserProfileChangedListener {

        fun onUserProfileChanged(username: String?)

        fun onUserProfileChangeFailed(message: String)
    }

    fun createFirebaseUserProfile(user: FirebaseUser, username: String) {
        this.user = user
        val addProfileName = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build()
        user.updateProfile(addProfileName).addOnCompleteListener(this)
    }

    override fun onComplete(task: Task<Void>) {
        if (task.isSuccessful) {
            onUserProfileChangedListener.onUserProfileChanged(user!!.displayName)
        } else {
            onUserProfileChangedListener.onUserProfileChangeFailed(Objects.requireNonNull<Exception>(task.exception).message!!)
        }
    }
}
