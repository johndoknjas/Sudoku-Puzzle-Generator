// VERSION 3:  Version 3 allows the user to choose which square to enter a number for.  This is quicker, rather than having to
// go through each unknown square and skip until reaching the chosen square.

import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;

class Main
{
    private static int grid [][] = new int [9][9];

    private static Scanner scan = new Scanner (System.in);

    private static double difficulty = 0;

    private static int num_correct = 0; // Stores how many problems the user got correct.

    private static int num_attempted = 0; // Stores how many problems the user attempted in total.

    public static void main(String[] args)
    {
        boolean play_again = true; // Holds whether the user wants to play again or not.

        while (play_again) // meaning while play_again == true, since a boolean type can be written this way.
        {
            // Increase the global num_attempted variable:

            num_attempted ++;

            // Reset the grid with random negative numbers, in case the game has already been played:

            fill_negatives();

            // Fill the grid legally:

            fill_grid();

            // Print the grid to the screen, assuming it is valid:

            if (valid_grid() && no_negatives())
            {
                print_grid();
            }

            // Now to get the user to set the difficulty level (which is stored in the global difficulty variable).

            set_difficulty();

            // Now to remove some numbers at random from the grid, replacing them with different negative numbers:

            remove_numbers();

            // Let the user solve the grid:

            user_solve();

            // Tell the user their current percentage of problems they have correct:

            System.out.println ("You currently have a " + ( (double) num_correct / num_attempted * 100) + "% score.");

            // Ask the user if they want to play again:

            System.out.println ("To play again, press the Enter key.");

            if (!(scan.nextLine().equals("")))
            {
                play_again = false;
            }
        }
    }

    public static void user_solve()
    {
        // This method allows the user to solve the grid.

        // I want to make a while loop that allows the user to pick an unknown square to set to a number.  This unknown
        // square is then indicated with a "?".

        // If the user enters a number, the square is set to it.  The user can change his mind and bypass to another unknown square by
        // pressing any other key.

        // If the grid is at any time not valid or has no unknown squares left to solve, the while loop ends.

        boolean correctly_solved = false; // holds whether or not the user correctly solved the grid.

        // WHILE LOOP BEGINS:

        while (!(!valid_grid() || no_negatives()))
        {
            print_grid();

            System.out.println ("Enter the row number of the unknown square you want to enter a number for:");

            int row = scan.nextInt() - 1;

            System.out.println ("Enter the column number of the unknown square you want to enter a number for:");

            int col = scan.nextInt() - 1;

            scan.nextLine();

            if (row > 8 || row < 0 || col > 8 || col < 0)
            {
                System.out.println ("You did not enter a valid row or column number.  Please try again.");
            }

            else // So, user entered a valid row and column number.
            {
                if (grid[row][col] < 0) // The user will get to solve this square, since it is negative / unknown:
                {
                    // Before setting this square to 0, I want to retain its old value in a variable in case I need
                    // to set the square back to it:

                    int old_value = grid[row][col];

                    grid[row][col] = 0; // The print_grid() method knows to print a "?" if a square holds a 0.

                    print_grid();

                    // Now the user can choose to solve the grid or not:

                    System.out.print ("Enter a single digit number for the current unknown square, or press any other key to skip it.");

                    String user_answer = scan.nextLine();

                    // Now to check if "user_answer" is a single digit number:

                    boolean is_number = false;

                    for (int i = 1; i <= 9; i++)
                    {
                        if (user_answer.equals(Integer.toString(i)))
                        {
                            is_number = true;

                            break;
                        }
                    }

                    if (!is_number)
                    {
                        // So, the user did not enter a single digit number, and has decided to skip this square.

                        // I will return this square's old value back to it:

                        grid[row][col] = old_value;

                        // For loop now iterates again.
                    }

                    else // Meaning the user entered a single digit number, in order to give the square a value:
                    {
                        grid[row][col] = Integer.parseInt(user_answer); // changes the String to an Int.
                    }
                }

                else // So, the user picked a square that already has a number in it:
                {
                    System.out.println ("You picked a square that already has a number in it.  Please try again:");
                }

            }
        }

        // WHILE LOOP ENDS.

        // Show the user the grid their completed grid:

        System.out.print("\n");

        print_grid();

        System.out.print("\n");

        // Now to check if the grid is valid, meaning the user solved it correctly:

        if (valid_grid())
        {
            correctly_solved = true;

            num_correct ++;

            System.out.println ("Good job, you correctly solved the Sudoku puzzle!");
        }

        else
        {
            System.out.println ("You did not correctly solve the Sudoku puzzle - you lose.");
        }
    }

