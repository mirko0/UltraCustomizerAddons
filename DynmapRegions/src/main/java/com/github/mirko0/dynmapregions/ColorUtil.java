package com.github.mirko0.dynmapregions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

public class ColorUtil {
    /**
     * This method is used to create a java color object from hex color code.
     * @param colorStr - hex color code.
     * @return java.awt.Color
     *
     * example: #hex2RGB(#ffffff)
     */
    public static Color hex2RGB(String colorStr) {
        return new Color(
                Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                Integer.valueOf( colorStr.substring( 5, 7 ), 16 ));
    }
    public static String toHexString(Color color) {
        String hex = Integer.toHexString(color.getRGB() & 0xFFFFFF);
        if (hex.length() < 6) {
            hex = "0" + hex; // pad with zero if needed
        }
        return "#" + hex.toUpperCase();
    }

    /**
     * @param s - potentional hexadecimal color string Example: #ffffff
     * @return - true if the string is valid
     */
    public static boolean isValidHexString(String s){
        return s.matches("#([A-Fa-f0-9]{6})");
    }

    @AllArgsConstructor
    public enum DefaultColors {

        NAVY("#001f3f"),
        BLUE("#0074D9"),
        AQUA("#7FDBFF"),
        TEAL("#39CCCC"),
        PURPLE("#B10DC9"),
        PINK("#F012BE"),
        DARK_PINK("#85144b"),
        RED("#FF4136"),
        DARK_RED("#dc2f2f"),
        ORANGE("#FF851B"),
        YELLOW("#FFDC00"),
        OLIVE("#3D9970"),
        GREEN("#2ECC40"),
        LIME("#01FF70"),
        BROWN("#402a23"),
        BLACK("#111111"),
        GRAY("#AAAAAA"),
        SILVER("#DDDDDD"),
        WHITE("#FFFFFF")
        ;

        @Getter private String hexCode;
        public Color getColor(){
            return hex2RGB(hexCode);
        }

    }
}
