package com.terasolasgn.movieinfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import coil.load
import com.terasolasgn.movieinfo.R
import com.terasolasgn.movieinfo.model.MovieDataClass
import com.terasolasgn.movieinfo.model.MovieDataClassItem
import com.terasolasgn.movieinfo.utils.Tools
import com.terasolasgn.movieinfo.view.HomeFragment
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class HomeFragRecyclerAdapter(var dataa: ArrayList<MovieDataClassItem>, val fragmentManager: FragmentManager, val context: Context?, val fragment : Fragment) : androidx.recyclerview.widget.RecyclerView.Adapter<HomeFragRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.homefrag_rvlayout, p0, false))
    }

    override fun getItemCount() =
            dataa.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //holder.textView.text = ("Total Fare : ${dataa[position].totalFare} \nTotal Earning : ${dataa[position].totalEarning} \nTotal Commission : ${dataa[position].totalCommision}")
        dataa[position].let {
            holder.posterIv.load(it.info.image_url)
            holder.titleDateTv.text = (it.title + " (${it.year})")
            holder.rankDurationGenreTv.text = ("Rank ${it.info.rank} | ${TimeUnit.SECONDS.toMinutes(it.info.running_time_secs.toLong())} mins | ${Tools().returnStringOfArray(it.info.genres!!)}")
            holder.plotTv.text = it.info.plot
            holder.directorActorTv.text = ("Director(s): ${Tools().returnStringOfArray(it.info.directors)}\n" + "Actor(s): ${Tools().returnStringOfArray(it.info.actors)}")
            if(it.info.rating != null){
                holder.ratingTv.text = "${it.info.rating}"
            }
            else {
                holder.ratingTv.visibility = View.GONE
            }
        }

        holder.itemView.setOnClickListener {
            //(fragment as HomeFragment).showBottomSheetSort()
        }
    }

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val posterIv = view.findViewById<ImageView>(R.id.iv1_hfrvl)
        val titleDateTv = view.findViewById<TextView>(R.id.tv1_hfrvl)
        val rankDurationGenreTv = view.findViewById<TextView>(R.id.tv2_hfrvl)
        val ratingTv = view.findViewById<TextView>(R.id.tv3_hfrvl)
        val plotTv = view.findViewById<TextView>(R.id.tv4_hfrvl)
        val directorActorTv = view.findViewById<TextView>(R.id.tv5_hfrvl)
    }

    fun addItems(items : ArrayList<MovieDataClassItem>){
        dataa.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems(){
        dataa = ArrayList()
        notifyDataSetChanged()
    }

}