package e.ashmita.weatherapp.dao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import e.ashmita.weatherapp.model.weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class WeatherViewmodel : ViewModel() {
    val _apiResponse = MutableStateFlow<weather?>(null)

    var city= "Agra"

    fun getData(cityName:String) {
        city= cityName
        viewModelScope.launch {
            val result = MyRetrofitBuilder.build().create(ApiInterface::class.java)
                .getWeatherData(cityName, "039275bf6c1e419fa678a6ab62542504", "metric")
            if (result.code() == 200) {
                _apiResponse.emit(result.body())
            }
        }





//        retrofit = MyRetrofitBuilder.build()
//        CoroutineScope(Dispatchers.IO).launch {
//            val result = retrofit.create(ApiInterface::class.java)
//                .getWeatherData("jaipur", "039275bf6c1e419fa678a6ab62542504", "metric")
//            if (result.code() == 200) {
//
//            }
//        }
    }
}

