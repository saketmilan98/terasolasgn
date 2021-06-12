package com.terasolasgn.movieinfo.utils

import android.content.Context
import android.widget.Toast
import com.terasolasgn.movieinfo.model.MovieDataClass

class Tools {

    fun showToast(str : String, ctx : Context){ //method for showing toast
        Toast.makeText(ctx, str, Toast.LENGTH_LONG).show()
    }

    fun returnStringOfArray(arr : List<String>?) : String{ // this method will take a list of string as input and return a single string having all items separated by comma
        var tempStr = ""
        if(arr.isNullOrEmpty()){
            return tempStr
        }
        else {
            for (element in arr) {
                tempStr += "$element, "
            }
        }
        return tempStr.substringBeforeLast(", ")
    }

    fun returnListOfDirectors(dataa : MovieDataClass) : List<String>{ //this method will return a hashset of director name from all the movies
        val hashSet = HashSet<String>()
        dataa.forEach{it1->
            it1.info.directors?.forEach {it2->
                hashSet.add(it2)
            }
        }
        return hashSet.toList()
    }

    fun returnListOfGenres(dataa : MovieDataClass) : List<String>{ //this method will return a hashset of genre name from all the movies
        val hashSet = HashSet<String>()
        dataa.forEach{it1->
            it1.info.genres?.forEach {it2->
                hashSet.add(it2)
            }
        }
        return hashSet.toList()
    }

    fun checkKeyPresentInList(arr : List<String>?, key : String) : Boolean{ //this method checks whether a given key is present in given list of string
        return if(arr.isNullOrEmpty()){
            false
        } else {
            arr.contains(key)
        }
    }

    fun returnListOfRatings() : List<String> { //this method returns list of string having numbers from 1.0 to 10.0 incremented by 0.1
        val listOfRatings = ArrayList<String>()
        for(k in 10..100){
            listOfRatings.add("${k/10F}       ")
        }
        return listOfRatings
    }

    fun checkRatingInRange(rating : Double?, from : Float, to : Float) : Boolean{ // this method checks whether a given value lies within a given range
        return if(rating != null){
            rating in from..to
        } else {
            false
        }
    }

}