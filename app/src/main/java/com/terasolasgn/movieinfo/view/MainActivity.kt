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
        val text = resources.openRawResource(R.raw.moviedata).bufferedReader().use { it.readText() } // reading moviedata json from raw folder and storing it in a string
        val mainMovieList: MovieDataClass = Gson().fromJson(text, MovieDataClass::class.java) // converting above generated string to dataclass object
        viewModel.setItem(mainMovieList) //storing generated dataclass object in viewmodel to allow it to be accessed by homefragment
        viewModel.setDirectorList(Tools().returnListOfDirectors(mainMovieList)) //storing list of directors in viewmodel to allow it to be accessed by homefragment for filtering by director
        viewModel.setGenreList(Tools().returnListOfGenres(mainMovieList)) //storing list of genres in viewmodel to allow it to be accessed by homefragment for filtering by director
        supportFragmentManager.beginTransaction().add(R.id.frame_container_mainact, HomeFragment()).commit()



    }
}