# TarotRL

A very in-progress procedurally generated Roguelike, developed in Java using [Ziron](https://github.com/Hexworks/zircon) to render ASCII tile map graphics.

## Build

To run and build and run with Maven:

```
mvn clean install
java -jar target/TarotRL-1.0-SNAPSHOT.jar
```

_Note:  The current build may fail as the Zircon release artifact `2019.0.16-PREVIEW` is apparently no longer hosted on jitpack.io.  As the keyboard input API has since changed, I'll need to update the code to build with the latest available artifact._
