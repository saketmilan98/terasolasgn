package com.terasolasgn.movieinfo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.terasolasgn.movieinfo.R
import com.terasolasgn.movieinfo.model.MovieDataClass
import com.terasolasgn.movieinfo.utils.Tools
import com.terasolasgn.movieinfo.viewmodel.MainActViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainActViewModel::class.java)
        val text = resources.openRawResource(R.raw.moviedata).bufferedReader().use { it.readText() }
        val mainMovieList: MovieDataClass = Gson().fromJson(text, MovieDataClass::class.java)
        viewModel.setItem(mainMovieList)
        viewModel.setDirectorList(Tools().returnListOfDirectors(mainMovieList))
        viewModel.setGenreList(Tools().returnListOfGenres(mainMovieList))
        supportFragmentManager.beginTransaction().add(R.id.frame_container_mainact, HomeFragment()).commit()



    }
}