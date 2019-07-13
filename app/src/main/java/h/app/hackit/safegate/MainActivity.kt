package h.app.hackit.safegate

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.FloatingActionButton
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity : BaseActivity(), View.OnClickListener {

    internal var isFABOpen = false

    private var textImageView: ImageView? = null
    private var decodedTextView: TextView? = null

    private var fabAdd: FloatingActionButton? = null
    private var fabCamera: FloatingActionButton? = null
    private var fabGallery: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialise view components
        setUpView()
        setSupportActionBar(toolbarMain)

        //------------------------------------------------------------------
        // onClick listeners for floating buttons
        //------------------------------------------------------------------
        fabAdd!!.setOnClickListener(this)
        fabCamera!!.setOnClickListener(this)
        fabGallery!!.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.activity_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_logout -> {
                startActivity(Intent(this, LoginActivity::class.java))
                mAuth.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!isFABOpen) {
            super.onBackPressed()
        } else {
            closeFABMenu()
        }// call closeFABMenu() to hide floating action buttons, if visible.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // close fab menu
        closeFABMenu()

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == AppConstants.CAMERA_REQUEST) {

                val photo = data!!.extras!!.get("data") as Bitmap
                detectTextFromImage(photo)

            } else if (requestCode == AppConstants.PICK_IMAGE && null != data) {

                try {

                    val photo = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                    detectTextFromImage(photo)

                } catch (e: IOException) {

                    e.printStackTrace()
                }

            }
        }
    }

    override fun onClick(view: View) {

        when (view.id) {

            R.id.fabAdd -> if (!isFABOpen) {
                showFABMenu()
            } else {
                closeFABMenu()
            }

            R.id.fabCamera -> {
                // start camera intent
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, AppConstants.CAMERA_REQUEST)
            }

            R.id.fabGallery -> {
                // start gallery intent
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstants.PICK_IMAGE)
            }
        }
    }

    /**
     * Initialise view components
     */
    private fun setUpView() {

        fabAdd = findViewById<View>(R.id.fabAdd) as FloatingActionButton
        fabCamera = findViewById<View>(R.id.fabCamera) as FloatingActionButton
        fabGallery = findViewById<View>(R.id.fabGallery) as FloatingActionButton

        textImageView = findViewById<View>(R.id.image_view) as ImageView
        decodedTextView = findViewById<View>(R.id.text_value) as TextView
    }

    //-------------------------------------------------------------------------
    // floating action button animations
    //-------------------------------------------------------------------------
    private fun showFABMenu() {
        isFABOpen = true
        fabCamera!!.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fabGallery!!.animate().translationY(-resources.getDimension(R.dimen.standard_105))
    }

    private fun closeFABMenu() {
        isFABOpen = false
        fabCamera!!.animate().translationY(0f)
        fabGallery!!.animate().translationY(0f)
    }

    /**
     * Detects and image for any text and displays in textView.
     *
     * @param bitmap is the image loaded from camera or gallery
     */
    private fun detectTextFromImage(bitmap: Bitmap) {

        // create the TextRecognizer
        val textRecognizer = TextRecognizer.Builder(this@MainActivity).build()

        // check if the TextRecognizer is operational.
        if (!textRecognizer.isOperational) {
            Toast.makeText(this@MainActivity, R.string.text_recogniser_error_message, Toast.LENGTH_LONG).show()

            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, R.string.low_storage_error, Toast.LENGTH_LONG).show()
            }
        }

        // create Frame instance from the bitmap to supply to the detector
        // returns a sparseArray of text data
        val frame = Frame.Builder().setBitmap(bitmap).build()
        // call detector synchronously with a frame to detect text data
        val items = textRecognizer.detect(frame)

        // create an empty string builder
        val stringBuilder = StringBuilder()

        // append detected data to string builder
        for (i in 0 until items.size()) {

            val item = items.valueAt(i)
            stringBuilder.append(item.value)
            stringBuilder.append("\n")
        }

        textImageView!!.setImageBitmap(bitmap)
        decodedTextView!!.text = stringBuilder.toString()
    }
}
