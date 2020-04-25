package com.android.weatherapp.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.android.weatherapp.R
import com.android.weatherapp.models.Weather
import com.android.weatherapp.remote.ICON_BASE_URL
import com.bumptech.glide.Glide
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherAdapter (
    private val context: Context,
    private var list: List<Weather>
) : BaseAdapter() {

    fun setData(data: List<Weather>){
        list = data
        notifyDataSetChanged()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val viewHolder: ViewHolder
        if(view == null){
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.list_item_weather, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        }else{
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.bind(list[position])
        return view
    }

    override fun getItem(position: Int): Any = list[position]

    override fun getItemId(position: Int): Long = list[position].dt

    override fun getCount(): Int = list.size

    inner class ViewHolder (view: View){
        private val defaultFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        private val desiredFormatter = SimpleDateFormat("EEE, MMM dd, hh:mm a", Locale.getDefault())
        private val today = Calendar.getInstance()
        //======================
        private val tvDate: TextView = view.findViewById(R.id.tv_date)
        private val tvWeatherDay: TextView = view.findViewById(R.id.tv_weather_day)
        private val tvWeatherNight: TextView = view.findViewById(R.id.tv_weather_night)
        private val imageView: ImageView = view.findViewById(R.id.imv)
        private val tvPhenomenon: TextView = view.findViewById(R.id.tv_phenomenon)
        private val tvSpeed: TextView = view.findViewById(R.id.tv_speed)
        private val tvCloud: TextView = view.findViewById(R.id.tv_cloud)
        private val tvToday: TextView = view.findViewById(R.id.tv_today)

        fun bind(weather: Weather){
            initDate(weather.date)
            tvWeatherDay.text = "${kevinToCelsius(weather.main.tempMin)} °C"
            tvWeatherNight.text = "${kevinToCelsius(weather.main.tempMax)} °C"
            tvSpeed.text = "${weather.wind.speed} m/s"
            tvCloud.text = "clouds: ${weather.clouds.all}%, ${weather.main.pressure} hPa"
            if(weather.weather.isNotEmpty()) {
                tvPhenomenon.text = weather.weather[0].description
                val url = "${ICON_BASE_URL}${weather.weather[0].icon}@2x.png"
                Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.weather_default)
                    .error(R.drawable.weather_default)
                    .override(24, 24)
                    .centerCrop()
                    .into(imageView);
            }
        }

        private val decimal = DecimalFormat("0.00")
        private fun kevinToCelsius(kevin: Float): String = decimal.format(kevin - 273.15F)

        private fun initDate(dateStr:String) {
            try{
                val date = defaultFormatter.parse(dateStr)
                if(date == null){
                    tvDate.text = dateStr
                    return
                }
                if(isToday(Calendar.getInstance().apply { this.time  = date  })){
                    tvToday.visibility = View.VISIBLE
                }else
                    tvToday.visibility = View.GONE
                tvDate.text = desiredFormatter.format(date)
            }catch (ex: Exception){
                tvDate.text = dateStr
            }
        }

        private fun isToday(specifiedDate: Calendar): Boolean{
            return today.get(Calendar.DAY_OF_MONTH) == specifiedDate.get(Calendar.DAY_OF_MONTH)
                    &&  today.get(Calendar.MONTH) == specifiedDate.get(Calendar.MONTH)
                    &&  today.get(Calendar.YEAR) == specifiedDate.get(Calendar.YEAR);
        }
    }

}