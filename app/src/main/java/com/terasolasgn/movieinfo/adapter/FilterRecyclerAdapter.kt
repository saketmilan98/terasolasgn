package com.terasolasgn.movieinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.terasolasgn.movieinfo.R
import com.terasolasgn.movieinfo.view.HomeFragment
import java.util.ArrayList

class FilterRecyclerAdapter(var dataa: ArrayList<String>, val fragmentManager: FragmentManager, val context: Context?, val fragment : Fragment, val type : String) : androidx.recyclerview.widget.RecyclerView.Adapter<FilterRecyclerAdapter.ViewHolder>() {

    var selected = -1

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.filter_rvlayout, p0, false))
    }

    override fun getItemCount() =
            dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(selected == position){
            holder.checkIv.visibility = View.VISIBLE
        }
        else {
            holder.checkIv.visibility = View.GONE
        }

        holder.titleTv.text = dataa[position]
        holder.itemView.setOnClickListener {
            (fragment as HomeFragment).filterMovies(type, dataa[position])
            selected = position
            notifyDataSetChanged()
        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val titleTv = view.findViewById<TextView>(R.id.tv1_filterrvl)
        val checkIv = view.findViewById<ImageView>(R.id.iv1_filterrvl)
    }

    fun addItems(items : ArrayList<String>){
        dataa.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems(){
        dataa = ArrayList()
        notifyDataSetChanged()
    }

}