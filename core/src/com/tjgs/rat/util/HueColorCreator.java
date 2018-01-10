package com.tjgs.rat.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Tyler Johnson on 4/24/2017.
 *
 */
public class HueColorCreator {

    private static Color[] colors = new Color[]{new Color(1, 0, 0, 1), new Color(1, 1, 0, 1), new Color(0, 1, 0, 1)};
    private static float[] percents = new float[]{0, 0.5f, 1};

    public static Color createColor(float percent){
        return createColor(percent, colors, percents);
    }

    public static Color createColor(float percent, Color[] colors, float[] percents){

        assert percent >= 0;
        assert percents.length > 0;
        assert percents.length == colors.length;
        assert percents[0] >= 0;

        int between = 1;

        for(int i = 1; i < percents.length; i++){
            if(percent <= percents[i]){
                between = i;
                break;
            }
        }

        Color lowerC = colors[between - 1];
        Color upperC = colors[between];

        float lowerP = percents[between - 1];
        float upperP = percents[between];

        float range = upperP - lowerP;
        float percentRange = (percent - lowerP) / range;

        float top = percentRange;
        float bottom = 1 - percentRange;

        float r = (lowerC.r * bottom) + (upperC.r * top);
        float g = (lowerC.g * bottom) + (upperC.g * top);
        float b = (lowerC.b * bottom) + (upperC.b * top);
        float a = (lowerC.a * bottom) + (upperC.a * top);

        return new Color(r, g, b, a);
    }

}
