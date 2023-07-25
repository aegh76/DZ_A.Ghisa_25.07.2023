import furhatos.nlu.Intent
//Erklärung: Vergleiche Klasse "nlu.Ja"

class Türkisch : Intent() {
    //var name: String = ""
    //var vorname: String = ""
    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf(
            "turkish",
            "türkisch",
            "Türkei",
            "Auch Türkisch"
        )
    }
    /*fun getFullName(): String =
        "$vorname $name"
*/
}