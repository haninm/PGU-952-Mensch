package models;

import java.util.Scanner;

/**
 * Created by hani on 02/07/2017.
 */
public class MainTest {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        Scanner sc = new Scanner(System.in);
        board.nextRound();
        while(sc.next()!= null){
            board.nextRound();

        }

    }
}
