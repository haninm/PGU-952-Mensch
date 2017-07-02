import java.util.Scanner;

/**
 * Created by hani on 02/07/2017.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Board board = new Board();
        Scanner sc = new Scanner(System.in);
        board.playGame();
        while(sc.next()!= null){
            board.playGame();

        }

    }
}