    public static void set_difficulty()
    {
        // Get user to select difficulty level:

        System.out.println ("\nSelect a difficulty level.\nEnter 1 for easy, 2 for medium, and 3 for hard.");

        difficulty = scan.nextDouble();

        scan.nextLine(); // This just takes in the Enter character that the user types when entering the Double above.

        // scan.nextDouble() only takes in the number, and the "\n" character is taken by the next scan.nextLine().  So, I have to get rid of it now.

        if (difficulty != 1 && difficulty != 2 && difficulty != 3)
        {
            System.out.println ("You did not enter 1, 2, or 3.  Please try again:");

            set_difficulty();
        }
    }

    public static void remove_numbers()
    {
        // Now to make an int variable that stores how many numbers will get removed from the grid.
        // The value of this variable depends on the difficulty level that the user entered.

        int remove = 0;

        if (difficulty == 1)
        {
            // In easy sudoku puzzles, 40 numbers will be left in the puzzle.  81 - 40 = 41:

            remove = 41;
        }

        else if (difficulty == 2)
        {
            // In medium sudoku puzzles, 30 numbers will be left in the puzzle.  81 - 30 = 51:

            remove = 51;
        }

        else // difficulty = 3:
        {
            // In hard sudoku puzzles, 26 numbers will be left in the puzzle.  81 - 26 = 55:

            remove = 55;
        }

        // Now to remove some numbers from the grid, using a for loop:

        for (int i = 1; i <= remove; i++)
        {
            // Now to pick a square - I need to find its row and column index, randomly:

            int row = (int) (Math.random() * 9);

            int col = (int) (Math.random() * 9);

            // Now to check if I have already removed the number from the chosen square:

            if (grid[row][col] <= 0) // I set all removed squares to negative nums.
            {
                i--;

                // for loop runs again.
            }

            else // meaning I haven't removed the number from the chosen square yet - the selection is valid:
            {
                grid[row][col] = i * -1;
            }
        }

    }

    public static void fill_grid()
    {
        while (!valid_grid() || !no_negatives()) // Loop runs if the grid isn't valid, or if there are negative numbers in the grid.  Both cases require fixes to the grid.
        {
            // First I want to fill the grid with all different negative numbers, because later on I'll be testing to
            // see if any squares contain the same numbers:

            fill_negatives();

            // ALGORITHM:

            int counter = 0; // Counts how many trials the program spends on one block.  If counter reaches 100 for one
            // block, the program goes back and resets the previous block.

            int big_counter = 0; // Counts how many total runs there are through the for loop (not limited to just one block).

            for (int i = 1; i <= 9; i++)
            {
                fill_block(i);

                if (!valid_grid())
                {
                    i--;

                    counter ++;

                    if (counter >= 100)
                    {
                        // Program has had 100 attempts to set the current block.  Maybe it is near impossible to find the
                        // right combo of numbers.  I will get the program to reset the previous block:

                        i--;
                    }
                }

                else // the grid is valid:
                {
                    counter = 0;

                    // The program moves on to the next block in the for loop.
                }

                big_counter ++;

                if (big_counter >= 50) // Program has run too long.  I will break out of this for loop and
                // the big while loop will run again.
                {
                    break;
                }
            }
        }
    }

