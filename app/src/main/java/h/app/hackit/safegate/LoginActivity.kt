package h.app.hackit.safegate

import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), BaseFragment.LoginListener {
    override fun onLoginStart() {
    }

    override fun onLoginEnd() {
    }

    override fun onStart() {
        super.onStart()
        if (mFirebaseUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbarLogin)

        setViewPager()
    }

    private fun setViewPager() {
        pagerLogin.adapter = LoginPagerAdapter(supportFragmentManager)
        tabLogin.setupWithViewPager(pagerLogin)
    }
}
