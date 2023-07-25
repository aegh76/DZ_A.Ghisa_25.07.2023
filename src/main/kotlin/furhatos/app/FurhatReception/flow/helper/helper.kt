

import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import furhatos.flow.kotlin.*
import furhatos.skills.UserManager.current
import furhatos.util.Gender
import furhatos.util.Language
import org.zeromq.SocketType
import org.zeromq.ZContext
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import jcifs.CIFSContext
import jcifs.config.PropertyConfiguration
import jcifs.context.BaseContext
import jcifs.smb.NtlmPasswordAuthenticator
import jcifs.smb.SmbFile
import java.io.File
import furhatos.flow.kotlin.Furhat
import furhatos.records.User


//In der helper.kt sind alle benötigten Funktionen der Interaktion definiert.

fun furhatask (furhat:Furhat, englishText: String, turkishText: String, germanText: String, sprache: Language) {
    if (sprache == Language.ENGLISH_US) {
        furhat.ask(text=englishText)
    }
    if (sprache == Language.TURKISH) {
        furhat.ask(text=turkishText)
    }
    if (sprache == Language.GERMAN) {
        furhat.ask(text=germanText)
    }
}

fun furhatsay (furhat:Furhat, englishText: String, turkishText: String, germanText: String, sprache: Language) {
    if (sprache == Language.ENGLISH_US) {
        furhat.say(text=englishText)
    }
    if (sprache == Language.TURKISH) {
        furhat.say(text=turkishText)
    }
    if (sprache == Language.GERMAN) {
        furhat.say(text=germanText)
    }
}

fun furhatsetVoice (furhat:Furhat, sprache: Language) {
    if (sprache == Language.ENGLISH_US) {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE, setInputLanguage = true)
    }
    if (sprache == Language.TURKISH) {
        furhat.setVoice(Language.TURKISH, Gender.MALE, setInputLanguage = true)
    }
    if (sprache == Language.GERMAN) {
        furhat.setVoice(Language.GERMAN, Gender.MALE, setInputLanguage = true)
    }
}




fun GetDigitsKunde(Benutzer: User, furhat: Furhat) {
    val sprache = Benutzer.get("sprache") as Language

    val sprachNachricht = when (sprache) {
        Language.GERMAN -> "Sag mir bitte die Patientennummer Ziffer für Ziffer"
        Language.ENGLISH_US -> "Please tell me the patient number digit by digit"
        Language.TURKISH -> "Lütfen bana hasta numarasını rakam rakam söyleyin"
        else -> "Wie lautet die Patientennummer Ihres Kunden"
    }

    furhat.askFor<furhatos.nlu.common.Number>(
        sprachNachricht,
        timeout = 20000,
        endSil = 5000
    ) {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user = current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
    }
}

