# ZoomOpener

The ZoomOpener was designed for opening my Zoom calls for my classes during the pandemic. 
It reads in a CSV file (see Fall2020.csv for an example of the proper formating), then asks user for the name (or nickname) of the class you want to open
After, it opens Google Chrome, then types out the URL letter by letter using my type funciton, then types enter and a password, if there is one. 
The type function uses the Robot class's .keyPress(). More information on type can be found on my RobotTypeClass git page
The AutoZoomOpener was designed to automatically open any zoom call five minutes before it was supposed to open.


Though it was designed with a college schedule in mind, the concept can be abstracted to open any link, which is what I plan on doing next, along with adding a way to add/update/change links and names.
