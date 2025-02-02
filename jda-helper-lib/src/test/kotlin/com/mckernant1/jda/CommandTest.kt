package com.mckernant1.jda

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
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

        val testMapping = mock(OptionMapping::class.java)
        `when`(testMapping.asString).thenReturn("testResult")
        `when`(slash.getOption("test")).thenReturn(testMapping)

        val testOptionalMapping = mock(OptionMapping::class.java)
        `when`(testOptionalMapping.asString).thenReturn("optionalDefault")
        `when`(slash.getOption("test-optional-default")).thenReturn(testOptionalMapping)

        val testNumberMapping = mock(OptionMapping::class.java)
        `when`(testNumberMapping.asDouble).thenReturn(42.0)
        `when`(slash.getOption("test-number-double")).thenReturn(testNumberMapping)

        val testBoolMapping = mock(OptionMapping::class.java)
        `when`(testBoolMapping.asBoolean).thenReturn(true)
        `when`(slash.getOption("test-bool")).thenReturn(testBoolMapping)

        val testIntMapping = mock(OptionMapping::class.java)
        `when`(testIntMapping.asInt).thenReturn(7)
        `when`(slash.getOption("test-int")).thenReturn(testIntMapping)

        val testUserMapping = mock(OptionMapping::class.java)
        `when`(testUserMapping.asUser).thenReturn(mock())
        `when`(slash.getOption("test-user")).thenReturn(testUserMapping)

        val testChannelMapping = mock(OptionMapping::class.java)
        `when`(testChannelMapping.asChannel).thenReturn(mock())
        `when`(slash.getOption("test-channel")).thenReturn(testChannelMapping)

        val testRoleMapping = mock(OptionMapping::class.java)
        `when`(testRoleMapping.asRole).thenReturn(mock())
        `when`(slash.getOption("test-role")).thenReturn(testRoleMapping)

        val testMentionableMapping = mock(OptionMapping::class.java)
        `when`(testMentionableMapping.asMentionable).thenReturn(mock())
        `when`(slash.getOption("test-mentionable")).thenReturn(testMentionableMapping)

        val testCommand = slash.toTestCommand()

        assertEquals("testResult", testCommand.testString)
        assertEquals("optionalDefault", testCommand.testStringOptional)
        assertNull(testCommand.testStringNullable)  // Expecting null
        assertEquals(42.0, testCommand.testNumber, 0.001)
        assertEquals(true, testCommand.testBool)
        assertEquals(7, testCommand.testInt)
        assertNotNull(testCommand.testUser)
        assertNotNull(testCommand.testChannel)
        assertNotNull(testCommand.testRole)
        assertNotNull(testCommand.testMentionable)
    }

}