/*
fun GetDigitsKunde(Benutzer: User, furhat: Furhat) {


furhat.askFor<furhatos.nlu.common.Number>("") {
    val sprache = Benutzer.get("sprache") as Language

    if (sprache == Language.GERMAN) {
        furhat.askFor<furhatos.nlu.common.Number>(
            "Sag mir bitte die Patientennummer Ziffer für Ziffer",
            timeout = 20000,
            endSil = 5000
        )
    } else if (sprache == Language.ENGLISH_US) {
        furhat.askFor<furhatos.nlu.common.Number>(
            "Please tell me the patient number digit by digit",
            timeout = 20000,
            endSil = 5000
        )
    } else if (sprache == Language.TURKISH) {
        furhat.askFor<furhatos.nlu.common.Number>(
            "Lütfen bana hasta numarasını rakam rakam söyleyin",
            timeout = 20000,
            endSil = 5000
        )
    } else {
        // Standardfrage für andere Sprachen (Deutsch in diesem Fall)
        furhat.askFor<furhatos.nlu.common.Number>(
            "Wie lautet die Patientennummer Ihres Kunden",
            timeout = 20000,
            endSil = 5000
        )
    }

    onResponse<furhatos.nlu.common.Number> {
        furhat.attend(user = current)
        val x: String = it.text.toString().replace(" ".toRegex(), "")
        val resultx: String = x.filter { it.isDigit() }
        Benutzer.put("Patientennummer", resultx)
        goto(ValidierungNummerKunde)
    }

}
}
*/


    /*
    fun GetDigitsKunde (Benutzer: User, furhat: Furhat) {

    //Mit furhat.askFor fragt furhat nach einem spezifischem intent. In diesem Fall eine Nummer.
        furhat.askFor<furhatos.nlu.common.Number>("Sagen Sie mir bitte die Patientennummer ziffer für ziffer", timeout = 20000, endSil = 5000)
        {
            //Antwortet der Nutzer mit einer Nummer, so triggert onResponse<Number>.
            onResponse<furhatos.nlu.common.Number> {
                //Furhat schaut den User an, der geantwortet hat
                furhat.attend(user= current)


                //Mit it.text kann das Gesagte des Nutzers manipuliert werden. Mit toString wird aus dem Gesagten
                //ein String gemacht. Anschließend werden alle Leerzeichen mit replace ersetzt.
                val x: String = it.text.toString().replace(" ".toRegex(), "")

                //Aus der Variable X werden alle Buchstaben entfernt, lediglich Zahlen bleiben über.
                //So kann der Nutzer auch sagen: "Meine Patientennummer ist XXXXX. Auch in diesem Fall,
                //würde nur die Patientennummer verarbeitet werden.
                val resultx: String = x.filter { it.isDigit() }

                //Schließlich wird das field Patientennummer mit der vollends korrekt manipulierten Variable
                //resultx beschrieben, dem nutzer ist jetzt eine Patientennummer zugeordnet.
                Benutzer.put("Patientennummer", resultx)
                goto(ValidierungNummerKunde)
            }
            onNoResponse {
                //Antwortet der Nutzer gar nicht, so startet Furhat die Frage (Funktion) nochmals.
                reentry()
            }
            onResponse<FrageWiederholen> {
                //Fordert der Nutzer Furhat auf die Frage noch einmal zu wiederholen,
                // so wiederholt auch durch diesen trigger Furhat die Frage nochmal.
                reentry()
            }
        }
    }

    */

fun GetDigitsKundenochmal(Benutzer: User, furhat: Furhat) {
    val sprache = Benutzer.get("sprache") as Language

    val sprachNachricht = when (sprache) {
        Language.GERMAN -> "Könnten sie mir bitte nochmal die Patientenummer sagen am besten so 5  ${
                            furhat.voice.pause("1000ms")},  6 ${furhat.voice.pause("1000ms")} und so weiter"

        Language.ENGLISH_US -> "Could you please tell me the patient number again best like this 5  ${
                                furhat.voice.pause("1000ms")},  6 ${furhat.voice.pause("1000ms")} and so on"

        Language.TURKISH -> "Lütfen bana hasta numarasını tekrar en iyi şekilde söyler misiniz 5  ${
                             furhat.voice.pause("1000ms")},  6 ${furhat.voice.pause("1000ms")} ve bu şekilde devam eder"

        else -> "Könnten sie mir bitte nochmal die Patientenummer sagen am besten so 5 + " +
                "${furhat.voice.pause("1000ms")}, + 6 ${furhat.voice.pause("1000ms")} und so weiter"
    }

    furhat.askFor<furhatos.nlu.common.Number>(
        sprachNachricht,
        timeout = 20000,
        endSil = 5000
    ) {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user = current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
    }
}




