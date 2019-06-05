import jdk.nashorn.internal.scripts.JO;

import java.awt.*;        // Uses AWT's Layout Managers
import java.awt.event.*;  // Uses AWT's Event Handlers
import javax.swing.*;     // Uses Swing's Container/Components
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class Sudoku extends JFrame {

    /*Fields and variables here*/
    public static final int GRID_SIZE = 9;    // Size of the board
    public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int CANVAS_WIDTH  = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
    // Board width/height in pixels
    public static final Color OPEN_CELL_BGCOLOR = Color.WHITE;
    public static final Color CLOSED_CELL_BGCOLOR = Color.WHITE;
    public static final Color CLOSED_CELL_TEXT = Color.BLACK;
    public static final Font FONT_NUMBERS = new Font("Times New Roman", Font.PLAIN, 22);
    public static final Font FONT_TEXT_ENTRY = new Font("Times New Roman", Font.BOLD, 22);
    public static final Color TEXT_ENTRY_COLOR = Color.BLUE;

    // Thickness for the borders of the 3x3 grids
    public static final int BORDER_THICKNESS = 4;

    // The game board composes of 9x9 JTextFields,
    // each containing String "1" to "9", or empty String
    private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];

    private PuzzleGenerator generator = new PuzzleGenerator();


    // Puzzle to be solved and the mask (which can be used to control the
    //  difficulty level).
    // Hardcoded here. Extra credit for automatic puzzle generation
    //  with various difficulty levels.
    // The board with the 0's

    // The original puzzle with all numbers
    private int[][] board = generator.nextBoard();
    // The raw solution. Because of referencing, need to save this as a copy for checking later.
    private int[][] rawSolution = saveSolution(board);

    // The board with the 0's to decide what values are going ot be hidden.
    public int[][] puzzle = generator.getBoard(35);

    // Puzzle should have 0's for any 0 make it invisible.
    public boolean[][] masks = makeMasks(puzzle);


    /**
     * Constructor to setup the game and the UI Components
     */
    public Sudoku() {
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));  // 9x9 GridLayout


        // Construct 9x9 JTextFields and add to the content-pane
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {

                tfCells[row][col] = new JTextField(); // Allocate element of array
                tfCells[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean incorrect = false;  // Will only be true if a value doesn't match sol.
                        int wrongRow = 0, wrongCol = 0; // Save the cell location of the incorrect entry.
                        String wrongValue = ""; // Save the value as a string to say what value is wrong.
                        boolean finished = true;    // If this is false, only the 'keep going' message should be shown.

                        for (int i = 0; i < GRID_SIZE; i++) {
                            for (int j = 0; j < GRID_SIZE; j++) {
                                if (masks[i][j]) {
                                    String entry = tfCells[i][j].getText();

                                    if(!entry.equals("")) {

                                        if (rawSolution[i][j] != Integer.parseInt(entry)) {
                                            incorrect = true;
                                            wrongRow = i;
                                            wrongCol = j;
                                            wrongValue += entry;
                                        }
                                    } else {
                                        finished = false;
                                    }
                                }
                            }
                        }


                        if (incorrect) {
                            // Construct an error message
                            String msg = "Cell at row " + (wrongRow + 1) + " column " + (wrongCol + 1) + " has incorrect value of " + wrongValue;
                            msg += ".\nCheck your answers and try again.";
                            JOptionPane.showMessageDialog(getContentPane(), msg);
                            return;
                        } else if (!finished) {
                            JOptionPane.showMessageDialog(getContentPane(), "So far so good!\nKeep going!");
                            return;
                        }

                        // If we never left method, then the solution is right.
                        JOptionPane.showMessageDialog(getContentPane(), "Correct!\nYou Win!");
                        System.exit(0);
                    }
                });


                // Set up the text fields such that it only can receive one digit.
                AbstractDocument d = (AbstractDocument) tfCells[row][col].getDocument();
                d.setDocumentFilter(new DocumentFilter(){
                    int max = 1;

                    @Override
                    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        int documentLength = fb.getDocument().getLength();

                        // To prevent the user from entering non-numeric characters,
                        // we'll use the try-catch when we attempt to parse the text.
                        try {
                            Integer.parseInt(text); // Determine if the text is an int.

                            if (documentLength - length + text.length() <= max) {
                                super.replace(fb, offset, length, text.toUpperCase(), attrs);
                            }

                        } catch(NumberFormatException e) {
                            System.out.println("Not an integer");
                            super.replace(fb, offset, length, "", attrs);
                        }

                    }
                });



                cp.add(tfCells[row][col]);            // ContentPane adds JTextField
                // Make a more defined border for the text fields.
                tfCells[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, false));

                // We're on a third row, so the text fields needs a thicker border on the top region.
                if(row % SUBGRID_SIZE == 0) {
                    padTopBorder(tfCells[row][col]);
                }

                // Now if we're on the third column, the thicker border goes on the left hand side.
                if (col % 3 == 0) {
                    // Thicker border for top and left on blocks that are also on the third rows
                    padTopAndLeftBorder(tfCells[row][col]);

                    // If the block is not on the next third row, we only want to have thickness on its left.
                    if (row % SUBGRID_SIZE != 0) {
                        padLeftBorder(tfCells[row][col]);
                    }

                }

                // Add border to the right of cells that are in the last column
                if (col == GRID_SIZE - 1) {
                    padTopAndRightBorder(tfCells[row][col]);

                    if (row % SUBGRID_SIZE != 0) {
                        padRightBorder(tfCells[row][col]);
                    }
                }

                // Now pad the thickness for the outer right of the last column.
                // and pad the thickness for the bottom row.
                if (row == GRID_SIZE - 1) {
                    padBottomLeftBorder(tfCells[row][col]);

                    if (col % SUBGRID_SIZE != 0) {
                        padBottomBorder(tfCells[row][col]);
                    }
                }

                if (row == 8 && col == 8) {
                    padBottomRightCornerCell(tfCells[row][col]);
                }



                if (masks[row][col]) {  // If its an empty cell.
                    tfCells[row][col].setText("");     // set to empty string
                    tfCells[row][col].setEditable(true);    // User can write in text field.
                    tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR); // White background.
                    tfCells[row][col].setFont(FONT_TEXT_ENTRY);
                    tfCells[row][col].setForeground(TEXT_ENTRY_COLOR);  // Set the user text to blue.


                } else {
                    tfCells[row][col].setText(puzzle[row][col] + "");
                    tfCells[row][col].setEditable(false);
                    tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
                    tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
                    tfCells[row][col].setFont(FONT_NUMBERS);
                }
                // Beautify all the cells by centering the text in each block.
                tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);


            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        pack();

        //I'd prefer it if the board appears in the center of the user screen :)
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int upperLeftCornerX = (screenSize.width - getWidth()) / 2;
        int upperLeftCornerY = (screenSize.height - getHeight()) / 2;
        setLocation(upperLeftCornerX, upperLeftCornerY);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
        setTitle("Sudoku");
        setVisible(true);
    }


    // Methods to help make the board look nicer with the thicker matte borders in the apporpriate places.
    public static void padBottomBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(1, 1, BORDER_THICKNESS, 1, Color.BLACK));
    }

    public static void padBottomLeftBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(1,BORDER_THICKNESS, BORDER_THICKNESS, 1, Color.BLACK));
    }

    public static void padBottomRightCornerCell(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(1, 1, BORDER_THICKNESS, BORDER_THICKNESS, Color.BLACK));
    }

    public static void padTopAndLeftBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(BORDER_THICKNESS, BORDER_THICKNESS, 1, 1, Color.BLACK));
    }

    public static void padTopBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(BORDER_THICKNESS, 1, 1, 1, Color.BLACK));
    }

    public static void padTopAndRightBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(BORDER_THICKNESS, 1, 1, BORDER_THICKNESS, Color.BLACK));
    }

    public static void padRightBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(1, 1, 1, BORDER_THICKNESS, Color.BLACK));
    }

    public static void padLeftBorder(JTextField cell) {
        cell.setBorder(BorderFactory.createMatteBorder(1, BORDER_THICKNESS, 1, 1, Color.BLACK));
    }



    // Wherever there are 0's make those false.
    public static boolean[][] makeMasks(int[][] puzzle) {
        boolean[][] masks = new boolean[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {

                if (puzzle[i][j] == 0) {
                    masks[i][j] = true;
                } else {
                    masks[i][j] = false;
                }
            }
        }
        return masks;
    }


    public void print() {
        for(int i=0;i<9;i++) {
            for(int j=0;j<9;j++)
                System.out.print(rawSolution[i][j] + "  ");
            System.out.println();
        }
        System.out.println();
    }

    public static int[][] saveSolution(int[][] board) {
        int[][] solution = new int[9][9];

        // Init all the values from the board we got to a new array so we don't overwrite
        // any of the values with 0's. I need a new board to compare to that has the solution.
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solution[i][j] = board[i][j];
            }
        }

        return solution;
    }


    public static void main(String args[]) {
        Sudoku game = new Sudoku();
        game.setVisible(true);

        // Print out the solution in console for testing and visual reasons.
        game.print();
    }

}
