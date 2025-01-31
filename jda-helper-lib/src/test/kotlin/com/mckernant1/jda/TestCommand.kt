package com.mckernant1.jda

import com.mckernant1.jda.annotations.Command
import com.mckernant1.jda.annotations.Option
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

@Command(TestCommand.NAME, TestCommand.DESCRIPTION)
data class TestCommand(
    @Option(OptionType.STRING, "test", "test input description", true)
    val testString: String,

    @Option(OptionType.STRING, "test-optional-default", "test optional input description")
    val testStringOptional: String = "default",

    @Option(OptionType.STRING, "test-optional-nullable", "test nullable input description")
    val testStringNullable: String?,

    @Option(OptionType.NUMBER, "test-number-double", "test-number-desc")
    val testNumber: Double,

    @Option(OptionType.BOOLEAN, "test-bool", "test bool input description")
    val testBool: Boolean,

    @Option(OptionType.INTEGER, "test-int", "test integer input description")
    val testInt: Int,

    @Option(OptionType.USER, "test-user", "test user input description")
    val testUser: User,

    @Option(OptionType.CHANNEL, "test-channel", "test channel input description")
    val testChannel: GuildChannel,

    @Option(OptionType.ROLE, "test-role", "test role input description")
    val testRole: Role,

    @Option(OptionType.MENTIONABLE, "test-mentionable", "test mentionable input description")
    val testMentionable: IMentionable
) {
    companion object {
        const val NAME = "test"
        const val DESCRIPTION = "test description"

        val options: List<OptionData> = listOf(
            OptionData(OptionType.STRING, "test", "test input description", true),
            OptionData(OptionType.STRING, "test-optional-default", "test optional input description", false),
            OptionData(OptionType.STRING, "test-optional-nullable", "test nullable input description", false),
            OptionData(OptionType.NUMBER, "test-number-double", "test-number-desc", false),
            OptionData(OptionType.BOOLEAN, "test-bool", "test bool input description", false),
            OptionData(OptionType.INTEGER, "test-int", "test integer input description", false),
            OptionData(OptionType.USER, "test-user", "test user input description", false),
            OptionData(OptionType.CHANNEL, "test-channel", "test channel input description", false),
            OptionData(OptionType.ROLE, "test-role", "test role input description", false),
            OptionData(OptionType.MENTIONABLE, "test-mentionable", "test mentionable input description", false)
        )
    }
}
