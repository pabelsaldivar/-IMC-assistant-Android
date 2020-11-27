package mx.moobile.imcassistant.entity

class ImcModel (
    var age: Int? = null,
    var gender: Boolean? = null,
    var height:Double = 0.0,
    var weight:Double = 0.0
) {

//    val result = {
//        val heightOnMeters = height / 100
//        val b = (heightOnMeters * heightOnMeters)
//        val result = weight / b
//        result
//    }

    fun result(): Double {
        val heightOnMeters = height / 100
        val b = (heightOnMeters * heightOnMeters)
        val result = weight / b
        return result
    }

    fun desiredWeight(): String {
        if (level() == 3) {
            return animatedPhrase()
        } else {
            val heightOnMeters = height / 100
            val doubleHeight = heightOnMeters * heightOnMeters
            val desiredWeight = 21.7 * doubleHeight
            val diferenceWeight = (weight - desiredWeight).toInt()
            print("Peso correcto: $desiredWeight")
            if (diferenceWeight == 0) {
                return animatedPhrase()
            }
            if (weight < desiredWeight) {
                return "Le faltan ${diferenceWeight}kg"
            } else {
                return "Se pasa por ${diferenceWeight}kg"
            }
        }
    }

    fun animatedPhrase(): String {
        val randomInt = (0..6).random()
        return when(randomInt) {
            0 ->  "Excelentes noticias!"
            1 ->  "Estas en tu peso ideal."
            3 ->  "Todo bien por aqui"
            4 ->   "Buen resutado."
            5 ->  "...y muy saludable."
            6 ->  "Todo bien."
            else ->   "Estas en tu peso."
        }
    }

    fun resultFormatted(): String  {
        return String.format( "%.2f", result())
    }

    fun level(): Int {
        return when(result()) {
            in 1.0..16.0 -> 0
            in 1.0..16.9 -> 1
            in 1.0..18.4 -> 2
            in 1.0..24.9 -> 3
            in 1.0..29.9 -> 4
            in 1.0..34.9 -> 5
            in 1.0..39.9 -> 6
            in 1.0..40.0 -> 7
            else -> -36
        }
    }

    fun levelName(): String  {
        return when (level()) {
            0 -> "Delgadez muy severa"
            1 -> "Delgadez severa"
            2 -> "Delgadez"
            3 -> "Normal"
            4 -> "Sobrepeso"
            5 -> "Obesidad"
            6 -> "Obesidad severa"
            7 -> "Obesidad muy severa"
            else ->  ""
        }
    }

    fun levelSortName(): String  {
       return when (level()) {
            0 -> "Delgadez"
            1 -> "Delgadez"
            2 -> "Delgadez"
            3 -> "Normal"
            4 -> "Sobrepeso"
            5 -> "Obesidad"
            6 -> "Obesidad"
            7 -> "Obesidad"
            else ->  ""
        }
    }

    fun levelIMCRange(): String {
       return when (level()) {
            0 -> "Menor de 16.0"
            1 -> "16.0 - 16.9"
            2 -> "17.0 - 18.4"
            3 -> "18.5 - 24.9"
            4 -> "25.0 - 29.9"
            5 -> "30.0 - 34.9"
            6 -> "35.0 - 39.9"
            7 -> "Mayor a 40"
            else ->  ""
        }
    }

    fun levelIMCRangeData(): levelIMCRangeDataModel{
       return when (level()) {
            0 -> levelIMCRangeDataModel(16.0,16.0)
            1 -> levelIMCRangeDataModel(16.0,16.9)
            2 -> levelIMCRangeDataModel(17.0,18.4)
            3 -> levelIMCRangeDataModel(18.5,24.9)
            4 -> levelIMCRangeDataModel(25.0,29.9)
            5 -> levelIMCRangeDataModel(30.0,34.9)
            6 -> levelIMCRangeDataModel(35.0,39.9)
            7 -> levelIMCRangeDataModel(40.0,40.0)
            else ->  levelIMCRangeDataModel(40.0,40.0)
        }
    }

    fun weightRangeFormatted(): String {
        val heightOnMeters = height / 100
        val doubleHeight = heightOnMeters * heightOnMeters
        val firstWeight = levelIMCRangeData().a * doubleHeight
        val secondWeight = levelIMCRangeData().b * doubleHeight

        return String.format( "%.1f", firstWeight) + " - " + String.format( "%.1f", secondWeight)
    }
}

class levelIMCRangeDataModel(
        var a: Double,
        var b: Double
)