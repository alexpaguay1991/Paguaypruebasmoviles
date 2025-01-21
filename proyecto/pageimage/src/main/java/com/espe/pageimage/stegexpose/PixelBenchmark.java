package com.espe.pageimage.stegexpose;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class PixelBenchmark {
    public PixelBenchmark() {
    }

    public int getRed(int var1) {
        return var1 >> 16 & 255;
    }

    public int getGreen(int var1) {
        return var1 >> 8 & 255;
    }

    public int getBlue(int var1) {
        return var1 & 255;
    }
}
