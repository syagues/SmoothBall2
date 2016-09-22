package com.riuvic.smoothball2.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.riuvic.smoothball2.tweens.Value;
import com.riuvic.smoothball2.tweens.ValueAccessor;

import java.util.ArrayList;
import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;


public class ColorManager {
    private TweenManager manager;
    private Value c1 = new Value();
    private Value c2 = new Value();
    private Value c3 = new Value();
    private TweenCallback cb, cb1, cb2;
    private Color color;
    private ArrayList<String> colors = new ArrayList<String>();
    private float target1, target2, target3;

    private Random randomGenarator;
    private Integer random, rtime;

    public ColorManager() {

        // COLORS
        //this.colors.add("#ecf0f1");
        this.colors.add("#FBAA7B");
        this.colors.add("#6DC589");
        this.colors.add("#ABA5D1");
        //this.colors.add("#8e44ad");
        //this.colors.add("#2980b9");

        color = parseColor(colors.get(MathUtils.random(0,colors.size()-1)), 1f);
        c1.setValue(color.r);
        c2.setValue(color.g);
        c3.setValue(color.b);

        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();

        randomGenarator = new Random();

        cb = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                random = randomGenarator.nextInt(colors.size());
                rtime = randomGenarator.nextInt(10) + 4;
                target1 = parseColor(colors.get(random), 1f).r;
                Tween.to(c1, -1, rtime).target(target1).repeatYoyo(0, 0)
                        .setCallback(cb)
                        .setCallbackTriggers(TweenCallback.COMPLETE)
                        .ease(TweenEquations.easeInOutSine).start(manager);
            }
        };

        cb1 = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                target2 = parseColor(colors.get(random), 1f).g;
                Tween.to(c2, -1, rtime + 0.00001f).target(target2)
                        .repeatYoyo(0, 0).setCallback(cb1)
                        .setCallbackTriggers(TweenCallback.COMPLETE)
                        .ease(TweenEquations.easeInOutSine).start(manager);
            }
        };

        cb2 = new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                target3 = parseColor(colors.get(random), 1f).b;
                Tween.to(c3, -1, rtime + 0.00002f).target(target3)
                        .repeatYoyo(0, 0).setCallback(cb2)
                        .setCallbackTriggers(TweenCallback.COMPLETE)
                        .ease(TweenEquations.easeInOutSine).start(manager);
            }
        };

        Tween.to(c1, -1, 5).target(color.r).repeatYoyo(0, 0).setCallback(cb).setCallbackTriggers(
                TweenCallback.COMPLETE)
                .ease(TweenEquations.easeInOutSine).start(manager);
        Tween.to(c2, -1, 5.00001f).target(color.g).repeatYoyo(0, 0).setCallback(cb1)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .ease(TweenEquations.easeInOutSine).start(manager);
        Tween.to(c3, -1, 5.00002f).target(color.b).repeatYoyo(0, 0).setCallback(cb2)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .ease(TweenEquations.easeInOutSine).start(manager);

    }

    public Color getColor() {
        return color;
    }

    public void update(float delta) {
        manager.update(delta);
        color = new Color(c1.getValue(), c2.getValue(), c3.getValue(), 1f);
    }

    public static Color parseColor(String hex, float alpha) {
        String hex1 = hex;
        if (hex1.indexOf("#") != -1) {
            hex1 = hex1.substring(1);
            // Gdx.app.log("Hex", hex1);
        }
        Color color = Color.valueOf(hex1);
        color.a = alpha;
        return color;
    }
}
