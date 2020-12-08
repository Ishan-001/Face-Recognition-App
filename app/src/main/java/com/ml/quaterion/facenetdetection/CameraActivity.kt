package com.ml.quaterion.facenetdetection

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.name_dialog.*
import kotlinx.android.synthetic.main.name_dialog.view.*
import java.io.File
import java.io.FileOutputStream

class CameraActivity:AppCompatActivity(){

    private val CAMERA_REQUEST=1
    private lateinit var name:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        add.setOnClickListener {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST)
        }
        verify.setOnClickListener { startActivity(Intent(baseContext, MainActivity::class.java)) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            val bitmap = data!!.extras!!["data"] as Bitmap?
            openDialog(bitmap!!)
        }else
            super.onActivityResult(requestCode, resultCode, data)
    }

    private fun openDialog(bitmap: Bitmap){
        val dialog= Dialog(this@CameraActivity)
        dialog.setContentView(R.layout.name_dialog)
        dialog.setTitle("Add Image")
        dialog.save_button.setOnClickListener {
            name=dialog.input.text.toString().trim()
            createDirectoryAndSaveFile(bitmap, name)
            Snackbar.make(layout, "Image saved", Snackbar.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.cancel_button.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun createDirectoryAndSaveFile(imageToSave: Bitmap, fileName: String) {
        val path= baseContext.externalCacheDir!!.absolutePath + "/images" + "/$fileName"
        val direct = File(path)
        if (!direct.exists()) {
            val imageDirectory = File(path)
            imageDirectory.mkdirs()
        }
        val file = File(path, "$fileName.jpeg")
        if (file.exists()) {
            file.delete()
        }
        try {
            val out = FileOutputStream(file)
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}