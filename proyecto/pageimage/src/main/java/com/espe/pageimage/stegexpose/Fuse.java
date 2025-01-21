package com.espe.pageimage.stegexpose;

import java.util.List;
import java.util.Iterator;

public class Fuse {
    public Fuse() {
    }

    public static double se(List<Double> var0) {  // Cambié el tipo de 'var0' a List<Double>
        double var3 = 0.0;

        Double var6;
        for(Iterator<Double> var5 = var0.iterator(); var5.hasNext(); var3 += var6) {
            var6 = var5.next();
        }

        double var1 = var3 / (double)var0.size();
        return var1;
    }

    public static double seQ(double var0, double var2) {
        return var0 * var2 / 3.0;
    }
}