/*
fun GetDigitsKundenochmal (Benutzer: User, furhat: Furhat) {
//Die GetDigitsNochmal-Funktionen unterscheiden sich nur in der Frage nach der Patientennummer.
    furhat.askFor<furhatos.nlu.common.Number>("") {

        val sprache = Benutzer.get("sprache") as Language

        if (sprache == Language.GERMAN) {
            furhat.askFor<furhatos.nlu.common.Number>(
                "Könnten sie mir bitte nochmal die Patientenummer sagen am besten so 5 + ${furhat.voice.pause("1000ms") }, + 6 ${furhat.voice.pause("1000ms") } und so weiter",
                timeout = 20000,
                endSil = 5000
            )
        } else if (sprache == Language.ENGLISH_US) {
            furhat.askFor<furhatos.nlu.common.Number>(
                "Could you please tell me the patient number again best like this 5 + ${furhat.voice.pause("1000ms") }, + 6 ${furhat.voice.pause("1000ms") } and so on",
                timeout = 20000,
                endSil = 5000
            )
        } else if (sprache == Language.TURKISH) {
            furhat.askFor<furhatos.nlu.common.Number>(
                "Lütfen bana hasta numarasını tekrar en iyi şekilde söyler misiniz 5 + ${furhat.voice.pause("1000ms") }, + 6 ${furhat.voice.pause("1000ms") } ve bu şekilde devam eder",
                timeout = 20000,
                endSil = 5000
            )
        } else {
            // Standardfrage für andere Sprachen (Deutsch in diesem Fall)
            furhat.askFor<furhatos.nlu.common.Number>(
                "Könnten sie mir bitte nochmal die Patientenummer sagen am besten so 5 + ${furhat.voice.pause("1000ms") }, + 6 ${furhat.voice.pause("1000ms") } und so weiter",
                timeout = 20000,
                endSil = 5000
            )
        }

        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user= current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
        onResponse<FrageWiederholen> {
            reentry()
        }
    }
}
 */


/*fun qrCodeScan13(benutzer: User) {
    val imageInput: BufferedImage? = captureImageFromSocket1()

        val luminanceSource = RGBLuminanceSource(imageInput.width, imageInput.height, getPixels2(imageInput))
        val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

        val reader = MultiFormatReader()
        val result = reader.decode(binaryBitmap)

        println("QR Code Text: ${result.text}")
        val barcodeText: String = result.text
        benutzer.put("QR Code Text", barcodeText) // Speichert den erkannten QR-Code-Text im Benutzerobjekt

}

 */
fun containsQRCode(image: BufferedImage): Boolean {
    val luminanceSource = RGBLuminanceSource(image.width, image.height, getPixels2(image))
    val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

    val reader = MultiFormatReader()

    return try {
        // Versuche, einen QR-Code im Bild zu erkennen
        reader.decode(binaryBitmap)
        // Wenn kein Fehler aufgetreten ist, gebe true zurück
        true
    } catch (e: Exception) {
        // Wenn ein Fehler aufgetreten ist (kein QR-Code gefunden), gebe false zurück
        false
    }
}

fun getPixels2(image: BufferedImage): IntArray {
    val width = image.width
    val height = image.height
    val pixels = IntArray(width * height)
    image.getRGB(0, 0, width, height, pixels, 0, width)
    return pixels
}



fun captureImageFromSocket(benutzer: User) {
    val context = ZContext()
    val subscriber = context.createSocket(SocketType.SUB)

    // Verbinde den Socket mit dem ZMQ.SUB-Socket
    subscriber.connect("tcp://10.198.3.150:3000")

    // Setze den Filter auf leeren String, um alle Nachrichten zu empfangen
    subscriber.subscribe("".toByteArray())

    var imageInput: BufferedImage?

    // Empfange eine Nachricht
    val message = subscriber.recv(0)

    // Schließe den Socket und den Kontext
    subscriber.close()
    context.close()

    // Überprüfe, ob eine Nachricht empfangen wurde
    if (message != null) {
        // Verarbeite das empfangene Bild
        val image = ImageIO.read(ByteArrayInputStream(message))

        // Speichere das empfangene Bild als latestImage
        imageInput = image

        if (!containsQRCode(imageInput)) {
            // Wenn kein Bild übergeben wurde oder das Bild keinen QR-Code enthält,
            // rufe die Funktion rekursiv neu auf
            Thread.sleep(2000) // Füge eine Verzögerung von einer halben Sekunde ein

            captureImageFromSocket(benutzer)
        } else {
            val luminanceSource = RGBLuminanceSource(imageInput.width, imageInput.height, getPixels2(imageInput))
            val binaryBitmap = BinaryBitmap(HybridBinarizer(luminanceSource))

            val reader = MultiFormatReader()
            val result = reader.decode(binaryBitmap)

            println("QR Code Text: ${result.text}")
            val barcodeText: String = result.text
            benutzer.put("QR Code Text", barcodeText) // Speichert den erkannten QR-Code-Text im Benutzerobjekt
        }
    } else {
        // Keine Nachricht empfangen - handle den Fall entsprechend
        // Zum Beispiel eine Fehlermeldung ausgeben oder weitere Maßnahmen ergreifen
    }
}





