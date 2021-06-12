package com.terasolasgn.movieinfo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.terasolasgn.movieinfo.model.MovieDataClassItem

class MainActViewModel : ViewModel() {

    var item: MutableLiveData<MovieDataClassItem> = MutableLiveData<MovieDataClassItem>()

    fun getItem(): MovieDataClassItem? {
        return item.value
    }

    fun setItem(item1: MovieDataClassItem) {
        this.item.value = item1
    }

}