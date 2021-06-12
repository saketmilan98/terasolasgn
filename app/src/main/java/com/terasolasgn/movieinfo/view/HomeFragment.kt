package com.terasolasgn.movieinfo.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.terasolasgn.movieinfo.R
import com.terasolasgn.movieinfo.model.MovieDataClass
import com.terasolasgn.movieinfo.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val text = resources.openRawResource(R.raw.moviedata).bufferedReader().use { it.readText() }
        val mainMovieList: MovieDataClass = Gson().fromJson(text, MovieDataClass::class.java)


    }

}