/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package edu.austral.ingsis.starships

import javafx.application.Application
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test

class AppTest {
    @Test
    fun appHasAGreeting() {
        val classUnderTest = Starships()
        assertNotNull(classUnderTest.init(), "app should have a greeting")
    }
}
