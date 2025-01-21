package com.espe.pageimage.stegexpose;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.Color;
import java.awt.image.BufferedImage;

public class PrimarySets {
    private BufferedImage image;
    private double result;

    public PrimarySets(BufferedImage var1) {
        this.image = var1;
    }

    public double run() {
        int var1 = this.image.getWidth();
        int var2 = this.image.getHeight();
        int var9 = 0;
        int var10 = 0;
        int var11 = 0;
        int var12 = 0;
        int var13 = 0;
        int var14 = 0;

        for(int var27 = 0; var27 < var2; ++var27) {
            for(int var28 = 0; var28 < var1; var28 += 2) {
                if (var28 + 1 < var1) {
                    int var3 = (new Color(this.image.getRGB(var28, var27))).getRed();
                    int var6 = (new Color(this.image.getRGB(var28 + 1, var27))).getRed();
                    ++var9;
                    if (((var6 & 1) != 0 || var3 >= var6) && ((var6 & 1) != 1 || var3 <= var6)) {
                        if (((var6 & 1) != 0 || var3 <= var6) && ((var6 & 1) != 1 || var3 >= var6)) {
                            if (var3 == var6) {
                                ++var14;
                            }
                        } else {
                            ++var11;
                            if (((var3 & 1) != 0 || (var6 & 1) != 1) && ((var3 & 1) != 1 || (var6 & 0) != 0)) {
                                ++var12;
                            } else {
                                ++var13;
                            }
                        }
                    } else {
                        ++var10;
                    }
                }
            }
        }

        double var15 = 0.5 * (double)(var13 + var14);
        double var17 = (double)(2 * var10 - var9);
        double var19 = (double)(var11 - var10);
        double var21 = Math.pow(var17, 2.0) - 4.0 * var15 * var19;
        double var23 = (-var17 + Math.sqrt(var21)) / (2.0 * var15);
        double var25 = (-var17 - Math.sqrt(var21)) / (2.0 * var15);
        if (var23 < var25) {
            this.result = var23;
        } else {
            this.result = var25;
        }

        return var15;
    }

    public double getResult() {
        return this.result;
    }
}
