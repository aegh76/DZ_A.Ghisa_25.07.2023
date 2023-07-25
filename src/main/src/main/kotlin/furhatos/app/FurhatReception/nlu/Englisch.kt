import furhatos.nlu.Intent
//Erklärung: Vergleiche Klasse "nlu.Ja"

class Englisch : Intent() {
    //var name: String = ""
    //var vorname: String = ""
    override fun getExamples(lang: furhatos.util.Language): List<String> {
        return listOf(
            "english",
            "englisch",
            "England",
            "English too",
            "In english please"
        )
    }
    /*fun getFullName(): String =
        "$vorname $name"
*/
}