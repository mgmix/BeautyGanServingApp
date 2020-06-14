package dev.mgmix.beautyganservingapp

import android.Manifest
import android.app.Dialog
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import gun0912.tedbottompicker.TedRxBottomPicker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_result.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MainActivity : AppCompatActivity() {

    val api = Provider.api
    val dialog: Dialog by lazy {

        Dialog(this, R.style.AppTheme).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.layout_result)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var srcPart: MultipartBody.Part? = null
        var refPart: MultipartBody.Part? = null

        // Add Permission
        setPermission()


        srcButton.setOnClickListener {
            TedRxBottomPicker.with(this)
                .show()
                .subscribe({
                    // Show SrcImage
                    Log.d("TEST", " $it: ");
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(srcImage)
                    // Store Uri Path
                    val file = File(it.path.toString())
                    val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    srcPart = MultipartBody.Part.createFormData("srcImage", file.name, requestBody)

                }) {
                    it.printStackTrace()
                }
        }

        refButton.setOnClickListener {
            TedRxBottomPicker.with(this)
                .show()
                .subscribe({
                    // Show SrcImage
                    Log.d("TEST", " $it: ");
                    Glide.with(this)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(refImage)
                    // Store Uri Path
                    val file = File(it.path.toString())
                    val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    refPart = MultipartBody.Part.createFormData("refImage", file.name, requestBody)

                }) {
                    it.printStackTrace()
                }
        }

        execute.setOnClickListener {
            // TODO API Call and Popup Loading View and
            if (srcPart == null || refPart == null) {
                Toast.makeText(this, "사진을 넣어주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressBar.visibility = View.VISIBLE
            api.uploadImage(
                srcPart ?: return@setOnClickListener, refPart ?: return@setOnClickListener
            )
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    executeDialog(it.path)
                }
                .doOnSuccess { progressBar.visibility = View.INVISIBLE }
                .doOnError { progressBar.visibility = View.INVISIBLE }
                .subscribeOn(Schedulers.io())
                .subscribe({}) {
                    it.printStackTrace()
                }
        }

    }

    private fun setPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {

            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                finish()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("사진 촬영 및 업로드를 위해서는 [권한] 설정에서 권한 허용이 필요합니다.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }

    private fun executeDialog(path: String) {
        dialog.apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            Glide.with(this@MainActivity)
                .load(Provider.baseUrl + "/image/" + path)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(image)

            val size = Point()
            windowManager.defaultDisplay.getSize(size)
            window?.setLayout(size.x, size.y)
            show()

            close.setOnClickListener {
                dialog.dismiss()
            }

        }
    }

}