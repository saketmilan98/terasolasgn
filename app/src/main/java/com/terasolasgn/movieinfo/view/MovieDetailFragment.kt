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
            tv2_detail_frag.text = ("by ${Tools().returnStringOfArrayWithAnd(it.info.directors)}")
            if(it.info.rating != null){
                rb1_detail_frag.rating = it.info.rating.toFloat()/2F
                tv3_detail_frag.text = ("${it.info.rating}/10")
            }
            else {
                rb1_detail_frag.visibility = View.GONE
                tv3_detail_frag.visibility = View.GONE
            }
            if(it.info.rank != null){
                tv6_detail_frag.text = ("Rank ${it.info.rank}")
            }

            tv4_detail_frag.text = Tools().returnStringOfArray(it.info.genres, " | ")
            tv7_detail_frag.text = ("Cast: ${Tools().returnStringOfArrayWithAnd(it.info.actors)}")
            if(it.info.plot != null){
                tv5_detail_frag.text = it.info.plot
            }
        }
        topToolbar_detail_frag.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}