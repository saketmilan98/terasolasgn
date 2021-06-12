package com.terasolasgn.movieinfo.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.terasolasgn.movieinfo.R
import com.terasolasgn.movieinfo.model.MovieDataClassItem
import com.terasolasgn.movieinfo.utils.Tools
import com.terasolasgn.movieinfo.viewmodel.MainActViewModel
import com.terasolasgn.movieinfo.viewmodel.MovieDetailViewModel
import kotlinx.android.synthetic.main.movie_detail_fragment.*
import java.io.Serializable
import java.util.concurrent.TimeUnit

class MovieDetailFragment : Fragment() {

    companion object {
        fun newInstance(param1: Serializable) =
            MovieDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("ARG_PARAM1", param1)
                }
            }
    }

    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var viewModelShared: MainActViewModel
    lateinit var movieData : MovieDataClassItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)
        viewModelShared = ViewModelProvider(requireActivity()).get(MainActViewModel::class.java)
        movieData = arguments?.getSerializable("ARG_PARAM1") as MovieDataClassItem
        setupLayouts()

    }

    fun setupLayouts(){ //for setting up  data in appropriate layouts
        movieData.let {
            iv1_detail_frag.load(it.info.image_url)
            tv1_detail_frag.text = (it.title + " (${it.year})")
            tv2_detail_frag.text = ("Rank ${it.info.rank} | ${TimeUnit.SECONDS.toMinutes(it.info.running_time_secs.toLong())} mins | ${Tools().returnStringOfArray(it.info.genres!!)}")
            tv4_detail_frag.text = it.info.plot
            tv5_detail_frag.text = ("Director(s): ${Tools().returnStringOfArray(it.info.directors)}\n" + "Actor(s): ${Tools().returnStringOfArray(it.info.actors)}")
            if(it.info.rating != null){
                tv3_detail_frag.text = "${it.info.rating}"
            }
            else {
                tv3_detail_frag.visibility = View.GONE
            }
        }
        topToolbar_detail_frag.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}