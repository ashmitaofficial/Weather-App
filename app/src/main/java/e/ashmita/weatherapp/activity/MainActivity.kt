package e.ashmita.weatherapp.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import e.ashmita.weatherapp.R
import e.ashmita.weatherapp.dao.WeatherViewmodel
import e.ashmita.weatherapp.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//039275bf6c1e419fa678a6ab62542504

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewmodel by viewModels<WeatherViewmodel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewmodel.getData("Agra")
        searchCity()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel._apiResponse.collectLatest { response ->
                    val temp = response?.main?.temp.toString()
                    val humidity = response?.main?.humidity
                    val windSpeed = response?.wind?.speed
                    val sunRise = response?.sys?.sunrise?.toLong()
                    val sunSet = response?.sys?.sunset?.toLong()
                    val seaLevel = response?.main?.pressure
                    val condition = response?.weather?.firstOrNull()?.main ?: "unknown"
                    val maxTemp = response?.main?.temp_max
                    val minTemp = response?.main?.temp_max
                    binding.celciusNum.text = "$temp °C"
                    binding.maxTemp.text = "Max Temp: $maxTemp °C"
                    binding.minTemp.text = "Min Temp: $minTemp °C"
                    binding.weatherType.text = condition
                    binding.humidity.text = "$humidity %"
                    binding.windspeed.text = "$windSpeed m/s"
                    binding.sunRise.text = "${time(sunRise)}"
                    binding.sunSet.text = "${time(sunSet)}"
                    binding.seaLvl.text = "$seaLevel hPa"
                    binding.weatherCondition.text = condition
                    binding.date.text = date()
                    binding.cityName.text = viewmodel.city
                    binding.weekday.text = dayName(System.currentTimeMillis())

                    changeBgAccToWeatherCondition(condition)
                }
            }
        }
    }

    private fun date(): String? {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long?): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        if (timestamp != null) {
            return sdf.format((Date(timestamp.times(1000))))
        }
        return "default"
    }


    private fun searchCity() {
        val searchView = binding.search
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewmodel.getData(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun changeBgAccToWeatherCondition(weatherCondition: String) {
        when (weatherCondition) {
            "Haze", "Partly Clouds", "Clouds", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.catdodweather)
                binding.lottieAnimation.setAnimation(R.raw.cloudy)
            }

            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunrise)
                binding.lottieAnimation.setAnimation(R.raw.sunny)
            }

            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain", "Rain" -> {
                binding.root.setBackgroundResource(R.drawable.raindrop)
                binding.lottieAnimation.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snowfall)
                binding.lottieAnimation.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunrise)
                binding.lottieAnimation.setAnimation(R.raw.sunny)
            }
        }

        binding.lottieAnimation.playAnimation()
    }

}