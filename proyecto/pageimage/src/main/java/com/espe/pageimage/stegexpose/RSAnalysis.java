package com.espe.pageimage.stegexpose;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import javax.imageio.ImageIO;

public class RSAnalysis extends PixelBenchmark {
    public static final int ANALYSIS_COLOUR_RED = 0;
    public static final int ANALYSIS_COLOUR_GREEN = 1;
    public static final int ANALYSIS_COLOUR_BLUE = 2;
    private int[][] mMask;
    private int mM;
    private int mN;

    public RSAnalysis(int var1, int var2) {
        this.mMask = new int[2][var1 * var2];
        int var3 = 0;

        for(int var4 = 0; var4 < var2; ++var4) {
            for(int var5 = 0; var5 < var1; ++var5) {
                if ((var5 % 2 != 0 || var4 % 2 != 0) && (var5 % 2 != 1 || var4 % 2 != 1)) {
                    this.mMask[0][var3] = 0;
                    this.mMask[1][var3] = 1;
                } else {
                    this.mMask[0][var3] = 1;
                    this.mMask[1][var3] = 0;
                }

                ++var3;
            }
        }

        this.mM = var1;
        this.mN = var2;
    }

    public double[] doAnalysis(BufferedImage var1, int var2, boolean var3) {
        int var4 = var1.getWidth();
        int var5 = var1.getHeight();
        int var6 = 0;
        int var7 = 0;
        int[] var8 = new int[this.mM * this.mN];
        double var9 = 0.0;
        double var11 = 0.0;
        double var13 = 0.0;
        double var15 = 0.0;
        double var17 = 0.0;
        double var19 = 0.0;

        while(var6 < var4 && var7 < var5) {
            for(int var27 = 0; var27 < 2; ++var27) {
                int var28 = 0;

                for(int var29 = 0; var29 < this.mN; ++var29) {
                    for(int var30 = 0; var30 < this.mM; ++var30) {
                        var8[var28] = var1.getRGB(var6 + var30, var7 + var29);
                        ++var28;
                    }
                }

                double var21 = this.getVariation(var8, var2);
                var8 = this.flipBlock(var8, this.mMask[var27]);
                double var23 = this.getVariation(var8, var2);
                var8 = this.flipBlock(var8, this.mMask[var27]);
                this.mMask[var27] = this.invertMask(this.mMask[var27]);
                double var25 = this.getNegativeVariation(var8, var2, this.mMask[var27]);
                this.mMask[var27] = this.invertMask(this.mMask[var27]);
                if (var23 > var21) {
                    ++var9;
                }

                if (var23 < var21) {
                    ++var11;
                }

                if (var23 == var21) {
                    ++var17;
                }

                if (var25 > var21) {
                    ++var13;
                }

                if (var25 < var21) {
                    ++var15;
                }

                if (var25 == var21) {
                    ++var19;
                }
            }

            if (var3) {
                ++var6;
            } else {
                var6 += this.mM;
            }

            if (var6 >= var4 - 1) {
                var6 = 0;
                if (var3) {
                    ++var7;
                } else {
                    var7 += this.mN;
                }
            }

            if (var7 >= var5 - 1) {
                break;
            }
        }

        double var37 = var9 + var11 + var17;
        double[] var38 = this.getAllPixelFlips(var1, var2, var3);
        double var39 = this.getX(var9, var13, var38[0], var38[2], var11, var15, var38[1], var38[3]);
        double var32;
        if (2.0 * (var39 - 1.0) == 0.0) {
            var32 = 0.0;
        } else {
            var32 = Math.abs(var39 / (2.0 * (var39 - 1.0)));
        }

        double var34;
        if (var39 - 0.5 == 0.0) {
            var34 = 0.0;
        } else {
            var34 = Math.abs(var39 / (var39 - 0.5));
        }

        double[] var36 = new double[28];
        var36[0] = var9;
        var36[1] = var11;
        var36[2] = var13;
        var36[3] = var15;
        var36[4] = Math.abs(var9 - var13);
        var36[5] = Math.abs(var11 - var15);
        var36[6] = var9 / var37 * 100.0;
        var36[7] = var11 / var37 * 100.0;
        var36[8] = var13 / var37 * 100.0;
        var36[9] = var15 / var37 * 100.0;
        var36[10] = var36[4] / var37 * 100.0;
        var36[11] = var36[5] / var37 * 100.0;
        var36[12] = var38[0];
        var36[13] = var38[1];
        var36[14] = var38[2];
        var36[15] = var38[3];
        var36[16] = Math.abs(var38[0] - var38[1]);
        var36[17] = Math.abs(var38[2] - var38[3]);
        var36[18] = var38[0] / var37 * 100.0;
        var36[19] = var38[1] / var37 * 100.0;
        var36[20] = var38[2] / var37 * 100.0;
        var36[21] = var38[3] / var37 * 100.0;
        var36[22] = var36[16] / var37 * 100.0;
        var36[23] = var36[17] / var37 * 100.0;
        var36[24] = var37;
        var36[25] = var32;
        var36[26] = var34;
        var36[27] = (double)(var4 * var5 * 3) * var34 / 8.0;
        return var36;
    }

