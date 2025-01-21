package com.espe.pageimage.stegexpose;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

@Service
public class StegExposeService {

    private static double threshold = 0.2;
    private static boolean fast = false;
    private static ArrayList<Double> stegExposeInput;

    public void analyzeImage(MultipartFile file) {
        try {
            // Cargar la imagen desde el MultipartFile
            BufferedImage image = loadImageFromMultipartFile(file);

            // Validar si la imagen fue cargada correctamente
            if (image == null) {
                System.out.println("La imagen no es válida.");
                return;
            }

            // Inicialización de variables de análisis
            stegExposeInput = new ArrayList<>();
            double fileSize = (double) file.getSize();
            String fileName = file.getOriginalFilename();

            // Realizar el análisis de las diferentes técnicas
            double ps = analyzePrimarySets(image);
            double[] cs = analyzeChiSquare(image);
            double sp = analyzeSamplePairs(image);
            double rs = analyzeRSAnalysis(image);
            double fusion = analyzeFusion(image);

            // Imprimir resultados
            printResults(fileName, fusion, fileSize, ps, cs[0], cs[1], cs[2], rs, fusion);

        } catch (IOException e) {
            System.out.println("Error al procesar el archivo: " + e.getMessage());
        }
    }

    private BufferedImage loadImageFromMultipartFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return ImageIO.read(inputStream);
        }
    }

    private double analyzePrimarySets(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] colorCount = new int[256];
        int totalPixels = width * height;

        // Contar la cantidad de cada valor de color rojo
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int color = (image.getRGB(x, y) >> 16) & 0xFF;
                colorCount[color]++;
            }
        }

        // Calcular la entropía
        double primarySetAnalysis = 0.0;
        for (int count : colorCount) {
            if (count > 0) {
                double proportion = (double) count / totalPixels;
                primarySetAnalysis += proportion * Math.log(proportion);
            }
        }

        return -primarySetAnalysis; // Retornar el valor negativo de la entropía
    }

    private double[] analyzeChiSquare(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double[][] expected = new double[3][256];
        double[][] observed = new double[3][256];

        // Calcular valores observados
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                observed[0][(rgb >> 16) & 0xFF]++; // Rojo
                observed[1][(rgb >> 8) & 0xFF]++;  // Verde
                observed[2][rgb & 0xFF]++;         // Azul
            }
        }

        // Calcular valores esperados
        double totalPixels = width * height;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                expected[i][j] = totalPixels / 256;
            }
        }

        // Calcular Chi-Cuadrado para cada canal
        double[] chiSquare = new double[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < observed[i].length; j++) {
                if (expected[i][j] > 0) {
                    chiSquare[i] += Math.pow(observed[i][j] - expected[i][j], 2) / expected[i][j];
                }
            }
        }

        return chiSquare; // Retorna un array con los valores Chi-Cuadrado para R, G y B
    }

    private double analyzeSamplePairs(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double totalVariation = 0.0;
        int count = 0;

        // Iterar sobre la imagen en pares de píxeles
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                int color1 = (image.getRGB(x, y) >> 16) & 0xFF; // Rojo
                int color2 = (image.getRGB(x + 1, y) >> 16) & 0xFF; // Rojo
                totalVariation += Math.abs(color1 - color2);
                count++;
            }
        }

        // Calcular la media de la variación
        return count > 0 ? totalVariation / count : 0.0;
    }

    private double analyzeRSAnalysis(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        double totalVariation = 0.0;
        int count = 0;

        // Iterar sobre la imagen en bloques
        for (int y = 0; y < height - 1; y++) {
            for (int x = 0; x < width - 1; x++) {
                int[] block = new int[4];
                block[0] = image.getRGB(x, y);
                block[1] = image.getRGB(x + 1, y);
                block[2] = image.getRGB(x, y + 1);
                block[3] = image.getRGB(x + 1, y + 1);
                double variation = getVariation(block, RSAnalysis.ANALYSIS_COLOUR_RED);
                totalVariation += variation;
                count++;
            }
        }

        return count > 0 ? totalVariation / count : 0.0;
    }

    private double getVariation(int[] block, int colour) {
        double var = 0;
        int colour1, colour2;

        for (int i = 0; i < block.length; i += 2) {
            colour1 = getPixelColour(block[i], colour);
            colour2 = getPixelColour(block[i + 1], colour);
            var += Math.abs(colour1 - colour2);
        }
        return var;
    }

    public int getPixelColour(int pixel, int colour) {
        if (colour == RSAnalysis.ANALYSIS_COLOUR_RED)
            return getRed(pixel);
        else if (colour == RSAnalysis.ANALYSIS_COLOUR_GREEN)
            return getGreen(pixel);
        else if (colour == RSAnalysis.ANALYSIS_COLOUR_BLUE)
            return getBlue(pixel);
        else
            return 0;
    }

    public int getRed(int pixel) {
        return ((pixel >> 16) & 0xff);
    }

    public int getGreen(int pixel) {
        return ((pixel >> 8) & 0xff);
    }

    public int getBlue(int pixel) {
        return (pixel & 0xff);
    }

    private double analyzeFusion(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        ArrayList<Double> fusionValues = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Calcular el promedio de los colores
                double pixelFusionValue = (red + green + blue) / 3.0;
                fusionValues.add(pixelFusionValue);
            }
        }

        return Fuse.se(fusionValues);
    }

    private void printResults(String fileName, double fusion, double fileSize, double ps, double cs1, double cs2, double cs3, double rs, double fusionQ) {
        boolean isSuspicious = fusion > threshold;

        // Mensaje de salida basado en el análisis
        if (isSuspicious) {
            System.out.printf("%s es sospechosa. Cantidad aproximada de datos ocultos: %.2f bytes.%n", fileName, fusionQ);
        } else {
            System.out.printf("%s no contiene datos ocultos.%n", fileName);
        }

        // Imprimir información adicional
        System.out.printf("Tamaño del archivo: %.2f bytes%n", fileSize);
        System.out.printf("Resultados del análisis:%n");
        System.out.printf("  - Análisis de Conjuntos Primarios (PS): %.2f%n", ps);
        System.out.printf("  - Chi-Cuadrado (CS): [%.2f, %.2f, %.2f]%n", cs1, cs2, cs3);
        System.out.printf("  - Análisis RS: %.2f%n", rs);
        System.out.printf("  - Valor de fusión: %.2f%n", fusion);
    }

    // Función para analizar la imagen y determinar si tiene esteganografía.
    public static String analyzeImageForSteganography(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            SamplePairs samplePairs = new SamplePairs();
            PrimarySets primarySets = new PrimarySets(image);
            RSAnalysis rsAnalysis = new RSAnalysis(2, 2);

            double primarySetsResult = primarySets.run();
            double samplePairsRedResult = samplePairs.doAnalysis(image, SamplePairs.ANALYSIS_COLOUR_RED);
            double samplePairsGreenResult = samplePairs.doAnalysis(image, SamplePairs.ANALYSIS_COLOUR_GREEN);
            double samplePairsBlueResult = samplePairs.doAnalysis(image, SamplePairs.ANALYSIS_COLOUR_BLUE);

            double averageSamplePairs = (samplePairsRedResult + samplePairsGreenResult + samplePairsBlueResult) / 3.0;
            double fusionResult = Fuse.se(Collections.singletonList(averageSamplePairs));

            if (fusionResult > threshold) {
                return "La imagen contiene esteganografía. Nivel de ocultación estimado: " + fusionResult;
            } else {
                return "La imagen no parece contener esteganografía.";
            }

        } catch (IOException e) {
            return "Error al procesar la imagen: " + e.getMessage();
        }
    }
}
