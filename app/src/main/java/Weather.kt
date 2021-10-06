import kotlinx.serialization.Serializable

@Serializable
data class Weather(val id: Int, val main: String, val description: String, val icon: String)

//    "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],
