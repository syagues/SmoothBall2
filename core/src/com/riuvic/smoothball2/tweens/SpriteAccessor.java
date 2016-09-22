package com.riuvic.smoothball2.tweens;

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int ALPHA = 1;
    public static final int SCALE = 2;
    public static final int VERTICAL = 3;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case SCALE:
                returnValues[0] = target.getScaleX();
                return 2;
            case VERTICAL:
                returnValues[0] = target.getY();
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b,
                        newValues[0]);
                break;
            case SCALE:
                target.setScale(newValues[0]);
                break;
            case VERTICAL:
                target.setY(newValues[0]);
                break;
        }
    }

}
