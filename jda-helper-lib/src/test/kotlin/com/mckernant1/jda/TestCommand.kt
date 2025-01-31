package com.mckernant1.jda

import com.mckernant1.jda.annotations.Command
import com.mckernant1.jda.annotations.Option
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

@Command(TestCommand.NAME, TestCommand.DESCRIPTION)
data class TestCommand(
    @Option(
        OptionType.STRING,
        "test",
        "test input description",
        true
    )
    val testOption: String,
) {

    companion object {
        const val NAME = "test"
        const val DESCRIPTION = "test description"

        val options: List<OptionData> = listOf(
            OptionData(OptionType.STRING, "test", "test input description", true)
        )
    }
}
