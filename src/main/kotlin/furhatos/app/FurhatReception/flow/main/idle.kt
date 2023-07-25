package furhatos.app.blankskill1.flow.main


import Greetinglanguage
import furhat.libraries.standard.UsersLib.usersLib
import furhatos.app.blankskill1.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.util.Gender
import furhatos.util.Language

val Idle: State = state(Parent) {
    init {




        furhat.usersLib.attendClosestUser()
        if(users.count > 0)
        {
            //Befinden sich mehr als 0 User in Furhat's Reichweite, so leuchtet der LED Ring Furhat's in Magenta.
            furhat.ledStrip.solid(java.awt.Color.MAGENTA)

            //Furhat schaut den ihm nächsten User an
            furhat.usersLib.attendClosestUser()

            //und geht in den nächsten State "(Greetingname") über
            goto(Greetinglanguage)
        }
        //Falls genau 0 User in Furhat's Reichweite sind, schaut er niemanden an.
        else(furhat.attendNobody())
    }

    onEntry {

        furhat.setInputLanguage(Language.ENGLISH_US, Language.GERMAN, Language.TURKISH)
        //Mit furhat.voice wird die NLU ausgewählt, die Furhat für die Interaktion verwenden soll.
        //furhat.voice = PollyVoice.Hans()
        furhat.setVoice(Language.GERMAN, Gender.MALE, false)

        if (users.count < 1) {
            furhat.attendNobody()
        } else {
            furhat.attend(users.other)
            goto(Greetinglanguage)
        }
    }

    onUserEnter {
        furhat.attend(it)
        goto(Greetinglanguage)
    }

    onReentry {
        furhat.attend(users.other)
        goto(Greetinglanguage)
    }

}
