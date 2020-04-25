package com.android.weatherapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.weatherapp.R
import com.android.weatherapp.WeatherApp
import com.android.weatherapp.utils.LocationUtil
import com.android.weatherapp.viewmodels.WeatherActivityViewModel
import javax.inject.Inject

class WeatherActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var progressBar: ProgressBar
    //private lateinit var btnReload: Button
    private lateinit var searchView: SearchView
    private lateinit var btnLocation: ImageButton
    private lateinit var tvFeedback: TextView
    private lateinit var tvCity: TextView
    private lateinit var layData: ViewGroup
    private lateinit var adapter: WeatherAdapter
    private var cityFromLocation: String? = null
    private lateinit  var locationService: LocationUtil

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: WeatherActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        //UI
        listView = findViewById(R.id.listview)
        progressBar = findViewById(R.id.progressbar)
        searchView = findViewById(R.id.searchview)
        btnLocation = findViewById(R.id.btn_location)
        tvFeedback = findViewById(R.id.tv_feedback)
        tvCity = findViewById(R.id.tv_city)
        layData = findViewById(R.id.lay_list)
        adapter = WeatherAdapter(this, arrayListOf())
        listView.adapter = adapter
        //btnReload = findViewById(R.id.btn_reload)
        //initialize dagger for view model
        val daggerComponent =  (application as WeatherApp).daggerComponent()
        daggerComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherActivityViewModel::class.java)
        observe()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(city: String?): Boolean {
                viewModel.loadData(city)
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        locationService = LocationUtil(this, object: LocationUtil.IListener{
            override fun onRequestPermission(vararg permission: String) {}

            override fun onError(msg: String) {
                Toast.makeText(this@WeatherActivity, msg, Toast.LENGTH_LONG).show()
            }

            override fun onCityFound(city: String) {
                viewModel.loadData(city)
            }
        })
        btnLocation.setOnClickListener {
            if( !TextUtils.isEmpty(cityFromLocation) && cityFromLocation == viewModel.currentCity()) return@setOnClickListener
            locationService.findCurrentCity()
        }
        //Load for Save State
        if(savedInstanceState != null){
            val query = savedInstanceState.getString(SEARCH_QUERY_KEY)
            query?.let { searchView.setQuery(it, false) }
        } else
            viewModel.data.value.let { if(it == null)locationService.findCurrentCity()  }
    }

    private fun observe(){
        //for data
        viewModel.data.observe(this, Observer {
            if(it == null) return@Observer
            layData.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            tvFeedback.visibility = View.GONE
            //load data to ui
            tvCity.text = getString(R.string.city_text, it.city.name, it.city.country)
            adapter.setData(it.list)
            searchView.setQuery(null,false)
        })
        //for error msg
        viewModel.errorMsg.observe(this, Observer {
            progressBar.visibility = View.GONE
            layData.visibility = View.VISIBLE
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
        //for progress
        viewModel.isFetching.observe(this, Observer {
            if (it){
                progressBar.visibility = View.VISIBLE
                layData.visibility = View.GONE
                tvFeedback.visibility = View.GONE
            } else
                if(progressBar.visibility != View.GONE) progressBar.visibility = View.GONE
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == LocationUtil.REQUEST_CODE)
            locationService.onPermissionGranted(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val query = searchView.query.toString()
        if(!TextUtils.isEmpty(query))
            outState.putString(query,SEARCH_QUERY_KEY)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "SEARCH_QUERY_KEY"
    }
}
