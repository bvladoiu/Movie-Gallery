package com.vaha.challenge.ui.movielist

import MovieAdapter
import MovieSearchResult
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vaha.challenge.R
import com.vaha.challenge.model.Error
import com.vaha.challenge.network.SyncService.Companion.QUERY
import com.vaha.challenge.model.movie.Movies
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {

    var isLoading = false
    lateinit var movieAdapter: MovieAdapter
    var page = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        width = display!!.width

        loadQueriesFromCache()
        movieList.layoutManager = LinearLayoutManager(this)

        search.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                initSearch()
            }
            false
        })

        fab.setOnClickListener { view ->
            initSearch()
        }
    }

    /**
     * EventBus method that delivers a page of movies to the ui
     * TODO: had the app had more data types: save all data in a centralized in memory cache and use this method only for signaling the ui data is available in the cache!
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: MovieSearchResult) {
        loadQueriesFromCache()
        var position = movieAdapter.itemList.size
        if (event.results.isEmpty()) {
            movieList.clearOnScrollListeners()
            if (movieAdapter.itemList.isEmpty()) {
                showEmptySearchResult()
            }
        } else {
            showMovieList()
            movieAdapter.itemList.addAll(event.results)
            movieAdapter.notifyItemRangeInserted(position, event.results.size)
            isLoading = false
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(error: Error) {
        showError()
    }

    /**
     *  Caching the search query only if the screen is displaying results, i.e. the query was legitimate!
     *  TODO: cache also the search results!
     */
    private fun loadQueriesFromCache() {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, Movies.getQueries(this)
        );
        search.setAdapter(adapter);
    }

    /**
     * Preparing the ui,creating an endless adapter and resetting the results page, to receive the new search results
     */
    private fun initSearch() {
        hideSoftKeyboard()
        negativeMessage.visibility = GONE

        page = 1
        Movies.search(this, search.text.toString().trim(), page)

        movieAdapter = MovieAdapter(ArrayList())
        movieList.adapter = movieAdapter
        movieList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == (movieAdapter.itemList.size - 5)) {
                        //bottom of list!
                        loadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    /**
     * Increases the page number and loads the next movie result page. (Safe - api returns empty list if page exceeds actual max value)
     */
    private fun loadMore() {
        Movies.search(this, search.text.toString().trim(), ++page)
    }


    /**
     * Prepares the screen to show the non-empty search results, hides everything else
     */
    private fun showMovieList() {
        negativeMessage.visibility = GONE
        movieList.visibility = VISIBLE
    }

    /**
     * Prepares the screen to show an error, hides everything else
     */
    private fun showError() {
        movieList.visibility = GONE
        negativeMessage.visibility = VISIBLE
        negativeMessage.text = getString(R.string.error)
    }

    /**
     * Prepares the screen to show the empty message, hides everything else
     */
    private fun showEmptySearchResult() {
        negativeMessage.text = getString(R.string.empty_search_result)
        negativeMessage.visibility = VISIBLE
        movieList.visibility = GONE
    }

    /**
     * Function used to hide keyboard when search is engaged
     */
    private fun hideSoftKeyboard() {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        if (movieAdapter.itemCount > 0) {
            savedInstanceState.putString(QUERY, search.text.toString().trim())
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val query = savedInstanceState.getString(QUERY)
        if (!TextUtils.isEmpty(query)) {
            search.setText(query)
            initSearch()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    /**
     * Width seems to be available only for activities and needs to be passed to the movie adapter in order to compute the appropriate poster size
     */
    companion object {
        var width = 0
    }
}