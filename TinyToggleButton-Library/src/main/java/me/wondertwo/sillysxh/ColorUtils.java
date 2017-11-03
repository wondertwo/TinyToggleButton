package me.wondertwo.sillysxh;

import android.content.res.ColorStateList;

/**
 * Generate above and below color state list use tintColor
 * Created by kyle on 15/11/4.
 */
public class ColorUtils {

    private static final int STATE_ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int STATE_CHECKED_ATTR = android.R.attr.state_checked;
    private static final int STATE_PRESSED_ATTR = android.R.attr.state_pressed;

    static ColorStateList calculateAboveColorByTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-STATE_ENABLE_ATTR, STATE_CHECKED_ATTR},
                {-STATE_ENABLE_ATTR},
                {STATE_PRESSED_ATTR, -STATE_CHECKED_ATTR},
                {STATE_PRESSED_ATTR, STATE_CHECKED_ATTR},
                {STATE_CHECKED_ATTR},
                {-STATE_CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    static ColorStateList calculateBelowColorByTintColor(final int tintColor) {
        int[][] states = new int[][] {
                {-STATE_ENABLE_ATTR, STATE_CHECKED_ATTR},
                {-STATE_ENABLE_ATTR},
                {STATE_CHECKED_ATTR, STATE_PRESSED_ATTR},
                {-STATE_CHECKED_ATTR, STATE_PRESSED_ATTR},
                {STATE_CHECKED_ATTR},
                {-STATE_CHECKED_ATTR}
        };

        int[] colors = new int[] {
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor - 0xD0000000,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }
}
