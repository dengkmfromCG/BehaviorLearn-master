package com.gdut.dkmfromcg.behaviorlearn.behavior.android;

/**
 * Created by dkmFromCG on 2018/5/20.
 * function:
 */

public class MathUtils {
    static int constrain(int amount, int low, int high) {
        return amount < low ? low : (amount > high ? high : amount);
    }

    static float constrain(float amount, float low, float high) {
        return amount < low ? low : (amount > high ? high : amount);
    }
}
