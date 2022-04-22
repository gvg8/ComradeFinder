# ComradeFinder
The backend to the ComradeFinderApp

You need Intellij Ultimate Edition.

The community edition doesn't have the built-in Maven functionality that this project depends on

1. Clone from the 'main' branch or download a zip-file (whichever you prefer)

2. Open the project in Intellij Ultimate Edition

4. Fetch dependencies from Maven:
* On the right corner you should see the Maven tab.
* Open it and press the "refresh" symbol (Reload All Maven Projects)
* Wait a while since it may take time to fetch all the dependencies (You should see a progress bar at the bottom of the screen)

5. Check if Java Classes display correctly and fix any issues that may arise
* The Java Classes icons should be blue circles with the letter C in it.
* Open java classes to see if there are any problems (some words may be red in this case)
* If there are any problems, it may be because some Maven dependencies are missing (make sure to do step 4.)
* You may be missing the Running Configuration in which case,
  * Create a new Spring Boot configuration with the following settings (they probably will show up on default):
  * Run on: Local Machine
  * Java 11 (other versions probably work too, but this is what I used)
  * is.hi.comradefinder.ComradeFinderApplication
* If not, hover your mouse over the red words and see what recommended fixes show up.
* As the possible problems may wary greatly, I leave it as an exercise for the reader to sort out all of these problems themselves.

6. Build project
7. Run Project (wait until you get the "Started ComradeFinderApplication in x seconds..." message in the Run console)

After this, you should be able to run the ComradeFinderApp as intended.

If you need to reset files in the Database, you can scroll up in the Project overview, open the database folder and delete dbfile.mv.db. You may need to rerun the project to recreate the database.

If for some reason the localhost address isn't 10.0.2.2 or ComradeFinder's TomCat didn't start on port 8080, you may need to change the code in ComradeFinder (go to NetworkManager. At the top of that file you should find BASE_URL. Change that to whatever ComradeFinder is running on)
