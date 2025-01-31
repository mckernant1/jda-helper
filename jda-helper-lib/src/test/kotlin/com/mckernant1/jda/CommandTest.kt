package com.mckernant1.jda

import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CommandTest {

    @Test
    fun testAnnotation() {
        val d = TestCommand.toCommandData() as CommandDataImpl

        assertEquals(d.name, TestCommand.NAME)
        assertEquals(d.description, TestCommand.DESCRIPTION)

        for (data in d.options) {
            val correspondingData = TestCommand.options
                .find { it.name == data.name }!!

            assertEquals(correspondingData.name, data.name)
            assertEquals(correspondingData.description, data.description)
            assertEquals(correspondingData.type, data.type)
            assertEquals(correspondingData.isRequired, data.isRequired)
        }


    }

}