    public static boolean no_negatives()
    {
        // Returns true if there are no negative numbers in the grid.

        // I will go through the grid with a double for loop:

        for (int row = 0; row < grid.length; row ++)
        {
            for (int col = 0; col < grid[row].length; col ++)
            {
                if (grid[row][col] < 0)
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static void fill_negatives()
    {
        // Method fills the grid with all different negative numbers:

        int num = -1;

        for (int i = 0; i < grid.length; i++)
        {
            for (int x = 0; x < grid[i].length; x++)
            {
                grid[i][x] = num;

                num --;
            }
        }
    }

    public static void print_grid()
    {
        // Method prints the grid to screen:

        // After every third column, I want to add a "|" line.

        // After every third row, I want to add a "--" line.

        System.out.print("\n");

        for (int row = 0; row < grid.length; row ++)
        {
            if (row == 3 || row == 6)
            {
                System.out.print ("---------------------\n");
            }

            for (int col = 0; col < grid[row].length; col ++)
            {
                // If the current square holds a negative number, it is meant to be unknown:

                if (grid[row][col] < 0) // So it is a negative number.
                {
                    System.out.print("  ");
                }

                else if (grid[row][col] == 0) // This means the current square is the one the user is trying to solve.
                {
                    System.out.print("? ");
                }

                else
                {
                    System.out.print(grid[row][col] + " ");
                }

                if (col == 2 || col == 5)
                {
                    System.out.print("| ");
                }
            }

            System.out.print("\n");
        }
    }

    public static void fill_block(int block_number)
    {
        // Method fills the corresponding block, according to the parameter.

        // Vars below are the indexes for the min and max of rows and columns that will be filled.

        int min_row = 0;
        int max_row = 0;
        int min_col = 0;
        int max_col = 0;

        if (block_number == 1) // block in the upper-left corner of the grid.
        {
            min_row = 0;
            max_row = 2;
            min_col = 0;
            max_col = 2;
        }

        else if (block_number == 2) // block in the upper-center of the grid.
        {
            min_row = 0;
            max_row = 2;
            min_col = 3;
            max_col = 5;
        }

        else if (block_number == 3) // block in the upper-right corner of the grid.
        {
            min_row = 0;
            max_row = 2;
            min_col = 6;
            max_col = 8;
        }

        else if (block_number == 4) // block in the middle-left corner of the grid.
        {
            min_row = 3;
            max_row = 5;
            min_col = 0;
            max_col = 2;
        }

        else if (block_number == 5) // block in the center of the grid.
        {
            min_row = 3;
            max_row = 5;
            min_col = 3;
            max_col = 5;
        }

        else if (block_number == 6) // block in the middle-right corner of the grid.
        {
            min_row = 3;
            max_row = 5;
            min_col = 6;
            max_col = 8;
        }

        else if (block_number == 7) // block in the bottom-left corner of the grid.
        {
            min_row = 6;
            max_row = 8;
            min_col = 0;
            max_col = 2;
        }

        else if (block_number == 8) // block in the bottom-center of the grid.
        {
            min_row = 6;
            max_row = 8;
            min_col = 3;
            max_col = 5;
        }

        else if (block_number == 9) // block in the bottom-right corner of the grid.
        {
            min_row = 6;
            max_row = 8;
            min_col = 6;
            max_col = 8;
        }

        // Now to clear the block, in case the program had previously filled it in:

        clear_block(min_row, max_row, min_col, max_col);

        // Now to fill in each square, one at a time, of the block.

        // After giving a square a value, I'll want to test if the square's row, column, and block are all still valid.
        // If they aren't, I'll give the square another random value.

        int counter = 0;

        for (int row = min_row; row <= max_row; row ++)
        {
            for (int col = min_col; col <= max_col; col ++)
            {
                counter ++;

                if (counter >= 50) // it is likely that all numbers for this square create an invalid grid.
                {
                    break; // Since nothing seems to work for this square, I will redo the entire block.  I'm breaking
                    // out of the double for loop, and main will call this fill_block method again.
                }

                grid [row][col] = (int) (Math.random() * 9 + 1);

                // Now to check if the square's row, column, and this block are all still valid.  If not, I'll do
                // col --    in order for this iteration to run again.

                if (!valid_grid())
                {
                    col --; // Causes the iteration of this for loop to run again.
                }

                else // meaning the number in the square is fine.
                {
                    counter = 0; // Stop counting the trials for the square, since the program will be
                    // moving on to the next square now.
                }
            }

            if (counter >= 50)
            {
                break;
            }
        }
    }

    public static void clear_block(int min_row, int max_row, int min_col, int max_col)
    {
        // I want to replace the sent in block with varying negative numbers.  The negative nums will have to be
        // pretty high, since I don't want them to be equal to any numbers already in the grid.

        int num = -90;

        // Use a double for loop to replace the block:

        for (int row = min_row; row <= max_row; row ++)
        {
            for (int col = min_col; col <= max_col; col ++)
            {
                grid[row][col] = num;

                num --;
            }
        }
    }

    public static boolean row_valid(int row_index)
    {
        // This method checks if the row is valid (if there are no two identical numbers in it).

        // I want to add all the numbers in the row parameter to a list:

        ArrayList <Integer> list = new ArrayList <Integer>();

        for (int col = 0; col < grid[row_index].length; col ++)
        {
            list.add(grid[row_index][col]);
        }

        // Now to check if all the numbers in the list are unique - I don't want any two same numbers:

        if (unique_list(list))
        {
            return true;
        }

        return false;
    }

    public static boolean col_valid(int col_index)
    {
        // This method checks if the column is valid (if there are no two identical numbers in it).

        // I want to add all the numbers in the col parameter to a list:

        ArrayList <Integer> list = new ArrayList <Integer>();

        for (int row = 0; row < grid.length; row ++)
        {
            list.add(grid[row][col_index]);
        }

        // Now to check if all the numbers in the list are unique - I don't want any two same numbers:

        if (unique_list(list))
        {
            return true;
        }

        return false;
    }

    public static boolean block_valid(int min_row, int max_row, int min_col, int max_col)
    {
        // Need to compile all the numbers in the specified block into an ArrayList of Integers:

        ArrayList <Integer> list = new ArrayList <Integer>();

        // I'll use a double for loop to run through the block, and add all the numbers to the list:

        for (int row = min_row; row <= max_row; row ++)
        {
            for (int col = min_col; col <= max_col; col ++)
            {
                list.add(grid[row][col]);
            }
        }

        if (unique_list(list))
        {
            return true;
        }

        return false;
    }

    public static boolean unique_list(ArrayList <Integer> list)
    {
        // Now to figure out if there are any two same numbers in the list.  To do this, I'll use a double for loop:

        // The outside for loop is to go from the first element to the last.

        // The inside for loop goes through the list to check if anything equals the current element.

        for (int i = 0; i < list.size(); i++)
        {
            for (int x = 0; x < list.size(); x++)
            {
                if (x == i) // Indexes are the same - don't want to compare the current element to itself:
                {
                    x++;

                    // Need to make sure I didn't go out of bounds by increasing x:

                    if (x == list.size())
                    {
                        break;
                    }
                }

                if (list.get(i) == list.get(x))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean valid_grid()
    {
        // Method accesses if the entire grid is valid.

        // First to deal with all the rows.  I'll use a for loop and the row_valid method:

        for (int row = 0; row <= 8; row ++)
        {
            if (!row_valid(row))
            {
                return false;
            }
        }

        // Now to deal with all the columns.  I'll use a for loop and the col_valid method:

        for (int col = 0; col <= 8; col ++)
        {
            if (!col_valid(col))
            {
                return false;
            }
        }

        // Now to deal with the blocks.  I'll use a double for loop and the block_valid method:

        // Outside for loop goes through the rows.

        // Inside for loop goes across the columns.

        for (int min_row = 0; min_row <= 6; min_row += 3)
        {
            for (int min_col = 0; min_col <= 6; min_col += 3)
            {
                if (!block_valid(min_row, min_row + 2, min_col, min_col + 2))
                {
                    return false;
                }
            }
        }

        // If the program made it this far through the "gauntlet", true is returned:

        return true;
    }
}
