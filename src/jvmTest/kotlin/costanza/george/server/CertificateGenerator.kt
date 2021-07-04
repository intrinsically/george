package costanza.george.server

import io.ktor.network.tls.certificates.generateCertificate
import java.io.File
import kotlin.test.Test

/**
 * used to generate a self signed certificate for testing https
 */
class CertificateGeneratorTest {
    @Test
    fun testCreateCertificate() {
        val jksFile = File("src/jvmMain/resources/temporary.jks").apply {
            parentFile.mkdirs()
        }
        if (!jksFile.exists()) {
            generateCertificate(jksFile) // Generates the certificate
        }
    }
}