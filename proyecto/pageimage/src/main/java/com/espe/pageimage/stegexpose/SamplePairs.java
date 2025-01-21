package com.espe.pageimage.stegexpose;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class SamplePairs extends PixelBenchmark {
    public static final int ANALYSIS_COLOUR_RED = 0;
    public static final int ANALYSIS_COLOUR_GREEN = 1;
    public static final int ANALYSIS_COLOUR_BLUE = 2;

    public SamplePairs() {
    }

    public double doAnalysis(BufferedImage var1, int var2) {
        int var3 = var1.getWidth();
        int var4 = var1.getHeight();
        boolean var5 = false;
        boolean var6 = false;
        int[] var7 = new int[2];
        long var18 = 0L;
        long var16 = 0L;
        long var14 = 0L;
        long var12 = 0L;
        long var10 = 0L;

        int var8;
        int var9;
        int var34;
        int var35;
        for(var35 = 0; var35 < var4; ++var35) {
            for(var34 = 0; var34 < var3; var34 += 2) {
                var7[0] = var1.getRGB(var34, var35);
                var7[1] = var1.getRGB(var34 + 1, var35);
                var8 = this.getPixelColour(var7[0], var2);
                var9 = this.getPixelColour(var7[1], var2);
                if (var8 >> 1 == var9 >> 1 && (var9 & 1) != (var8 & 1)) {
                    ++var18;
                }

                if (var8 == var9) {
                    ++var16;
                }

                if (var9 == var9 >> 1 << 1 && var8 < var9 || var9 != var9 >> 1 << 1 && var8 > var9) {
                    ++var12;
                }

                if (var9 == var9 >> 1 << 1 && var8 > var9 || var9 != var9 >> 1 << 1 && var8 < var9) {
                    ++var14;
                }

                ++var10;
            }
        }

        for(var35 = 0; var35 < var4; var35 += 2) {
            for(var34 = 0; var34 < var3; ++var34) {
                var7[0] = var1.getRGB(var34, var35);
                var7[1] = var1.getRGB(var34, var35 + 1);
                var8 = this.getPixelColour(var7[0], var2);
                var9 = this.getPixelColour(var7[1], var2);
                if (var8 >> 1 == var9 >> 1 && (var9 & 1) != (var8 & 1)) {
                    ++var18;
                }

                if (var8 == var9) {
                    ++var16;
                }

                if (var9 == var9 >> 1 << 1 && var8 < var9 || var9 != var9 >> 1 << 1 && var8 > var9) {
                    ++var12;
                }

                if (var9 == var9 >> 1 << 1 && var8 > var9 || var9 != var9 >> 1 << 1 && var8 < var9) {
                    ++var14;
                }

                ++var10;
            }
        }

        double var20 = 0.5 * (double)(var18 + var16);
        double var22 = (double)(2L * var12 - var10);
        double var24 = (double)(var14 - var12);
        if (var20 == 0.0) {
            double var10000 = var24 / var22;
        }

        double var28 = Math.pow(var22, 2.0) - 4.0 * var20 * var24;
        double var26;
        if (var28 >= 0.0) {
            double var30 = (-1.0 * var22 + Math.sqrt(var28)) / (2.0 * var20);
            double var32 = (-1.0 * var22 - Math.sqrt(var28)) / (2.0 * var20);
            if (Math.abs(var30) <= Math.abs(var32)) {
                var26 = var30;
            } else {
                var26 = var32;
            }
        } else {
            var26 = var24 / var22;
        }

        if (var26 == 0.0) {
            var26 = var24 / var22;
        }

        return var26;
    }

    public int getPixelColour(int var1, int var2) {
        if (var2 == 0) {
            return this.getRed(var1);
        } else if (var2 == 1) {
            return this.getGreen(var1);
        } else {
            return var2 == 2 ? this.getBlue(var1) : 0;
        }
    }

    public static void main(String[] var0) {
        if (var0.length != 1) {
            System.out.println("Usage: invisibleinktoolkit.benchmark.SamplePairs <imagefilename>");
            System.exit(1);
        }

        try {
            System.out.println("\nSample Pairs Results");
            System.out.println("--------------------");
            SamplePairs var1 = new SamplePairs();
            BufferedImage var2 = ImageIO.read(new File(var0[0]));
            double var3 = 0.0;
            double var5 = var1.doAnalysis(var2, 0);
            System.out.println("Result from red: " + var5);
            var3 += var5;
            var5 = var1.doAnalysis(var2, 1);
            System.out.println("Result from green: " + var5);
            var3 += var5;
            var5 = var1.doAnalysis(var2, 2);
            System.out.println("Result from blue: " + var5);
            var3 += var5;
            var3 /= 3.0;
            System.out.println("Average result: " + var3);
            System.out.println();
        } catch (Exception var7) {
            System.out.println("ERROR: Cannot process that image type, please try another image.");
            var7.printStackTrace();
        }

    }
}
