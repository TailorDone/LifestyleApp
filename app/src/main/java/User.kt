class User (val name: String,
            val city: String,
            val country: String,
            val sex: String,
            val ageStr: String,
            val heightStr: String,
            val weightStr: String,
){
    val nameArray = name.split(" ").toTypedArray()
    var fname : String = nameArray[0]
    var lname : String = ""
    var age: Int = ageStr.toInt()
    var height: Double = heightStr.toDouble()
    var weight: Double = weightStr.toDouble()

    init {
        if (nameArray.size > 1)
            lname = nameArray[1]
    }
}