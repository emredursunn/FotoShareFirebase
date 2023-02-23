package com.example.fotosharefirebase

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylas.*
import java.security.Permission
import java.util.UUID

class FotografPaylasActivity : AppCompatActivity() {

    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null

    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylas)
        storage = FirebaseStorage.getInstance()
        database = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
    }

    fun paylas(view: View) {

        //storage işleri
        val uuid = UUID.randomUUID()
        val reference = storage.reference
        val gorselReference = reference.child("Images").child("${uuid}.jpg")
        if (secilenGorsel != null) {
            gorselReference.putFile(secilenGorsel!!).addOnCompleteListener {
                gorselReference.downloadUrl.addOnSuccessListener {uri ->
                    val downloadUrl = uri.toString()
                    val kullaniciemail = auth.currentUser!!.email.toString()
                    val yorum = aciklamaEditText.text.toString()
                    val tarih = Timestamp.now()

                    //veritabanı işlemleri
                    val postHashMap = hashMapOf<String,Any>()
                    postHashMap.put("Fotograf Url",downloadUrl)
                    postHashMap.put("Kullanici",kullaniciemail)
                    postHashMap.put("Yorum",yorum)
                    postHashMap.put("Tarih",tarih)

                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if(it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {exception ->
                        Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {exception ->
                    Toast.makeText(this,exception.localizedMessage,Toast.LENGTH_LONG).show() }
            }
        }
    }


    fun gorselSec(view: View) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //izin alınmamış
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )
        } else {
            //izin alınmış galeriye git
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            secilenGorsel = data.data
            if (secilenGorsel != null){
                if(Build.VERSION.SDK_INT >= 28){
                    val source = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(source)
                    FotografView.setImageBitmap(secilenBitmap)
                }else{
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    FotografView.setImageBitmap(secilenBitmap)
                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)

    }

}