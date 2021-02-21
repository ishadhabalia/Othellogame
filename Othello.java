import java.util.Scanner;
import java.util.Vector;
import java.io.IOException;
import GameLogic.*;
import Data.*;

public class Othello extends Thread
{
    public static int play_game(GameProcessing b,String p1,String p2,int no_players){
        Scanner sc = new Scanner(System.in);
        GameProcessing.Point move = b.new Point(-1, -1);
        int level;

        System.out.println("                                               _________________________________________                     ");
        System.out.println("                                              |                                         |");
        System.out.println("                                              |          ENTER 1 FOR STANDARD OTHELLO   |");
        System.out.println("                                              |                                         |");
        System.out.println("                                              |          ENTER 2 FOR MODIFIED VERSION   |");
        System.out.println("                                              |_________________________________________|\n\n");
        System.out.print("                                                               ENTER CHOICE:");
        level=sc.nextInt();
        //Score.level(choice);
        //String winner;
        //System.out.println("\nBlack Moves first\n");

        int wmoves=0,bmoves=0,flips=0,flag=0;
        int result;
        Boolean skip;
        int input;

        while(true) {
            Vector<GameProcessing.Point> black_valid_moves;
            Vector<GameProcessing.Point> white_valid_moves;
            flag=0;
            while (flag != 1)
            {
                skip = false;
                //System.out.print("\033[H\033[2J");
                //System.out.print('\u000C');
                //System.out.flush();

                b.update_scores();
                System.out.println("\n\n\n\n\n  Press 00 to leave current game");
                if(no_players==1)
                {
                    System.out.println("     COMPUTER PLAYS WHITE");
                    System.out.println("        "+p1+" : Black");
                }
                else
                    System.out.println("   "+p1+" : Black"+"  "+p2+" : White");
                System.out.println("\t  Black: " + b.BScore + " White: " + b.WScore);


                black_valid_moves = b.find_valid_moves('B', 'W', 1);
                white_valid_moves = b.find_valid_moves('W', 'B', -1);
                result = b.game_result(white_valid_moves, black_valid_moves);

                if (result == 0)
                {
                    System.out.println("It is a draw.");
                    break;
                } else if (result == 1)
                {
                    if(no_players==1) System.out.println("Computer (White) wins: " + b.WScore + ":" + b.BScore);
                    else {
                        System.out.println(p2 + " (White) wins: " + b.WScore + ":" + b.BScore);
                        UserData.update(p2, wmoves, level);
                    }
                    return 0;
                } else if (result == -1)
                {
                    System.out.println(p1+" (Black) wins: " + b.BScore + ":" + b.WScore);
                    UserData.update(p1, bmoves, level);

                    return 0;
                }

                if (black_valid_moves.isEmpty()) {
                    System.out.println("Black has no valid moves! Turn skipped");
                    skip = true;
                    flag=1;
                }

                if (!skip) {
                    System.out.print("Place move (Black) : ");
                    input=sc.nextInt();
                    do {
                        move.y = input % 10;
                        move.x = ((input - move.y) / 10);
                        if (move.x == 0 && move.y == 0)
                        {
                            //break;
                            return 0;
                        }
                        --move.x;
                        --move.y;

                        if (!black_valid_moves.contains(move)) {
                            System.out.print("Invalid move!\nPlace move again (Black) : ");
                            input=sc.nextInt();
                        } else break;
                    } while (true);

                    if (level == 1)
                    {
                        b.place_move(move, 'B', 'W');
                        bmoves++;
                        flag=1;
                    } else {
                        flips = b.place_move_modified(move, 'B', 'W');
                        bmoves++;
                        if (flips % 2 !=0)
                        {
                            flag=1;
                        }
                    }
                }

            }
            flag=0;
            while(flag!=1) {
                skip = false;

                b.update_scores();
                System.out.println("\n\n\n\n\n  Press 00 to leave current game");
                if(no_players==1)
                {
                    System.out.println("     COMPUTER PLAYS WHITE");
                    System.out.println("        "+p1+" : Black");
                }
                else
                    System.out.println("   "+p1+" : Black"+"  "+p2+" : White");
                System.out.println("\t  White: " + b.WScore + " Black: " + b.BScore);

                white_valid_moves = b.find_valid_moves('W', 'B', 1);
                black_valid_moves = b.find_valid_moves('B', 'W', -1);
                result = b.game_result(white_valid_moves, black_valid_moves);

                if (result == 0) {
                    System.out.println("It is a draw.");
                    break;
                } else if (result == 1) {
                    if(no_players==1) System.out.println("Computer (White) wins: " + b.WScore + ":" + b.BScore);
                    else {
                        System.out.println(p2 + " (White) wins: " + b.WScore + ":" + b.BScore);
                        UserData.update(p2, wmoves, level);
                    }
                    return 0;
                } else if (result == -1) {
                    System.out.println("Black wins: " + b.BScore + ":" + b.WScore);
                    UserData.update(p1, bmoves, level);
                    return 0;
                }

                if (white_valid_moves.isEmpty()) {
                    System.out.println("White has no valid moves! Turn skipped");
                    skip = true;
                    flag=1;
                }

                if (!skip) {
                    System.out.print("Place move (White) : ");

                    if(no_players==1)
                    {
                        move=b.computer_move(white_valid_moves);
                        System.out.println((move.x+1)+""+(move.y+1));
                        try
                        {
                            Thread.sleep(2000);
                        }
                        catch(Exception e)
                        {
                            System.out.println("There was an error");
                        }
                    } //computer's move

                    else{
                        input=sc.nextInt();
                        do {
                            move.y = input % 10;
                            move.x = ((input - move.y) / 10);
                            if (move.x == 0 && move.y == 0) {
                                //sc.close();
                                return 0;
                                //break;
                            }
                            --move.x;
                            --move.y;

                            if (!white_valid_moves.contains(move)) {
                                System.out.print("Invalid move!\nPlace move again (White) : ");
                                input=sc.nextInt();
                            } else break;
                        } while (true);
                    }

                    if (level == 1) {
                        b.place_move(move, 'W', 'B');
                        wmoves++;
                        flag=1;
                    } else
                    {
                        flips=b.place_move_modified(move, 'W', 'B');
                        wmoves++;
                        if(flips%2!=0)
                        {
                            flag=1;
                        }

                    }
                }
            }
        }
        //return 0;
    }