    private double getX(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
        double var17 = 0.0;
        double var19 = var1 - var9;
        double var21 = var3 - var11;
        double var23 = var5 - var13;
        double var25 = var7 - var15;
        double var27 = 2.0 * (var23 + var19);
        double var29 = var21 - var25 - var23 - 3.0 * var19;
        double var31 = var19 - var21;
        if (var27 == 0.0) {
            double var10000 = var31 / var29;
        }

        double var33 = Math.pow(var29, 2.0) - 4.0 * var27 * var31;
        double var35;
        double var37;
        if (var33 >= 0.0) {
            var35 = (-1.0 * var29 + Math.sqrt(var33)) / (2.0 * var27);
            var37 = (-1.0 * var29 - Math.sqrt(var33)) / (2.0 * var27);
            if (Math.abs(var35) <= Math.abs(var37)) {
                var17 = var35;
            } else {
                var17 = var37;
            }
        } else {
            var35 = (var3 - var1) / (var5 - var1 + var3 - var7);
            var37 = (var11 - var9) / (var13 - var9 + var11 - var15);
            var17 = (var35 + var37) / 2.0;
        }

        if (var17 == 0.0) {
            var35 = (var7 - var5 + var1 - var3 + (var3 - var1) / var17) / (var17 - 1.0);
            var37 = (var15 - var13 + var9 - var11 + (var11 - var9) / var17) / (var17 - 1.0);
            if (var37 > 0.0 | var35 < 0.0) {
                double var39 = (var3 - var1) / (var5 - var1 + var3 - var7);
                double var41 = (var11 - var9) / (var13 - var9 + var11 - var15);
                var17 = (var39 + var41) / 2.0;
            }
        }

        return var17;
    }

    private double[] getAllPixelFlips(BufferedImage var1, int var2, boolean var3) {
        int[] var4 = new int[this.mM * this.mN];

        int var5;
        for(var5 = 0; var5 < var4.length; ++var5) {
            var4[var5] = 1;
        }

        var5 = var1.getWidth();
        int var6 = var1.getHeight();
        int var7 = 0;
        int var8 = 0;
        int[] var9 = new int[this.mM * this.mN];
        double var10 = 0.0;
        double var12 = 0.0;
        double var14 = 0.0;
        double var16 = 0.0;
        double var18 = 0.0;
        double var20 = 0.0;

        while(var7 < var5 && var8 < var6) {
            for(int var28 = 0; var28 < 2; ++var28) {
                int var29 = 0;

                for(int var30 = 0; var30 < this.mN; ++var30) {
                    for(int var31 = 0; var31 < this.mM; ++var31) {
                        var9[var29] = var1.getRGB(var7 + var31, var8 + var30);
                        ++var29;
                    }
                }

                var9 = this.flipBlock(var9, var4);
                double var22 = this.getVariation(var9, var2);
                var9 = this.flipBlock(var9, this.mMask[var28]);
                double var24 = this.getVariation(var9, var2);
                var9 = this.flipBlock(var9, this.mMask[var28]);
                this.mMask[var28] = this.invertMask(this.mMask[var28]);
                double var26 = this.getNegativeVariation(var9, var2, this.mMask[var28]);
                this.mMask[var28] = this.invertMask(this.mMask[var28]);
                if (var24 > var22) {
                    ++var10;
                }

                if (var24 < var22) {
                    ++var12;
                }

                if (var24 == var22) {
                    ++var18;
                }

                if (var26 > var22) {
                    ++var14;
                }

                if (var26 < var22) {
                    ++var16;
                }

                if (var26 == var22) {
                    ++var20;
                }
            }

            if (var3) {
                ++var7;
            } else {
                var7 += this.mM;
            }

            if (var7 >= var5 - 1) {
                var7 = 0;
                if (var3) {
                    ++var8;
                } else {
                    var8 += this.mN;
                }
            }

            if (var8 >= var6 - 1) {
                break;
            }
        }

        double[] var32 = new double[]{var10, var12, var14, var16};
        return var32;
    }

