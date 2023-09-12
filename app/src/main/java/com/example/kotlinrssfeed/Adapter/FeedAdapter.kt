package com.example.kotlinrssfeed.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinrssfeed.Interface.ItemClickListener
import com.example.kotlinrssfeed.Model.RSSObject
import com.example.kotlinrssfeed.R

class FeedViewHoder(itemView :View):RecyclerView.ViewHolder(itemView),View.OnClickListener,View.OnLongClickListener{

    var txtTitle :TextView
    var txtPubdate:TextView
    var txtContent:TextView

    private var itemClickListener : ItemClickListener ?= null
    init {
        txtTitle = itemView.findViewById(R.id.textTitle) as TextView
        txtPubdate = itemView.findViewById(R.id.textPubdate) as TextView
        txtContent = itemView.findViewById(R.id.textContent) as TextView

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
    override fun onClick(p0: View?) {
        itemClickListener?.onClick(p0,adapterPosition,false)
    }

    override fun onLongClick(p0: View?): Boolean {
        itemClickListener?.onClick(p0,adapterPosition,false)
        return true
    }

}
class FeedAdapter(private val rssObject: RSSObject, private val mContext:Context):RecyclerView.Adapter<FeedViewHoder>(){
    private val infalter:LayoutInflater
    init {
        infalter = LayoutInflater.from(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHoder {
        val intView = infalter.inflate(R.layout.row,parent,false)
        return FeedViewHoder(intView)
    }

    override fun getItemCount(): Int {
        if(rssObject.item != null){
            return rssObject.item.size
        }else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: FeedViewHoder, position: Int) {
        holder.txtTitle.text = rssObject.item[position].title
        holder.txtContent.text = rssObject.item[position].content
        holder.txtPubdate.text = rssObject.item[position].pubDate

        holder.setItemClickListener(ItemClickListener{view, position, isLongClick ->
            if(!isLongClick){
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(rssObject.item[position].link))
                mContext.startActivity(browserIntent)
            }
        })
    }


}