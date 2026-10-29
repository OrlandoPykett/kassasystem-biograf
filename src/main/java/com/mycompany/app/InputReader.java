package com.mycompany.app;
import java.util.Scanner;
public class InputReader {
    
    private Scanner input;

    public InputReader() {
        this(new Scanner(System.in));
    }
    public InputReader(Scanner input) {
        this.input = input;
    }

    public String readString(String msg) {
        System.out.print(msg + "?>");
        String userString = input.nextLine().trim();
        while (userString.isEmpty()) {
            System.out.println("Error, must not be empty or blank");
            System.out.print(msg + "?>");
            userString = input.nextLine().trim();
        }
        return userString;
    }
}

