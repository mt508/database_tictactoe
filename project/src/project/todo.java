package project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class todo {
    public static void main(String[] args) {
        linklist l1 = new linklist();
        l1.atbeg("X");
        l1.atbeg("O");

        Scanner sc = new Scanner(System.in);
        Game g1 = new Game(sc, l1);
        g1.playerName();
        g1.move();
        sc.close();
    }
}

// Node class
class Node {
    String data;
    Node next;

    Node(String data) {
        this.data = data;
        this.next = null;
    }
}

// Linked List class
class linklist {
    Node start = null;

    public void atbeg(String data) {
        Node newnode = new Node(data);
        if (start == null) {
            start = newnode;
            newnode.next = start;
        } else {
            newnode.next = start;
            start = newnode;
        }
    }

    public void player_iterator(int i, Scanner sc, int[] arr1, int[] arr2) {
        Node temp = start;

        if (i % 2 == 0) {
            temp = temp.next;
        }

        System.out.print("Enter position for " + temp.data + ": ");
        int position = sc.nextInt();

        if (i % 2 != 0) {
            arr1[i / 2] = position;
        } else {
            arr2[i / 2 ] = position;
        }
    }
}

// Game class
class Game {
    private Scanner sc;
    private linklist player;
    int[] arr1 = new int[5];
    int[] arr2 = new int[4];
    String player1,player2;

    public Game(Scanner sc, linklist player) {
        this.sc = sc;
        this.player = player;
    }

    public void playerName() {
        System.out.println("Enter name of player1:");
         player1 = sc.nextLine();
        System.out.println("Enter name of player2:");
         player2 = sc.nextLine();
    }

    protected void move() {
        for (int i = 1; i <= 9; i++) {
            player.player_iterator(i, sc, arr1, arr2);
            if (i >= 5) { // Check only after at least 5 moves (minimum required to win)
                if (computation(arr1)) {
                    System.out.println( player1+" " +"win");
                    
                    try {
                        new mysqlconnection(player1, player2, player1 + " wins");
                    } catch (Exception e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    return ;
                }
                if (computation(arr2)) {
                    System.out.println(player2+" "+"win");
                    try {
                        new mysqlconnection(player1, player2, player2 + " wins");
                    } catch (Exception e) {
                        System.out.println("Database error: " + e.getMessage());
                    }
                    return;
                }
            }
        }
        System.out.println("It's a Draw!");
        try {
            new mysqlconnection(player1, player2, "Draw");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    protected boolean computation(int[] arr) {
        int[][] temp1 = {
            {1, 2, 3}, {4, 5, 6}, {7, 8, 9}, // Rows
            {1, 4, 7}, {2, 5, 8}, {3, 6, 9}, // Columns
            {1, 5, 9}, {3, 5, 7}             // Diagonals
        };

        for (int[] winPattern : temp1) {
            int count = 0;
            for (int pos : winPattern) {
                for (int move : arr) {
                    if (pos == move) count++;
                }
            }
            if (count == 3) return true; 
        }
        return false;
        

        
        
    }
}

class mysqlconnection  {
	String sql = "INSERT INTO tictactoe (player1, player2, result) VALUES (?, ?, ?)";
String 	username="xxxxxx"; //enter your username 
String password="13XXXXX";// enter your pass
String url="jdbc:mysql://localhost:3306/javaproject";
int rowsAffected;

	mysqlconnection(String player1,String player2,String result)throws Exception{
		Connection  con =  DriverManager.getConnection(url,username,password);	
		PreparedStatement pst = con.prepareStatement(sql);
		pst.setString(1, player1);
        pst.setString(2, player2);
        pst.setString(3, result);
        
         rowsAffected = pst.executeUpdate();
         if (rowsAffected > 0) {
        	    System.out.println("Game result saved successfully!");
        	} else {
        	    System.out.println("Failed to save game result.");
        	}
	 
	
	}
	
	
}
