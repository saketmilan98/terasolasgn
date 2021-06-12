package com.terasolasgn.movieinfo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.terasolasgn.movieinfo.model.MovieDataClass

class MainActViewModel : ViewModel() {

    var item: MutableLiveData<MovieDataClass> = MutableLiveData<MovieDataClass>()

    var itemDirectorList: MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    var itemGenreList: MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    fun getItem(): MovieDataClass? {
        return item.value
    }

    fun setItem(item1: MovieDataClass) {
        this.item.value = item1
    }

    fun getDirectorList() : List<String>? {
        return itemDirectorList.value
    }

    fun setDirectorList(item1: List<String>) {
        this.itemDirectorList.value = item1
    }

    fun getGenreList() : List<String>? {
        return itemGenreList.value
    }

    fun setGenreList(item1: List<String>) {
        this.itemGenreList.value = item1
    }

}