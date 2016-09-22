package com.riuvic.smoothball2.tweens;

import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.TweenAccessor;

public class VectorAccessor implements TweenAccessor<Vector2> {


    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;

    @Override
    public int getValues(Vector2 target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case VERTICAL:
                returnValues[0] = target.y;
                return 1;
            case HORIZONTAL:
                returnValues[0] = target.x;
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Vector2 target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case VERTICAL:
                target.y = newValues[0];
                break;
            case HORIZONTAL:
                target.x = newValues[0];
                break;
        }
    }

}
