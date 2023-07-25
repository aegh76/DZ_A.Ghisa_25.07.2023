
import furhatos.app.blankskill1.flow.Parent
import furhatos.app.blankskill1.flow.main.Idle
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.Voice
import furhatos.gestures.Gestures
import furhatos.skills.UserManager.current
import furhatos.util.Language
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime


val Greeting : State = state(Parent) {
    onEntry {


        /*furhat.ask (){
            //furhat stellt eine Frage und zieht während der Frage seine Brauen hoch, async = flase sorgt dafür,
            //dass der State erst nach Beendigung der Gesture weiterläuft
            +"Hallo Haben Sie für mich einen ${furhat.voice.emphasis("QR-Code")} oder wollen Sie mir die Patientennummer, Nummer für Nummer diktieren"
            furhat.gesture(Gestures.BrowRaise, async = false)
        }

         */
        furhatask(
            furhat=this.furhat,
            englishText = "Hello do you have a ${furhat.voice.emphasis("QR code")} " +
        "Or do you have a patient number?",
            germanText =  "Hallo Haben Sie einen ${furhat.voice.emphasis("QR-Code")} " +
                    "oder haben Sie eine Patientennummer?",
            turkishText = "Merhaba Bir ${furhat.voice.emphasis("QR code")} " +
            "ya da bir hasta numaranız var mı?" ,
            sprache = Benutzer!!.get("sprache") as Language

        )
    }
    onResponse<Patientennummer> {
        furhat.attend(user= current)

        furhatsay(furhat=this.furhat,
            englishText = "Good, then I can help ${furhat.voice.emphasis("you")} further.",
            germanText =  "Gut, dann kann ich ${furhat.voice.emphasis("Ihnen")} weiterhelfen.",
            turkishText = "Güzel, o zaman ${furhat.voice.emphasis("size")} daha fazla yardımcı olabilirim.",
            sprache = Benutzer!!.get("sprache") as Language)


            //mit ${furhat.voice.emphasis("Ihnen")} kann Furhat einzelne Abschnitte betonen.

            furhat.gesture(Gestures.BigSmile, async = false)


        //Nun wird die Variable Benutzer mit dem User überschrieben, der auf dei Frage geantwortet hat.
        Benutzer = users.getUser(it.userId)

        //Der Benutzer wird von Furhat angeschaut.
        furhat.attend(user= current)

        //Mit der Funktion GetDigitsPatient wird die Frage nach der Patientennummer des Gesprächspartners gestellt.
        //User kann nicht mehr null sein deswegen Benutzer!!, da der Benutzer bereits gesetzt.
        GetDigitsKunde(Benutzer!!, this.furhat)
        goto(ValidierungNummerKunde)

    }

    onResponse<FrageWiederholen> {
        furhat.attend(user= current)
        reentry()
    }

    onResponse<QRCode>{
        Benutzer = users.getUser(it.userId)

        furhatsay(furhat=this.furhat,
            englishText = "Okay, then please show me your QR code.",
            germanText =  "Okay, dann zeig mir bitte dein QR-Code",
            turkishText = "Tamam, o zaman lütfen bana QR kodunuzu gösterin",
            sprache = Benutzer!!.get("sprache") as Language)


        captureImageFromSocket(benutzer = Benutzer!!)
        Benutzer!!.put("Patientennummer", Benutzer!!.get("QR Code Text"))
        furhat.say("${Benutzer!!.get("Patientennummer")}")
        ReadExcel(Benutzer!!, this.furhat,
            networkDrivePath = "smb://na-filer-w.thm.intern/daten/Projekte/Robotik/Furhat/projekte/Belegungsplan_Dialysezentrum/Furhat.csv",
            password = "FB07Tutor2022@THM", username = "07-Tutor")

        /*  Eingaben fürs DZ
            ReadExcel(Benutzer!!, this.furhat,
            networkDrivePath = "smb://10.203.31.70/mv/furhat/Furhat.csv",
            password = "Furhat2023", username = "Furhat")*/

        val raumy: Any? = Benutzer!!.get("raum")
        val platzx: Any? = Benutzer!!.get("platz")
        val dialysebeginn = furhat.voice.sayAs(Benutzer!!.get("dialysebeginn").toString(), Voice.SayAsType.TIME)
        val dialyseende: Any = furhat.voice.sayAs(Benutzer!!.get("dialyseende").toString(), Voice.SayAsType.TIME)
        val datum = furhat.voice.sayAs(Benutzer!!.get("datum").toString(), Voice.SayAsType.TIME)


        //Der Nutzer wird über seine Termindaten informiert und weiß somit, wann, wo und wie lange seine Dialyse
        //stattfinden wird.


        furhatsay(furhat=this.furhat,
            englishText = "Alright, ${Benutzer!!.get("name")}. I would change ${furhat.voice.emphasis("please")} go to " +
            " the room${furhat.voice.emphasis("$raumy")} + ${furhat.voice.pause("1000ms") }+ to bed  ${furhat.voice.emphasis("$platzx")} " +
            "to go. Your dialysis will start at $dialysebeginn start and end at $dialyseende end on $datum",
            germanText =  "Gut, ${Benutzer!!.get("name")}. Ich würde Sie ${furhat.voice.emphasis("bittten")} in " +
                    "den Raum${furhat.voice.emphasis("$raumy")} + ${furhat.voice.pause("1000ms") }+ an den  Platz ${furhat.voice.emphasis("$platzx")} " +
                    "zu gehen. Ihre Dialyse fängt um $dialysebeginn an und endet um $dialyseende am $datum",
            turkishText = "Şey, ${Benutzer!!.get("isim")}. Ben ${furhat.voice.emphasis("sor")}'ı oda" +
            "${furhat.voice.emphasis("$raumy")} + ${furhat.voice.pause("1000ms") }+ Yer'a ${furhat.voice.emphasis("$platzx")} " +
            "gitmek için. Diyaliziniz $dialysebeginn başlangıcında başlayacak ve $datum $dialyseende bitişinde sona erecektir",
            sprache = Benutzer!!.get("sprache") as Language)





       /* furhat.say (
            "Gut, ${Benutzer!!.get("name")}. Ich würde Sie ${furhat.voice.emphasis("bittten")} in " +
                    "den${furhat.voice.emphasis("$raumy")} + ${furhat.voice.pause("1000ms") }+ an den  PLatz ${furhat.voice.emphasis("$platzx")} " +
                    "zu gehen. Ihre Dialyse fängt um $dialysebeginn an und endet um $dialyseende am $datum")
*/

        val currentDateTime = LocalDateTime.now(ZoneId.of("Europe/Berlin"))
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formattedDateTime = currentDateTime.format(formatter)

        furhatsay(furhat=this.furhat,
            englishText = "Current time and date: $formattedDateTime",
            germanText =  "Aktuelle Uhrzeit und Datum: $formattedDateTime",
            turkishText = "Geçerli saat ve tarih: $formattedDateTime",
            sprache = Benutzer!!.get("sprache") as Language)






        // furhat.say("Aktuelle Uhrzeit und Datum: $formattedDateTime")


        onResponse<Nein>  {
            furhat.attend(user= current)
            goto(AngehoerigeUndTaxifahrer)
        }

        delay(3500)
        goto(Idle)
    }

    onResponse<Nein>  {
        furhat.attend(user= current)
        goto(AngehoerigeUndTaxifahrer)
    }
    onResponse<FrageWiederholen> {
        furhat.attend(user= current)
        reentry()
    }
    onResponseFailed {
        reentry()
    }

}





