Dungeon Quest
======

![screenshot](http://i.imgur.com/iJugShH.png)

Dungeon Quest is 2D topdown view game written in Java. You can explore dungeons with the arrow keys and collect gold, but don't get killed! For an extra challenge try getting rid of the rat by attacking it (press the space bar to do so). All of the art, music and code (apart from the java sound classes) was made by me.

Now for some technical stuff: As for the program structure, there's a couple of bigger classes which include the Warrior class (has the main loop, key listeners etc.), a Logic class and a Map class. They then call on smaller ones like the Character and Tile classes. Maps are stored as a text file which is loaded when needed, more specifically when the player walks out of bounds of the screen into a new area. They consist of a 2D array of tile objects which have a number of different attributes. For example here's a couple of lines from a .map file:

    @ 16 3 skeleton
    2 1 images/map/soil.png false false false
    2 2 images/map/wall_corner_nw.png false false false
    2 3 images/map/rock_pit.png true true false
    
For now the enemy rat AI is pretty simple, no path-finding or anything like that. It just simply turns towards the character's direction when it hits a wall. I'll probably add something more sophisticated later on.

For sound classes I use AePlayWave which is a free Java sound library available online. Wav files are pretty big, so I probably need to find something better.

Oh, I've also made a map editor in which you can save .map files in which you add or remove tiles by right/left clicking. I'm thinking of making the dungeons randomly generated, so when you go out of bounds of the screen you always enter a new area. I'll probably add more enemies, traps and new weapons as well.

Click [here](http://www.students.tut.fi/~podsechi/dungeon_quest.zip) to download the executable jar file.
