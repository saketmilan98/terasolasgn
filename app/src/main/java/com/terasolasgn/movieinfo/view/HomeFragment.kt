package com.terasolasgn.movieinfo.view

import android.app.AlertDialog
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.slider.Slider
import com.google.gson.Gson
import com.terasolasgn.movieinfo.R
import com.terasolasgn.movieinfo.adapter.FilterRecyclerAdapter
import com.terasolasgn.movieinfo.adapter.HomeFragRecyclerAdapter
import com.terasolasgn.movieinfo.model.MovieDataClass
import com.terasolasgn.movieinfo.model.MovieDataClassItem
import com.terasolasgn.movieinfo.utils.PaginationScrollListener
import com.terasolasgn.movieinfo.utils.Tools
import com.terasolasgn.movieinfo.viewmodel.HomeViewModel
import com.terasolasgn.movieinfo.viewmodel.MainActViewModel
import kotlinx.android.synthetic.main.filter_bs.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.sort_bs.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var viewModelShared: MainActViewModel

    lateinit var mainDataList : MovieDataClass
    lateinit var rvAdapter : HomeFragRecyclerAdapter
    lateinit var filterRvAdapter : FilterRecyclerAdapter
    lateinit var bottomSheetBehavior1: BottomSheetBehavior<ConstraintLayout> //for filter
    lateinit var bottomSheetBehavior2: BottomSheetBehavior<ConstraintLayout> //for sort

    var isLastPage1 = false
    var isLoading1 = false
    var currentPage = 0
    var offSet = 10

    var ratingFrom = 1F
    var ratingTo = 10F

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModelShared = ViewModelProvider(requireActivity()).get(MainActViewModel::class.java)
        mainDataList = viewModelShared.getItem()!!
        bottomSheetBehavior1 = BottomSheetBehavior.from(filter_bs)
        bottomSheetBehavior2 = BottomSheetBehavior.from(sort_bs)
        setupRecycler()
        setupBottomSheet()
        setupRatingTab()
        addItems()
        setupFilterSortButtons()
    }



    fun setupRecycler(){
        rvAdapter = HomeFragRecyclerAdapter(ArrayList(), activity?.supportFragmentManager!!, requireContext(), this)
        rv1_homefrag?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        rv1_homefrag?.adapter = rvAdapter

        rv1_homefrag?.addOnScrollListener(object : PaginationScrollListener(rv1_homefrag?.layoutManager as LinearLayoutManager) {
            override val isLastPage: Boolean
                get() = isLastPage1

            override val isLoading: Boolean
                get() = isLoading1


            override fun loadMoreItems() {
                isLoading1 = true
                currentPage += 1
                addItems()
            }
        })
        addItems()


    }

    fun addItems(){
        Log.e("insideAddItems", "true")
        val tempList = ArrayList<MovieDataClassItem>()
        //val startIndex = currentPage*offSet
        var endIndex = currentPage*offSet+9
        if(endIndex >= mainDataList.size){
            endIndex = mainDataList.size-1
            isLastPage1 = true
        }
        else {
            isLastPage1 = false
        }
        for(i in (currentPage*offSet)..endIndex){
            tempList.add(mainDataList[i])
        }
        rvAdapter.addItems(tempList)
        isLoading1 = false
    }

    fun setupFilterSortButtons(){
        iv1_toolbar.setOnClickListener {
            showClearFilterDialog()
        }
        iv2_toolbar.setOnClickListener {
            showBottomSheetSort()
        }
        iv3_toolbar.setOnClickListener {
            showBottomSheetFilter()
        }
    }

    fun filterMovies(type : String, key : String){
        mainDataList = viewModelShared.getItem()!!
        val tempFilteredList : List<MovieDataClassItem>
        if(type == "director"){
            tempFilteredList = mainDataList.filter {
                    s-> Tools().checkKeyPresentInList(s.info.directors, key)
            }
        }
        else if(type == "genre"){
            tempFilteredList = mainDataList.filter {
                    s-> Tools().checkKeyPresentInList(s.info.genres, key)
            }
        }
        else if(type == "rating"){
            tempFilteredList = mainDataList.filter {
                    s-> Tools().checkRatingInRange(s.info.rating, ratingFrom, ratingTo)
            }
        }
        else {
            tempFilteredList = mainDataList
        }
        hideBottomSheetFilter()
        mainDataList = MovieDataClass()
        mainDataList.addAll(tempFilteredList)
        currentPage = 0
        rvAdapter.clearItems()
        addItems()
    }

    fun showClearFilterDialog(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert")
        builder.setMessage("Do you want to clear all filter and sorting?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes"){dialogInterface, which ->
            setupBottomSheet()
            setupRatingTab()
            filterMovies("other","none")
        }
        //performing cancel action
        /*builder.setNeutralButton("Cancel"){dialogInterface , which ->
            Toast.makeText(this,"clicked cancel\n operation cancel",Toast.LENGTH_LONG).show()
        }*/
        //performing negative action
        builder.setNegativeButton("Cancel"){dialogInterface, which ->

        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    fun sortMovies(type : String, key : String){
        val tempFilteredList : List<MovieDataClassItem>
        if(type == "year"){
            if(key == "latestfirst"){
                tempFilteredList = mainDataList.sortedWith(compareByDescending { it.year })
            }
            else if(key == "oldestfirst"){
                tempFilteredList = mainDataList.sortedWith(compareBy { it.year })
            }
            else {
                tempFilteredList = mainDataList
            }
        }
        else if(type == "title"){
            if(key == "ascend_alphabet"){
                tempFilteredList = mainDataList.sortedWith(compareBy { it.title })
            }
            else if(key == "descend_alphabet"){
                tempFilteredList = mainDataList.sortedWith(compareByDescending { it.title })
            }
            else {
                tempFilteredList = mainDataList
            }
        }
        else {
            tempFilteredList = mainDataList
        }
        hideBottomSheetSort()
        mainDataList = MovieDataClass()
        mainDataList.addAll(tempFilteredList)
        currentPage = 0
        rvAdapter.clearItems()
        addItems()
    }


    fun setupBottomSheet(){
        bottomSheetBehavior1.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> { }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        coordL1_homefrag.setBackgroundColor(Color.parseColor("#33707070"))
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        coordL1_homefrag.setBackgroundColor(Color.parseColor("#00000000"))
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> { }
                    BottomSheetBehavior.STATE_SETTLING -> { }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { }
                }
            }
        })

        tv1_cl1_filter_bs.setOnClickListener {
            tv1_cl1_filter_bs.setTextColor(Color.WHITE)
            tv1_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  requireActivity().theme)

            tv2_cl1_filter_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv2_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv4_cl1_filter_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv4_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            filterRvAdapter = FilterRecyclerAdapter(ArrayList(), activity?.supportFragmentManager!!, requireContext(), this, "director")
            rv1_filter_bs?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rv1_filter_bs?.adapter = filterRvAdapter
            filterRvAdapter.addItems(viewModelShared.getDirectorList() as java.util.ArrayList<String>)
            sv1_filter_bs.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String): Boolean {
                    val tempDirectorList = (viewModelShared.getDirectorList() as java.util.ArrayList<String>).filter {
                            s -> s.contains(newText, true)
                    }
                    filterRvAdapter.clearItems()
                    filterRvAdapter.addItems(tempDirectorList as java.util.ArrayList<String>)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            })
            onDirectorOrGenreClicked()

        }

        tv2_cl1_filter_bs.setOnClickListener {
            tv1_cl1_filter_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv1_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv2_cl1_filter_bs.setTextColor(Color.WHITE)
            tv2_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  requireActivity().theme)

            tv4_cl1_filter_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv4_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            filterRvAdapter = FilterRecyclerAdapter(ArrayList(), activity?.supportFragmentManager!!, requireContext(), this, "genre")
            rv1_filter_bs?.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            rv1_filter_bs?.adapter = filterRvAdapter
            filterRvAdapter.addItems(viewModelShared.getGenreList() as java.util.ArrayList<String>)
            sv1_filter_bs.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextChange(newText: String): Boolean {
                    val tempGenreList = (viewModelShared.getGenreList() as java.util.ArrayList<String>).filter {
                        s -> s.contains(newText, true)
                    }
                    filterRvAdapter.clearItems()
                    filterRvAdapter.addItems(tempGenreList as java.util.ArrayList<String>)
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }

            })
            onDirectorOrGenreClicked()
        }

        tv4_cl1_filter_bs.setOnClickListener {
            tv1_cl1_filter_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv1_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv2_cl1_filter_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv2_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv4_cl1_filter_bs.setTextColor(Color.WHITE)
            tv4_cl1_filter_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  requireActivity().theme)
            onRatingClicked()
        }
        tv1_cl1_filter_bs.performClick()

        //setting sort bottomsheet

        bottomSheetBehavior2.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> { }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        coordL1_homefrag.setBackgroundColor(Color.parseColor("#33707070"))
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        coordL1_homefrag.setBackgroundColor(Color.parseColor("#00000000"))
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> { }
                    BottomSheetBehavior.STATE_SETTLING -> { }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { }
                }
            }
        })

        tv1_cl1_sort_bs.setOnClickListener {
            tv1_cl1_sort_bs.setTextColor(Color.WHITE)
            tv1_cl1_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  requireActivity().theme)

            tv2_cl1_sort_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv2_cl1_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)
            cl3_sort_bs.visibility = View.VISIBLE
            cl4_sort_bs.visibility = View.GONE
        }

        tv2_cl1_sort_bs.setOnClickListener {
            tv1_cl1_sort_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv1_cl1_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv2_cl1_sort_bs.setTextColor(Color.WHITE)
            tv2_cl1_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid,  requireActivity().theme)
            cl3_sort_bs.visibility = View.GONE
            cl4_sort_bs.visibility = View.VISIBLE
        }

        tv1_cl3_sort_bs.setOnClickListener {
            tv1_cl3_sort_bs.setTextColor(Color.WHITE)
            tv1_cl3_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid_black,  requireActivity().theme)

            tv2_cl3_sort_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv2_cl3_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)
            sortMovies("year", "latestfirst")
        }

        tv2_cl3_sort_bs.setOnClickListener {
            tv1_cl3_sort_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv1_cl3_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv2_cl3_sort_bs.setTextColor(Color.WHITE)
            tv2_cl3_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid_black,  requireActivity().theme)
            sortMovies("year", "oldestfirst")
        }

        tv1_cl4_sort_bs.setOnClickListener {
            tv1_cl4_sort_bs.setTextColor(Color.WHITE)
            tv1_cl4_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid_black,  requireActivity().theme)

            tv2_cl4_sort_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv2_cl4_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)
            sortMovies("title", "ascend_alphabet")
        }

        tv2_cl4_sort_bs.setOnClickListener {
            tv1_cl4_sort_bs.setTextColor(ResourcesCompat.getColor(resources, R.color.customColor1, requireActivity().theme))
            tv1_cl4_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_black_prior,  requireActivity().theme)

            tv2_cl4_sort_bs.setTextColor(Color.WHITE)
            tv2_cl4_sort_bs.background = ResourcesCompat.getDrawable(resources, R.drawable.radio_high_solid_black,  requireActivity().theme)
            sortMovies("title", "descend_alphabet")
        }

        tv1_cl1_sort_bs.performClick()

    }

    fun showBottomSheetFilter(){
        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hideBottomSheetFilter(){
        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun showBottomSheetSort(){
        bottomSheetBehavior2.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hideBottomSheetSort(){
        bottomSheetBehavior2.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun setupRatingTab(){
        val ratingList = Tools().returnListOfRatings()
        val adapter1 = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, ratingList)
        spinner1_cl2_filter_bs.adapter = adapter1
        spinner1_cl2_filter_bs?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ratingFrom = ratingList[position].trim().toFloat()
            }
        }

        val adapter2 = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, ratingList)
        spinner2_cl2_filter_bs.adapter = adapter2
        spinner2_cl2_filter_bs?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ratingTo = ratingList[position].trim().toFloat()
            }
        }
        tv3_cl2_addedittaskact.setOnClickListener {
            if(ratingFrom <= ratingTo){
                filterMovies("rating", "none")
            }
            else {
                Tools().showToast("Please select a valid range", requireContext())
            }
        }
    }

    fun onRatingClicked(){
        cl2_filter_bs.visibility = View.VISIBLE
        tv3_cl2_addedittaskact.visibility = View.VISIBLE
        sv1_filter_bs.visibility = View.GONE
        rv1_filter_bs.visibility = View.GONE
    }
    fun onDirectorOrGenreClicked(){
        cl2_filter_bs.visibility = View.GONE
        tv3_cl2_addedittaskact.visibility = View.GONE
        sv1_filter_bs.visibility = View.VISIBLE
        rv1_filter_bs.visibility = View.VISIBLE
    }

}