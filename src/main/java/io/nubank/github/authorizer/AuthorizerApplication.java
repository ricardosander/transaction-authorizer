package io.nubank.github.authorizer;

import java.util.Scanner;

public class AuthorizerApplication {

    public static void main(String[] args) {
        System.out.println("Hello World!");

        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            System.out.println(input.nextLine());
        }
    }

}
