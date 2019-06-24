# Sudoku

I am a lifelong fan of the puzzle game Sudoku. Although there are many online web applications where you can play the game,
I wanted to take on the task of making my own Java Application with a Sudoku GUI that uses Java Swing. 

![Image description](https://github.com/thunderd568/Sudoku/blob/master/Screen%20Shot%202019-06-05%20at%204.19.04%20PM.jpg)

# How It Works

The user starts the program and sees a message asking what level of difficulty they would like the puzzle to be. 
![Image description](https://github.com/thunderd568/Sudoku/blob/master/firstMessage.jpg)

More on that is described below.
I had to find a program to produce a valid sudoku board and I managed to come across a decent one. Credit is fully given 
to the author in the source code of the PuzzleGenerator.java file. I also needed a base to start making the GUI. Some of that
code was borrowed from an outside source as well and once again credit was given. My modifications include adding the event
listeners, decorating the GUI with matte borders to show the 3x3 grids within, and a sudoku checker once the users hits enter.

The user can start entering text input in the empty cells. At any point, the user can hit enter. If the user has a value in 
a cell that is incorrect when compared to the raw solution saved by the program, then they are prompted with a message that says what cell has the incorrect value. 

![Image description](https://github.com/thunderd568/Sudoku/blob/master/invalidEntry.jpg)

If the user has text in one or more cells but they still have blank ones, the user can hit enter and the program will check
to see if the entries entered so far are correct and then encourage the user to finish the game.

![Image description](https://github.com/thunderd568/Sudoku/blob/master/keepGoing.jpg)

The program will not let the user add any input that is not numeric and more than 1 digit. 

Once the sudoku board is complete and determined to be correct, the program prompts the user they have won and terminates.
![Image description](https://github.com/thunderd568/Sudoku/blob/master/correct.jpg)

# Difficulty of the puzzle

When the user selects the difficulty, the following criteria is applied for each decision
#Easy
A random number between 31-35 is generated. This number is the number of blank squares in the grid.
#Medium
A random number between 36-40 is generated to determine how many blank squares will be in the grid.
#Hard
Same as above however the random number is between 41-45. All numbers in the aformentioned ranges are inclusives on both bounds.
