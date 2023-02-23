package com.example.fotosharefirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_kullanicigirisi.*

class KullaniciGirisiActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kullanicigirisi)

        auth = FirebaseAuth.getInstance()

        val mevcutKullanici = auth.currentUser
        if (mevcutKullanici!=null){
            val intent = Intent(this,AnasayfaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun girisYap(view: View){
        val email = emailAddressText.text.toString()
        val password = passwordText.text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                //DİĞER AKTİVİTEYE GİT
                val intent = Intent(this,AnasayfaActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }

    fun kaydol(view : View){
        val email = emailAddressText.text.toString()
        val password = passwordText.text.toString()
        try {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                if (it.isSuccessful){
                    //DİĞER AKTİVİTEYE GİT
                    val intent = Intent(this,AnasayfaActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}