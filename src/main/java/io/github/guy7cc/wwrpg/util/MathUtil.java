package io.github.guy7cc.wwrpg.util;

import java.util.Random;

public class MathUtil {
    public static int[] randomPermutation(int size){
        if(size <= 0) throw new IllegalArgumentException("The size of permutation must be more than 0.");
        int[] a = new int[size];
        for (int i = 0; i < size; i++){
            a[i] = i;
        }
        Random r = new Random();
        int j, tmp;
        for(int i = 0; i < size - 1; i++){
            j = r.nextInt(i, size);
            tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return a;
    }
}
