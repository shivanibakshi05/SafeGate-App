package h.app.hackit.safegate

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mAuth: FirebaseAuth
    protected var mFirebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        mFirebaseUser = mAuth.currentUser
    }
}
