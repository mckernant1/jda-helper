package com.mckernant1.jda

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class CommandTest {

    @Test
    fun testToCommandData() {
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

    @Test
    fun testFromSlashCommand() {
        val slash = mock(SlashCommandInteractionEvent::class.java)
        val mapping = mock(OptionMapping::class.java)

        `when`(mapping.asString).thenReturn("testResult")
        `when`(slash.getOption("test")).thenReturn(mapping)

        val testCommand = slash.toCommandData()
        assertEquals("testResult", testCommand.testOption)
    }


}
