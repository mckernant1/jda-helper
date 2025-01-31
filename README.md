# JDA Helper

This package includes helper classes for jda java library


### Annotations
Annotations allow you to annotate a data class and create a CommandData object from the class definition

NOTE: Classes MUST have a companion object declared in order for this to generate the 'static' method for commandData generation

```kotlin
@Command("test", "test description")
data class TestCommand(
    @Option(
        OptionType.STRING,
        "test",
        "test input description",
        true
    )
    val testOption: String,
) {
    companion object {}
}
```

You can use the 'static' method to grab the commandData from the data class
```kotlin
val c = TestCommand.createCommandData()
```

It will also generate a function to turn slash command events into your annotated class
```kotlin
slash.toCommandData()
```
