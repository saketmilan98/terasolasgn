package com.terasolasgn.movieinfo.utils

import android.content.Context
import android.widget.Toast
import com.terasolasgn.movieinfo.model.MovieDataClass

class Tools {

    fun showToast(str : String, ctx : Context){
        Toast.makeText(ctx, str, Toast.LENGTH_LONG).show()
    }

    fun returnStringOfArray(arr : List<String>?) : String{
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

    fun returnListOfDirectors(dataa : MovieDataClass) : List<String>{
        val hashSet = HashSet<String>()
        dataa.forEach{it1->
            it1.info.directors?.forEach {it2->
                hashSet.add(it2)
            }
        }
        return hashSet.toList()
    }

    fun returnListOfGenres(dataa : MovieDataClass) : List<String>{
        val hashSet = HashSet<String>()
        dataa.forEach{it1->
            it1.info.genres?.forEach {it2->
                hashSet.add(it2)
            }
        }
        return hashSet.toList()
    }

    fun checkKeyPresentInList(arr : List<String>?, key : String) : Boolean{
        return if(arr.isNullOrEmpty()){
            false
        } else {
            arr.contains(key)
        }
    }

    fun returnListOfRatings() : List<String> {
        val listOfRatings = ArrayList<String>()
        for(k in 10..100){
            listOfRatings.add("${k/10F}       ")
        }
        return listOfRatings
    }

    fun checkRatingInRange(rating : Double?, from : Float, to : Float) : Boolean{
        return if(rating != null){
            rating in from..to
        } else {
            false
        }
    }

}