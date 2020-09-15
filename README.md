# scala-rec-html
Prototype implementation of Recursive HTML in Scala

## Getting started

The easiest way to test Scala-Rec-HTML is by using [IntelliJ](https://www.jetbrains.com/idea/) with the Scala plugin.
You also need [npm](https://www.npmjs.com).

> # **Important:** without `npm` none of the following steps will work.

### Get the source code 

```
git pull https://github.com/tzbob/scala-rec-html.git
```

Now:
- Open IntelliJ
- "Open Project" 
- Select scala-rec-html/build.sbt (make sure to select the file, it's the "project" file)
- Wait for IntelliJ to index the project
- Open SBT Shell (CTRL+SHIFT+S)
- type `fastOptJS::webpack`

Every time `fastOptJS::webpack` is executed, the JavaScript program is compiled. It will compile the current application.
As of this commit, the default `main` function is defined in  `src/main/scala/rec/recordtyped/ViewPerson.scala`.
To test a different application the current application has to be changed from `object` to `class` and the target has to be changed from `class` to `object`.
Other applications include:
- `src/main/scala/rec/recordtyped/ViewPerson.scala` >> index.html
- `src/main/scala/rec/typed/ViewPerson.scala` >> index.html
- `src/main/scala/rec/ViewPerson.scala` >> index.html
- `src/main/scala/rec/TodoMVC.scala` >> todomvc.html

After the application is compiled, use the IntelliJ web-server to open the web-page:
- (In IntelliJ): Open `src/main/resources/index.html` (or `todomvc.html` when testing the TodoMVC application)
- Hover to the top right corner in the text edtior and press the browser of your choice.

## Structure

- `rec.typed` contains all information on the type-level approach
- `rec.recordtyped` contains all code regarding the record-typing approach
- `rec` contains the core code
