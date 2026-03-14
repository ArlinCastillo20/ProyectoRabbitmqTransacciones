package com.Arli.consumer;

public class ScoreArray {


    public static int score(int[] numbers) {

        int total = 0;

        for (int n : numbers) {

            if (n == 5) {
                total += 5;
            } 
            else if (n % 2 == 0) {
                total += 1;
            } 
            else {
                total += 3;
            }

        }

        return total;
    }

    public static void main(String[] args) {

        int[] arr1 = {1,2,3,4,5};
        System.out.println(score(arr1)); // 13

        int[] arr2 = {17,19,21};
        System.out.println(score(arr2)); // 9

        int[] arr3 = {5,5,5};
        System.out.println(score(arr3)); // 15

    }
}