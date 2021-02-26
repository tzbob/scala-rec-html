# scala-rec-html
Prototype implementation of Recursive HTML in Scala

## Getting started

To get started with the PolyRPC runtime you need the [scala build tool](https://www.scala-sbt.org) and [npm](https://www.npmjs.com).
Given an appropriate build file, SBT will pull down all required libraries and the appropriate Scala version.
NPM is required since there are some client-side libraries that are used by Scala.js dependencies of the runtime.

> # **Important:** without `npm` or `sbt` none of the following steps will work.

### Get the source code 

```
git pull https://github.com/tzbob/scala-rec-html.git
cd scala-rec-html
sbt
```


This starts up the SBT shell and the first time this will take a long time.
It downloads the proper Scala and Scala.JS versions as well as the proper SBT version itself.

In the SBT shell, execute ```fastOptJS::webpack```.
Every time `fastOptJS::webpack` is executed, the JavaScript program is compiled. It will compile the current application.
As of this commit, the default `main` function is defined in  `build.sbt` as the TodoMVC example.
To test a different application the current application has to be changed in `build.sbt` by commenting ```mainClass in Compile ...``` and uncommenting the example you'd like.

After the application is compiled, open ```src/main/resources/todomvc.html``` in your browser. 
When running any examples other than todomvc, run ```src/main/resources/index.html```.