    /*
    public static void clearScreen() {00
        System.out.print("\033[H\033[2J");
        System.out.flush();
       }
    */
    public static String get_username()
    {
        Scanner sc=new Scanner(System.in);
        int log,p,k=0;
        String n1; int exists;
        //loop:
        while(true) {
            System.out.println("                                               ____________________________________________                     ");
            System.out.println("                                              |                                            |");
            System.out.println("                                              |          ENTER 1 IF EXISTING USER          |");
            System.out.println("                                              |                                            |");
            System.out.println("                                              |            ENTER 2 IF NEW USER             |");
            System.out.println("                                              |____________________________________________|\n\n");
            System.out.print("                                                            ENTER CHOICE:");
            log=sc.nextInt();
            System.out.print("                                                            ENTER USERNAME : ");
            n1 = sc.next();
            exists=UserData.check(n1);
            switch (log) {
                case 1: //Existing user
                    if (exists == 1) {
                        System.out.println("                                                   YOU HAVE ENTERED SUCCESSFULLY");
                        return n1;
                    }
                    else {
                        System.out.println("                                      USERNAME DOES NOT EXIST. PLEASE ENTER VALID USERNAME!");
                    }
                    break;
                case 2: //New User
                    if(exists==1) {
                        System.out.println("                                           THE USERNAME IS ALREADY TAKEN. TRY ANOTHER ONE!");
                    }
                    else{
                        UserData.add_username(n1);
                        System.out.println("                                      USERNAME SUCCESSFULLY ADDED! YOU HAVE ENTERED SUCCESSFULLY ");
                        return n1;
                    }
                    break;
                default: System.out.println("Please enter valid choice");
                    break;

            }
        }
    }
    public static void main(String[] args)//throws IOException, InterruptedException
    {

        //System.out.println(Integer.parseInt(1+""+2));
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                     _________     __________                   _______                              _________");
        System.out.println("                    |         |        |         |       |     |             |           |          |         |");
        System.out.println("                    |         |        |         |       |     |             |           |          |         |");
        System.out.println("                    |         |        |         |_______|     |______       |           |          |         |         |");
        System.out.println("                    |         |        |         |       |     |             |           |          |         |");
        System.out.println("                    |_________|        |         |       |     |_______      |_______    |_______   |_________|");
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------\n\n");

        int choice,moves,choice2,choice3;
        String n1,n2;
        Scanner sc1=new Scanner(System.in);
        //Score.sorting(2);
        System.out.println("                                               _________________________________________                     ");
        System.out.println("                                              |                                         |");
        System.out.println("                                              |          ENTER 1 FOR 1 PLAYER           |");
        System.out.println("                                              |                                         |");
        System.out.println("                                              |          ENTER 2 FOR 2 PLAYERS          |");
        System.out.println("                                              |_________________________________________|\n\n");
        System.out.print("                                                               ENTER CHOICE:");
        choice3=sc1.nextInt();
        if(choice3==1)
        {
            System.out.println("                                                               PLAYER ");
            n1=get_username();
            n2="";
        }
        else {
            System.out.println("                                                               PLAYER 1");
            n1 = get_username();
            System.out.println("                                                               PLAYER 2");
            n2 = get_username();
        }

        do {
            System.out.println("                                      ____________________________________________________________                     ");
            System.out.println("                                     |                                                            |");
            System.out.println("                                     |                       ENTER 1 TO PLAY                      |");
            System.out.println("                                     |                                                            |");
            System.out.println("                                     |                    ENTER 2 TO READ THE RULES               |");
            System.out.println("                                     |                                                            |");
            System.out.println("                                     |       ENTER 3 TO DISPLAY HIGH SCORES OF STANDARD OTHELLO   |");
            System.out.println("                                     |                                                            |");
            System.out.println("                                     |       ENTER 4 TO DISPLAY HIGH SCORES OF MODIFIED VERSION   |");
            System.out.println("                                     |                                                            |");
            System.out.println("                                     |                        ENTER 5 TO EXIT                     |");
            System.out.println("                                     |____________________________________________________________|\n\n");
            System.out.print("                                                            ENTER CHOICE:");
            choice2=sc1.nextInt();
            switch(choice2)
            {
                case 1:
                    do
                    {
                        GameProcessing b = new GameProcessing();
                        play_game(b,n1,n2,choice3);
                        //System.out.println(moves);
                        //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        System.out.print("\nENTER 1 to PLAY AGAIN AND 2 TO GO BACK TO MENU : ");
                        choice=sc1.nextInt();
                    }while(choice!=2);
                    break;
                case 2:
                    System.out.println("\n\n                                                                 RULES\n");
                    System.out.println("#The board will start with 2 black and 2 white coins at the centre of the board. \n#Player 1 is allotted black coins and Player 2 is white coins and black always moves first" +
                            "\n#The goal is to get the majority of your color’s coins on the board. " +
                            "\n#A move is made by placing a coin of the player's color on the board in a position such that flips one or more of the opponent's coins. " +
                            "\n#A coin or series of coins is flipped when it is surrounded at the ends by the coins of the opposite color. " +
                            "\n#A coin may flip any number of coins in one or more direction (horizontal, vertical, diagonal). " +
                            "\n#The game alternates between white and black until: \n" +
                            "1.Both players have no valid moves \n2.The board has no empty squares left \n#Hard Level Variation: " +
                            "The opponent’s turn is skipped if the player manages to flip even number of coins of opponent." +
                            "\n#High scores : A player’s least number of moves taken to win a game are considered as that player’s high scores.\n");
                    break;
                case 3:
                    UserData.sorting(1);
                    break;
                case 4:
                    UserData.sorting(2);
                    break;
                case 5:
                    System.out.println("                                                      YOU HAVE EXITED SUCCESSFULLY");
                    break;
                default:
                    break;

            }
        }while(choice2!=5);

        sc1.close();
    }
}