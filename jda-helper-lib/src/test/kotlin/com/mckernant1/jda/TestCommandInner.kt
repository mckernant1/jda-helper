package com.mckernant1.jda

import com.mckernant1.jda.annotations.Command
import com.mckernant1.jda.annotations.Option
import net.dv8tion.jda.api.interactions.commands.OptionType

class TestCommandInner {


    companion object {
        @Command("test-inner", "this is an inner test")
        data class TestCommandCompanionInner(
            @Option(OptionType.STRING, "test", "test input description", true)
            val testString: String,
        ) {
            companion object
        }
    }

}
