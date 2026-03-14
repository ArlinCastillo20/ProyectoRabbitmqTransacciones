package com.Arli.consumer;

public class SecondMinMax {

    public static int[] secondMinMax(int[] numbers) {

        int min = Integer.MAX_VALUE;
        int secondMin = Integer.MAX_VALUE;

        int max = Integer.MIN_VALUE;
        int secondMax = Integer.MIN_VALUE;

        for (int n : numbers) {

            if (n < min) {
                secondMin = min;
                min = n;
            } 
            else if (n < secondMin && n != min) {
                secondMin = n;
            }

            if (n > max) {
                secondMax = max;
                max = n;
            } 
            else if (n > secondMax && n != max) {
                secondMax = n;
            }
        }

        return new int[]{secondMin, secondMax};
    }

    public static void main(String[] args) {

        int[] arr = {7,2,9,1,1,8};

        int[] result = secondMinMax(arr);

        System.out.println(result[0] + "," + result[1]);
    }
}