    public Enumeration getResultNames() {
        Vector var1 = new Vector(28);
        var1.add("Number of regular groups (positive)");
        var1.add("Number of singular groups (positive)");
        var1.add("Number of regular groups (negative)");
        var1.add("Number of singular groups (negative)");
        var1.add("Difference for regular groups");
        var1.add("Difference for singular groups");
        var1.add("Percentage of regular groups (positive)");
        var1.add("Percentage of singular groups (positive)");
        var1.add("Percentage of regular groups (negative)");
        var1.add("Percentage of singular groups (negative)");
        var1.add("Difference for regular groups %");
        var1.add("Difference for singular groups %");
        var1.add("Number of regular groups (positive for all flipped)");
        var1.add("Number of singular groups (positive for all flipped)");
        var1.add("Number of regular groups (negative for all flipped)");
        var1.add("Number of singular groups (negative for all flipped)");
        var1.add("Difference for regular groups (all flipped)");
        var1.add("Difference for singular groups (all flipped)");
        var1.add("Percentage of regular groups (positive for all flipped)");
        var1.add("Percentage of singular groups (positive for all flipped)");
        var1.add("Percentage of regular groups (negative for all flipped)");
        var1.add("Percentage of singular groups (negative for all flipped)");
        var1.add("Difference for regular groups (all flipped) %");
        var1.add("Difference for singular groups (all flipped) %");
        var1.add("Total number of groups");
        var1.add("Estimated percent of flipped pixels");
        var1.add("Estimated message length (in percent of pixels)(p)");
        var1.add("Estimated message length (in bytes)");
        return var1.elements();
    }

    private double getVariation(int[] var1, int var2) {
        double var3 = 0.0;

        for(int var7 = 0; var7 < var1.length; var7 += 4) {
            int var5 = this.getPixelColour(var1[0 + var7], var2);
            int var6 = this.getPixelColour(var1[1 + var7], var2);
            var3 += (double)Math.abs(var5 - var6);
            var5 = this.getPixelColour(var1[3 + var7], var2);
            var6 = this.getPixelColour(var1[2 + var7], var2);
            var3 += (double)Math.abs(var5 - var6);
            var5 = this.getPixelColour(var1[1 + var7], var2);
            var6 = this.getPixelColour(var1[3 + var7], var2);
            var3 += (double)Math.abs(var5 - var6);
            var5 = this.getPixelColour(var1[2 + var7], var2);
            var6 = this.getPixelColour(var1[0 + var7], var2);
            var3 += (double)Math.abs(var5 - var6);
        }

        return var3;
    }

