
import retrofit2.Call
import retrofit2.http.*

interface MyApiEndpointInterface {
    // Request method and URL specified in the annotation
    @GET("weather?")
    fun getWeather(@Query("zip") zip: String, @Query("appid") appid: String, @Query("units") units: String): Call<CurrentWeather>
}