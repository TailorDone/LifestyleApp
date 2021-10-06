import kotlinx.serialization.Serializable

@Serializable
class Sys(val type: Int, val id: Int, val country: String, val sunrise: Double, val sunset: Double)

//    "sys":{"type":2,"id":2019646,"country":"GB","sunrise":1631338156,"sunset":1631384717},

