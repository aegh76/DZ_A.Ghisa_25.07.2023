package furhatos.app.blankskill1.flow


import furhatos.app.blankskill1.flow.main.Idle
import furhatos.app.blankskill1.setting.DISTANCE_TO_ENGAGE
import furhatos.app.blankskill1.setting.MAX_NUMBER_OF_USERS
import furhatos.autobehavior.enableSmileBack
import furhatos.autobehavior.setDefaultMicroexpression
import furhatos.flow.kotlin.*
import furhatos.flow.kotlin.voice.PollyVoice
import furhatos.util.Gender
import furhatos.util.Language

//Im State "furhatos.app.blankskill1.flow.getInit" werden Parameter des Skills vor jedem Start initialisiert. States werden als Werte definiert.
//Die Klammer hinter "state" regelt die Vererbung von anderen States. In diesem Fall erbt der State vom State "Parent".

val Init : State = state {
    init {


        //Der dialogLogger sorgt dafür, dass die Gespräche mit Furhat unter
        //dem Pfad: C:\Users\Benutzer\.furhat\logs protokolliert werden.
        dialogLogger.startSession()


        //Mikroexpressionen sind kleine Gesichtsausdrücke, die kontinuierlich während eines Skill ablaufen,
        //so wirkt Furhat lebendiger. von "facialMovements = true" wird abgeraten, da Furhat das
        //Gesicht sonst als zu unruhig wahrgenommen werden könnte, wenn man ohnehin schon mit furhat.gestures arbeitet.
        furhat.setDefaultMicroexpression(blinking = true, facialMovements = false, eyeMovements = true)

        //furhat.enableSmileBack = true sorgt dafür, dass Furhat zurücklächelt, wenn der Gesprächspartner lacht.
        furhat.enableSmileBack = true

        //Mit users.setSimpleEngagementPolicy werden von Furhat Robotics definierte Default-Werte gesetzt.
        //Die Defaultwerte umfassen zum Beispiel die maximale Anzahl an zugelassenen Gesprächspartnern.
        users.setSimpleEngagementPolicy(DISTANCE_TO_ENGAGE, MAX_NUMBER_OF_USERS)

        //val logFile = File("N:\\Projekte\\Robotik\\Furhat\\projekte\\Demo02\\Flowlogger") //Log file can have any extension.
        //flowLogger.start(logFile) //Start the logger
        furhat.cameraFeed.enable()

        //Mit dem goto() Befehl definiert man welcher State der Anwendung als nächstes eingeleitet wird.
        goto(Idle)
    }
}
