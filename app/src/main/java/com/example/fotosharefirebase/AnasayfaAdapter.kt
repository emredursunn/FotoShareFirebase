package com.example.fotosharefirebase

import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fotograf_paylas.*
import kotlinx.android.synthetic.main.post_itemview.view.*

class AnasayfaAdapter(val postListesi: ArrayList<PostModel>) : RecyclerView.Adapter<AnasayfaAdapter.PostVH>() {
    class PostVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostVH {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_itemview,parent,false)
        return PostVH(itemView)
    }

    override fun getItemCount(): Int {
        return postListesi.size
    }

    override fun onBindViewHolder(holder: PostVH, position: Int) {
        holder.itemView.kullaniciTextView.text = postListesi[position].kullanici
        holder.itemView.yorumTextView.text = postListesi[position].yorum
        Picasso.get().load(postListesi[position].fotografUrl).into(holder.itemView.fotografView)
    }
}