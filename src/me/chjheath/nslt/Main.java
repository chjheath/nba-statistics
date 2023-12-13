package me.chjheath.nslt;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/*
    Sources listed in final writeup document attached to presentation.
*/

public class Main {

    public static void main(String[] args) {

        Scanner k = new Scanner(System.in);

        /*
        User & Pass are char arrays because I heard it is easier to remove from memory. Seemed like safer thing to do than making it a String object.
         */

        System.out.println();

        System.out.println();
        System.out.print("Database (TA/Professor Note: Type in REDACTED): ");
        System.out.println();

        char[] db = k.next().toCharArray();

        System.out.println();
        System.out.print("Username (TA/Professor Note: Not including in SRC but use the login given to me): ");
        System.out.println();

        char[] user = k.next().toCharArray();

        System.out.println();

        System.out.print("Password (TA/Professor Note: Not including in SRC but use the login given to me): ");
        System.out.println();

        char[] pass = k.next().toCharArray();

        System.out.println();

        /*
        Two counters to track final score to determine better player.
         */

        int player1 = 0;
        int player2 = 0;

        /*
        Using variables so I am not leaving a username & password left in a string in plaintext that can be read.
         */

        String url = "jdbc:sqlserver://REDACTED;database=" + String.valueOf(db) + ";user=" + String.valueOf(user) + ";password=" + String.valueOf(pass) + ";";

        ResultSet rs = null; //Creating new result set.

        try {
            Class.forName("java.sql.DriverManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url); /*Connecting to SQL Server using the given URL*/
            Statement stmt = conn.createStatement();) {

            /*
            Clearing out char array to avoid misuse of data left in memory.
            I know I'd be the only one accessing this but I feel I should still add a little security just for my own sake.
             */

            user = null;
            pass = null;
            db = null;

            System.out.println();
            System.out.println("\u001B[32mSuccessfully connected to SQL Server\u001B[0m");

            /*
                Asking user for their choice on what to do next.
             */

            System.out.println("\nWhat would you like to do?");
            System.out.println("\t1. List all players (all stats/info).");
            System.out.println("\t2. List all players from a specific college.");
            System.out.println("\t3. List all players with a career average of a specified number or greater in a specific category.");
            System.out.println("\t4. List all players from a specific team.");
            System.out.println("\t5. Compare two player's statistics.");
            System.out.println("\t6. List the lowest career average in a specific category.");
            System.out.println("\t7. List the highest career average in a specific category.");
            System.out.println("\t8. List the average of the (career averages) in a specific category.");
            System.out.println("\t9. Show information on a specific player.");
            System.out.println("\t10. List all player's first and last names and their teams.");
            System.out.println("\t11. List all players from a specific draft class with a career average of a specific number or greater in a specific category.");
            System.out.println("\t12. List all players that their team name begins with a specific letter.");
            System.out.println("\t13. List all teams.");
            System.out.println("\t14. List all teams in a specific conference.");
            System.out.println("\t0. Exit");

            System.out.println();
            System.out.print("Choice: ");

            int ch = k.nextInt();

            k.nextLine();
            System.out.println();

            String select = ""; /*Creating new String object to dynamically update the SELECT statements.*/

            if (ch == 1) {
                select = "SELECT P_FN, P_LN, NUM, T_NAME, COLLEGE, DRAFTED, PTS, AST, BLK, STL, REB FROM PLAYER, STAT, TEAM WHERE PLAYER.T_ID = TEAM.T_ID AND PLAYER.S_ID = STAT.S_ID ORDER BY NUM;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {

                    System.out.println("Name:\t" + rs.getString(1) + rs.getString(2));
                    System.out.println();
                    System.out.println("\tInformation:\n");
                    System.out.println("\t\tNumber: " + rs.getInt(3));
                    System.out.println("\t\tTeam: " + rs.getString(4));
                    System.out.println("\t\tCollege / From: " + rs.getString(5));
                    System.out.println("\t\tYear Drafted: " + rs.getInt(6));
                    System.out.println("\t\tPTS: " + rs.getFloat(7));
                    System.out.println("\t\tAST: " + rs.getFloat(8));
                    System.out.println("\t\tBLK: " + rs.getFloat(9));
                    System.out.println("\t\tSTL: " + rs.getFloat(10));
                    System.out.println("\t\tREB: " + rs.getFloat(11));
                    System.out.println();
                }

            } else if (ch == 2) {
                System.out.print("What college would you like to look up: ");
                String col = k.nextLine();
                System.out.println();

                select = "SELECT P_FN, P_LN FROM PLAYER WHERE COLLEGE = '" + col + "' ORDER BY P_LN;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("First: " + rs.getString(1));
                    System.out.println("Last: " + rs.getString(2));
                    System.out.println();
                }

            } else if (ch == 3) {
                System.out.print("What category would you like to look up (PTS, AST, BLK, STL, REB): ");
                String cat = k.nextLine();
                System.out.println();

                System.out.print("What value would you like to choose: ");
                float val = k.nextFloat();
                k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), " + cat + " FROM PLAYER, STAT WHERE " + cat + "  >= " + val + " AND PLAYER.S_ID = STAT.S_ID ORDER BY " + cat + ";";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println(cat + ": " + rs.getFloat(2));
                    System.out.println();
                }

            } else if (ch == 4) {
                System.out.print("What team would you like to choose: ");
                String tm = k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), NUM, T_NAME FROM PLAYER, TEAM WHERE PLAYER.T_ID = TEAM.T_ID AND T_NAME = '" + tm + "' ORDER BY NUM;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("Number: " + rs.getInt(2));
                    System.out.println("Team: " + rs.getString(3));
                    System.out.println();
                }

            } else if (ch == 5) {

                ArrayList<Float> vals = new ArrayList<>();

                System.out.print("Player 1 First Name: ");
                String p1f = k.nextLine();
                System.out.println();

                System.out.print("Player 1 Last Name: ");
                String p1l = k.nextLine();
                System.out.println();

                System.out.print("Player 2 First Name: ");
                String p2f = k.nextLine();
                System.out.println();

                System.out.print("Player 2 Last Name: ");
                String p2l = k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), PTS, AST, BLK, STL, REB FROM PLAYER, STAT WHERE P_LN = '" + p1l + "' AND P_FN = '" + p1f + "' AND PLAYER.S_ID = STAT.S_ID" +
                        " UNION ALL SELECT CONCAT(CONCAT(P_FN, ''), P_LN), PTS, AST, BLK, STL, REB FROM PLAYER, STAT WHERE P_LN = '" + p2l + "' AND P_FN = '" + p2f + "' AND PLAYER.S_ID = STAT.S_ID;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("\t\tPTS: " + rs.getFloat(2));
                    vals.add(rs.getFloat(2));
                    System.out.println("\t\tAST: " + rs.getFloat(3));
                    vals.add(rs.getFloat(3));
                    System.out.println("\t\tBLK: " + rs.getFloat(4));
                    vals.add(rs.getFloat(4));
                    System.out.println("\t\tSTL: " + rs.getFloat(5));
                    vals.add(rs.getFloat(5));
                    System.out.println("\t\tREB: " + rs.getFloat(6));
                    vals.add(rs.getFloat(6));
                    System.out.println();
                }

                /*Array is built like this: player 1 pts, player 1 ast, player 1 blk, player 1 stl, player 1 reb, player 2 pts, player 2 ast, player 2 blk, player 2 stl, player 2 reb
                    I am essentially accessing the first 5 elements and comparing them to 5 indices ahead of them (player 2's stats) and adding a point to their total.
                 */

                for(int x = 0; x < vals.size()/2; x++) {
                    if(vals.get(x) > vals.get(x+5)) {
                        player1 += 1;
                    } else if (vals.get(x+5) > vals.get(x)) {
                        player2 += 1;
                    }
                }

                /*
                Checking for winner. After each category, the higher will get points added
                 */

                if(player1 > player2) {
                    System.out.println((p1f + " " + p1l) + " (" + player1 + ")" + " is statistically better than " + (p2f + " " + p2l) + " (" + player2 + ")"  + " over their careers.");
                } else if (player2 > player1) {
                    System.out.println("\n" + (p2f + " " + p2l) + " (" + player2 + ")"   + " is statistically better than " + (p1f + " " + p1l) + " (" + player1 + ")"  + " over their careers.");
                } else {
                    System.out.println("\nBoth players are similar in statistics over their careers.");
                }

            } else if (ch == 6) {
                System.out.print("What category would you like to look up (PTS, AST, BLK, STL, REB): ");
                String cat = k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), " + cat + " FROM PLAYER, STAT WHERE " + cat + " = (SELECT MIN(" + cat + ") FROM STAT) AND PLAYER.S_ID = STAT.S_ID GROUP BY P_FN, P_LN, " + cat + ";";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("Lowest Career AVG in " + cat + ": " + rs.getFloat(2));
                    System.out.println();
                }

            } else if (ch == 7) {
                System.out.print("What category would you like to look up (PTS, AST, BLK, STL, REB): ");
                String cat = k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), " + cat + " FROM PLAYER, STAT WHERE " + cat + " = (SELECT MAX(" + cat + ") FROM STAT) AND PLAYER.S_ID = STAT.S_ID GROUP BY P_FN, P_LN, " + cat + ";";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("Highest Career AVG in " + cat + ": " + rs.getFloat(2));
                    System.out.println();
                }

            } else if (ch == 8) {

                System.out.print("What category would you like to look up (PTS, AST, BLK, STL, REB): ");
                String cat = k.nextLine();
                System.out.println();

                select = "SELECT AVG(" + cat + ") FROM STAT;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Average Career AVG in " + cat + ": " + rs.getFloat(1));
                }

            } else if (ch == 9) {

                System.out.print("Player First Name: ");
                String p1f = k.nextLine();
                System.out.println();

                System.out.print("Player Last Name: ");
                String p1l = k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), NUM, T_NAME, COLLEGE, DRAFTED, PTS, AST, BLK, STL, REB FROM PLAYER, STAT, TEAM WHERE PLAYER.S_ID = STAT.S_ID AND PLAYER.T_ID = TEAM.T_ID AND P_LN = '" + p1l + "' AND P_FN = '" + p1f + "';";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("\nInformation:");
                    System.out.println("\t\tNumber: " + rs.getInt(2));
                    System.out.println("\t\tTeam: " + rs.getString(3));
                    System.out.println("\t\tCollege / From: " + rs.getString(4));
                    System.out.println("\t\tYear Drafted: " + rs.getInt(5));
                    System.out.println("\nStatistics:");
                    System.out.println("\t\tPTS: " + rs.getFloat(6));
                    System.out.println("\t\tAST: " + rs.getFloat(7));
                    System.out.println("\t\tBLK: " + rs.getFloat(8));
                    System.out.println("\t\tSTL: " + rs.getFloat(9));
                    System.out.println("\t\tREB: " + rs.getFloat(10));
                    System.out.println();
                }

            } else if (ch == 10) {

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), T_NAME FROM PLAYER, TEAM WHERE PLAYER.T_ID = TEAM.T_ID ORDER BY P_FN, P_LN;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("Team: " + rs.getString(2));
                    System.out.println();
                }

            } else if (ch == 11) {

                System.out.print("What year would you like to choose: ");
                int year = k.nextInt();
                k.nextLine();
                System.out.println();

                System.out.print("What category would you like to look up (PTS, AST, BLK, STL, REB): ");
                String cat = k.nextLine();
                System.out.println();

                System.out.print("What value would you like to choose: ");
                float val = k.nextFloat();
                k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), DRAFTED, T_NAME, " + cat + " FROM PLAYER, STAT, TEAM WHERE PLAYER.S_ID = STAT.S_ID AND PLAYER.T_ID = TEAM.T_ID AND " + cat + " >= " + val + " AND DRAFTED = " + year + " ORDER BY " + cat + ";";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("\nInformation:");
                    System.out.println("\t\tYear Drafted: " + rs.getInt(2));
                    System.out.println("\t\tTeam: " + rs.getString(3));
                    System.out.println("\t\t" + cat + ": " + rs.getFloat(4));
                    System.out.println();
                }

            } else if (ch == 12) {

                System.out.print("What letter would you like to choose: ");
                String let = k.nextLine();
                System.out.println();

                select = "SELECT CONCAT(CONCAT(P_FN, ''), P_LN), T_NAME FROM PLAYER, TEAM WHERE PLAYER.T_ID = TEAM.T_ID AND T_NAME LIKE '" + let + "%' ORDER BY P_LN;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Name: " + rs.getString(1));
                    System.out.println("Team: " + rs.getString(2));
                    System.out.println();
                }

            } else if (ch == 13) {

                select = "SELECT T_NAME, T_CONF FROM TEAM ORDER BY T_CONF DESC;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Team: " + rs.getString(1) + " [" + rs.getString(2).trim() + "]");
                    System.out.println();
                }

            }  else if (ch == 14) {

                System.out.print("What conference would you like to choose? (EAST or WEST): ");
                String conf = k.nextLine();
                System.out.println();

                select = "SELECT T_NAME FROM TEAM WHERE T_CONF = '" + conf + "' ORDER BY T_NAME ASC;";
                rs = stmt.executeQuery(select);

                while(rs.next()) {
                    System.out.println("Team: " + rs.getString(1));
                    System.out.println();
                }

            } else {
                System.exit(0);
            }

        } catch (SQLException ex) {
            System.out.println("\u001B[31mCould not connect to SQL Server\n");

            System.out.println("\u001B[31mSee stacktrace below:");
            System.out.print("\t");
            ex.printStackTrace();
        }
    }

}