    private double getNegativeVariation(int[] var1, int var2, int[] var3) {
        double var4 = 0.0;

        for(int var8 = 0; var8 < var1.length; var8 += 4) {
            int var6 = this.getPixelColour(var1[0 + var8], var2);
            int var7 = this.getPixelColour(var1[1 + var8], var2);
            if (var3[0 + var8] == -1) {
                var6 = this.invertLSB(var6);
            }

            if (var3[1 + var8] == -1) {
                var7 = this.invertLSB(var7);
            }

            var4 += (double)Math.abs(var6 - var7);
            var6 = this.getPixelColour(var1[1 + var8], var2);
            var7 = this.getPixelColour(var1[3 + var8], var2);
            if (var3[1 + var8] == -1) {
                var6 = this.invertLSB(var6);
            }

            if (var3[3 + var8] == -1) {
                var7 = this.invertLSB(var7);
            }

            var4 += (double)Math.abs(var6 - var7);
            var6 = this.getPixelColour(var1[3 + var8], var2);
            var7 = this.getPixelColour(var1[2 + var8], var2);
            if (var3[3 + var8] == -1) {
                var6 = this.invertLSB(var6);
            }

            if (var3[2 + var8] == -1) {
                var7 = this.invertLSB(var7);
            }

            var4 += (double)Math.abs(var6 - var7);
            var6 = this.getPixelColour(var1[2 + var8], var2);
            var7 = this.getPixelColour(var1[0 + var8], var2);
            if (var3[2 + var8] == -1) {
                var6 = this.invertLSB(var6);
            }

            if (var3[0 + var8] == -1) {
                var7 = this.invertLSB(var7);
            }

            var4 += (double)Math.abs(var6 - var7);
        }

        return var4;
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

    private int[] flipBlock(int[] var1, int[] var2) {
        for(int var3 = 0; var3 < var1.length; ++var3) {
            int var4;
            int var5;
            int var6;
            int var7;
            if (var2[var3] == 1) {
                var4 = this.getRed(var1[var3]);
                var5 = this.getGreen(var1[var3]);
                var6 = this.getBlue(var1[var3]);
                var4 = this.negateLSB(var4);
                var5 = this.negateLSB(var5);
                var6 = this.negateLSB(var6);
                var7 = -16777216 | (var4 & 255) << 16 | (var5 & 255) << 8 | var6 & 255;
                var1[var3] = var7;
            } else if (var2[var3] == -1) {
                var4 = this.getRed(var1[var3]);
                var5 = this.getGreen(var1[var3]);
                var6 = this.getBlue(var1[var3]);
                var4 = this.invertLSB(var4);
                var5 = this.invertLSB(var5);
                var6 = this.invertLSB(var6);
                var7 = -16777216 | (var4 & 255) << 16 | (var5 & 255) << 8 | var6 & 255;
                var1[var3] = var7;
            }
        }

        return var1;
    }

    private int negateLSB(int var1) {
        int var2 = var1 & 254;
        return var2 == var1 ? var1 | 1 : var2;
    }

    private int invertLSB(int var1) {
        if (var1 == 255) {
            return 256;
        } else {
            return var1 == 256 ? 255 : this.negateLSB(var1 + 1) - 1;
        }
    }

    private int[] invertMask(int[] var1) {
        for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2] *= -1;
        }

        return var1;
    }

    public static void main(String[] var0) {
        if (var0.length != 1) {
            System.out.println("Usage: invisibleinktoolkit.benchmark.RSAnalysis <imagefilename>");
            System.exit(1);
        }

        try {
            System.out.println("\nRS Analysis results");
            System.out.println("-------------------");
            RSAnalysis var1 = new RSAnalysis(2, 2);
            BufferedImage var2 = ImageIO.read(new File(var0[0]));
            double var3 = 0.0;
            double[] var5 = var1.doAnalysis(var2, 0, true);
            System.out.println("Result from red: " + var5[26]);
            var3 += var5[26];
            var5 = var1.doAnalysis(var2, 1, true);
            System.out.println("Result from green: " + var5[26]);
            var3 += var5[26];
            var5 = var1.doAnalysis(var2, 2, true);
            System.out.println("Result from blue: " + var5[26]);
            var3 += var5[26];
            var3 /= 3.0;
            System.out.println("Average result: " + var3);
            System.out.println();
        } catch (Exception var6) {
            System.out.println("ERROR: Cannot process that image type, please try another image.");
            var6.printStackTrace();
        }

    }
}
