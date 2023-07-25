
import furhatos.app.blankskill1.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyNeuralVoice
import furhatos.flow.kotlin.voice.PollyVoice
import furhatos.records.User
import furhatos.util.Gender
import furhatos.util.Language



var Beneutzer: User? = null

//Der State Greeting erbt von dem State Parent, dort ist das User-Handling definiert.
val Grüße : State = state(Parent) {
    onEntry {

        //Zu Beginn des States wird definiert, dass Furhat den aktuellen User weiterhin anschaut.
        furhat.attend(users.current)

        furhat.ask("Ich kann Englisch, Deutsch und Türkisch reden und verstehen, sage mir einfach in welcher Sprache du fortfahren möchtest")
        furhat.setInputLanguage(Language.ENGLISH_US, Language.GERMAN, Language.TURKISH)
    }

    onResponse() {
        Benutzer = users.current
        furhat.say(" ${it.speech.language.code}")
        //furhat.say("${it.language}")

        var sprachcode: Language? = it.speech.language
        Benutzer!!.put("sprache", it.speech.language)
        var codesprache: String  = Benutzer!!.get("sprache").toString()
        furhat.say("$codesprache")
        if (Benutzer!!.get("sprache") as Language == Language.TURKISH){
            furhat.voice = PollyVoice.Filiz()
        }
        else{
            furhat.setVoice(Benutzer!!.get("sprache") as Language, Gender.MALE, true)
        }
        furhatsay(
            furhat = this.furhat,
            germanText = "OK deutsch",
            englishText = "OK english",
            turkishText = "Tamam Türkçe",
            sprache = it.speech.language
        )

        goto(Frage)
    }


}
