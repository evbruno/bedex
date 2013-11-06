## bedex

### A Scala + JavaFX application/spike that helps you manage your team's work log.

Configuration needed to run:

+ JDK > 1.7.06
+ Scala 2.10.2
+ sbt 0.12.4

**Running**

	$ export JAVA_HOME=/Library/Java/JavaVirtualMachines/1.7.0.06.jdk/Contents/Home
	$ sbt run
	
**Datasource configuration**

The datasource configuration will look for instructions in this order:

1. Parameters passed to the main method

2. A file named **db.properties** (see a sample at [src/main/resources](Bedex-sbt/src/main/resources/db.properties-sample))

3. A fallback option, that will load some useless data in memory

If you choose to run from command line:

	$ sbt "run-main bedex.BedexApp --db.url=jdbc:h2:~/test_h2 --db.user=sa"

...or just:

	$ sbt
	> run --db.url=jdbc:h2:~/test_h2 --db.user=sa
	
*see the [spikes](Bedex-sbt/src/test/scala/bedex/biz/jdbc) for some useful stuff.*


**Assembling (one-fat-ass-jar) and running the "executable"**

	$ sbt assembly
	$ java -jar target/scala-2.10/bedex-assembly-{VERSION}.jar

**Some cool 3rd-party libraries**

+ ScalaFX (http://code.google.com/p/scalafx)
+ JavaFX Dialogs (https://github.com/marcojakob/javafx-ui-sandbox)

### TODO

* vacations form
* holidays form
* user / team form
* ~~show work log~~
  * caching
* missappointment filters: 
  * by date
  * ~~by level~~
  * ~~by team~~
  * ~~by user name~~
* missappointment form redesign
* styling
* miss appointments pre-notification
* ProGuarding
* ~~Webstart deployment~~
* Splash screen
* BG loading stuff (add anysome responsiveness)
 
