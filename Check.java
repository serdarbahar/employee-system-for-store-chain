
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Check {
    public static void main(String[] args) throws FileNotFoundException {
        File file1 = new File("/Users/student/Desktop/out.txt");
        File file2 = new File("/Users/student/Desktop/test-cases/outputs/ask_large.txt");
        Scanner mine = new Scanner(file1);
        Scanner their = new Scanner(file2);
        Scanner input = new Scanner(System.in);
        System.out.println("Mode (all, first X): ");
        String mode = input.nextLine();
        int counter = 1;
        int stop;
        if (mode.split(" ")[0].equals("first")){
            stop = Integer.parseInt(mode.split(" ")[1]);
        }
        else{
            stop = 0;
        }
        int line = 1;


        while (mine.hasNext()){
            String their_line = their.nextLine();
            String my_line = mine.nextLine();

            if(!their_line.equals(my_line)){
                System.out.println(String.format("--- LINE %s ---",line));
                System.out.println("Their line: " + their_line);
                System.out.println("My line: " + my_line);
                System.out.println("\n\n");

                if (counter == stop){
                    System.exit(1);
                }
                counter++;
            }
            line++;

        }
    }
}