package ca.mcmaster.cas.se2aa4.a2.visualizer.team50.properties;

import ca.mcmaster.cas.se2aa4.a2.io.Structs;

import java.awt.*;
import java.util.List;

public class Property {

    public static Color extractColor(java.util.List<Structs.Property> properties) {
        String val = null;
        for(Structs.Property p: properties) {
            if (p.getKey().equals("rgb_color")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return Color.BLACK;
        String[] raw = val.split(",");
        int red = Integer.parseInt(raw[0]);
        int green = Integer.parseInt(raw[1]);
        int blue = Integer.parseInt(raw[2]);
        return new Color(red, green, blue);
    }

    public static Float extractThickness(java.util.List<Structs.Property> properties) {
        String val = null;
        for(Structs.Property p: properties) {
            if (p.getKey().equals("thickness")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return 3.0f;

        Float thicknessVal= Float.parseFloat(val);

        return thicknessVal;
    }

    public static Float extractAlpha(List<Structs.Property> properties) {
        String val = null;
        for(Structs.Property p: properties) {
            if (p.getKey().equals("alpha")) {
                val = p.getValue();
            }
        }
        if (val == null)
            return 1.0f;

        Float alphaVal = Float.parseFloat(val);

        return alphaVal;
    }

}