/*
fun GetDigitsTaxifahrer(Benutzer: User, furhat: Furhat, field: String) {
//Unterscheidet sich nur in der Art und Weise wie Furhat nach der Patientennummer fragt.
    furhat.askFor<furhatos.nlu.common.Number>(
        "Was ist die Patientennummer ihres Kunden beziehungsweise Angehörigen", timeout = 20000, endSil = 5000)
    {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user= current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
        onNoResponse {
            //Antwortet der Nutzer gar nicht, so startet Furhat die Frage (Funktion) nochmals.
            reentry()
        }
        onResponse<FrageWiederholen> {
            //Fordert der Nutzer Furhat auf die Frage noch einmal zu wiederholen,
            // so wiederholt auch durch diesen trigger Furhat die Frage nochmal.
            reentry()
        }
    }
}

fun GetDigitsTaxifahrernochmal(Benutzer: User, furhat: Furhat, field: String) {
//Die GetDigitsNochmal-Funktionen unterscheiden sich nur in der Frage nach der Patientennummer
    furhat.askFor<furhatos.nlu.common.Number>("Wie dann?", timeout = 20000, endSil = 5000) {
        onResponse<furhatos.nlu.common.Number> {
            furhat.attend(user= current)
            val x: String = it.text.toString().replace(" ".toRegex(), "")
            val resultx: String = x.filter { it.isDigit() }
            Benutzer.put("Patientennummer", resultx)
            goto(ValidierungNummerKunde)
        }
    }
}



 */




/*fun connecttonetwork(
    username: String,
    password: String,
    networkDrivePath: String,
    furhat: Furhat
)
{
    val config = PropertyConfiguration(System.getProperties())
    val context = BaseContext(config)
    val credentials = NtlmPasswordAuthenticator(username, password)

    val cifsContext: CIFSContext = context.withCredentials(credentials)

    val file = SmbFile(networkDrivePath, cifsContext)

    // Hier kannst du weitere Operationen auf dem Netzwerklaufwerk ausführen
    // Zum Beispiel: Dateien lesen, schreiben, löschen usw.

    // Beispiel: Dateien im Netzwerklaufwerk auflisten
    if (file.isDirectory) {
        val files = file.listFiles()
        files.forEach { println(it.name) }
    }
}

 */



fun getDirection (directoryPath: String, username: String, password: String): SmbFile? {
    //von hier...
    val config = PropertyConfiguration(System.getProperties())
    val context = BaseContext(config)
    val credentials = NtlmPasswordAuthenticator(username, password)

    val cifsContext: CIFSContext = context.withCredentials(credentials)

    val ordner = SmbFile(directoryPath, cifsContext)
    //bis hier...nur Anmeldung im Netzlaufwerk

    //val inputStream = ordner.inputStream


    /* val files = ordner.listFiles { file -> file.name.endsWith(".png") }
    if (files.isNullOrEmpty()) {
        return null
    }

    val latestFile = files.maxBy { file -> file.lastModified() }
    */

    return ordner
}