# Sudoku

I am a lifelong fan of the puzzle game Sudoku. Although there are many online web applications where you can play the game,
I wanted to take on the task of making my own Java Application with a Sudoku GUI that uses Java Swing.

![Image description](link-to-image)

# How It Works

The user starts the program and then sees a 9x9 grid show up on the screen with 35 empty cells and the rest filled in.
I had to find a program to produce a valid sudoku board and I managed to come across a decent one. Credit is fully given 
to the author in the source code of the java file. I also needed a base to start making the GUI. Some of that code was
borrowed from an outside source as well and once again credit was given. My modifications include adding the event listeners,
decorating the GUI with matte borders to show the 3x3 grids within, and a sudoku checker once the users hits enter.

The user can start entering text input in the empty cells. At any point, the user can hit enter. If the user has a value in 
a cell that is incorrect with the raw solution saved by the program, then they are prompted with a message that says what
cell has the incorrect value. If the user has text in the cells, but the values are right, the program tells the user
everything appears correct so far, however when it encounters a cell that doesn't have any text in it, it encourages the user
to keep going.

The program will not let the user add any input that is not numeric and no more than 1 digit. 

# Difficulty of the puzzle

At the moment, the difficulty is only accessible through the source code and will only show 35 cells to be filled in. It is with hope in the future, the program will randomize difficulty. It is also an option to add a way to have the user determine the level of difficulty. 
