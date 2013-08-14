## bedex

### A Scala + JavaFX application/spike that helps you manage your team's work log.

Configuration needed to run:

+ JDK > 1.7.06
+ Scala 2.10.2
+ sbt 0.12.4

Running:

	$ export JAVA_HOME=/Library/Java/JavaVirtualMachines/1.7.0.06.jdk/Contents/Home
	$ sbt run

Assembling (one-fat-ass-jar) and running the "executable":

	$ sbt assembly
	$ java -jar target/scala-2.10/bedex-assembly-{VERSION}.jar

Some cool 3rd-party libraries:

+ ScalaFX (http://code.google.com/p/scalafx)
+ JavaFX Dialogs (https://github.com/marcojakob/javafx-ui-sandbox)

### TODO

+ vacations
+ show work log (last 7 days)
+ better filters (by date, by lvl, etc..)
+ styling
+ miss appointments pre-notification
+ ProGuarding

