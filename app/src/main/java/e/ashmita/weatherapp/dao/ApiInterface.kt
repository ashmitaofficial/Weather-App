package e.ashmita.weatherapp.dao

import e.ashmita.weatherapp.model.weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("weather")
    suspend fun getWeatherData(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
    ): Response<weather>
}