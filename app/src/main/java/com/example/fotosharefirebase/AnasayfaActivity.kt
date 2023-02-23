package com.example.fotosharefirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_anasayfa.*

class AnasayfaActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore
    var postListesi = ArrayList<PostModel>()
    private lateinit var adapter : AnasayfaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anasayfa)
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        verileriAl()
        anasayfa_recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnasayfaAdapter(postListesi)
        anasayfa_recyclerView.adapter = adapter
    }


    fun verileriAl(){
        database.collection("Post").orderBy("Tarih",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if(error != null){
                        Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
                }else{
                    if(value != null && !value.isEmpty){
                        val documents = value.documents
                        postListesi.clear()
                        for(document in documents){
                            val kullanici= document.get("Kullanici") as String
                            val yorum= document.get("Yorum") as String
                            val fotografUrl= document.get("Fotograf Url") as String
                            postListesi.add(PostModel(fotografUrl,kullanici,yorum))
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }
}

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.fotografPaylasOption){
            //FOTOĞRAF PAYLAŞMA AKTİVİTESİNE GİDECEK
            val intent = Intent(this,FotografPaylasActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.cikisYapOption){
            //GİRİŞ SAYFASINA GİDECEK
            auth.signOut()
            val intent = Intent(this,KullaniciGirisiActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }


}