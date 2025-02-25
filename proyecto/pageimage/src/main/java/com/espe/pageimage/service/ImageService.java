package com.espe.pageimage.service;

import com.espe.pageimage.controller.ImageIntegrityEvaluator;
import com.espe.pageimage.model.Image;
import com.espe.pageimage.repository.ImageRepository;
import com.espe.pageimage.stegexpose.StegExposeService;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import org.apache.commons.math3.stat.inference.ChiSquareTest;
import org.apache.tika.Tika;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;


@Service
public class ImageService {


    private final ImageRepository imageRepository;

    //@Value("${image.storage.path}")
    //private String storagePath;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    public Image uploadImage(MultipartFile file) throws IOException {

        System.out.println("entropia: "+calcularEntropiaImagen(file));
        analyzeImage(file);
        if (!analyzeImage(file)) {
            throw new IllegalArgumentException("El archivo contiene posible esteganografía.");
        }

        /*if (!isValidImage(file)) {
            throw new IllegalArgumentException("El archivo contiene elementos no permitidos o es inválido.");
        }*/
        // Guardar archivo en el sistema
        // Crear el directorio si no existe
        //String storagePath1 = "D:/ESCRITORIO/septimo/softwareseguro/parcial2/frontend/image-app/src/images";
        String storagePath1 = "C:/Users/User/Documents/GitHub/Paguaypruebasmoviles/proyecto/image-app/src/images";//Cambia esta ruta según sea necesario
        //C:\Users\User\Documents\GitHub\Paguaypruebasmoviles
        //String storagePath1 = "../../../../../../../../image-app/src/images"; // Cambia esta ruta según sea necesario

        System.out.println("pasa este filtro: ");
        File destinationDir = new File(storagePath1);
        System.out.println("pasa este filtro: ");
        if (!destinationDir.exists()) {
            System.out.println("pasa este filtro: ");
            destinationDir.mkdirs(); // Crea el directorio si no existe
        }

        System.out.println(destinationDir+"pasa este filtro: ");

        // Guardar archivo en el sistema
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destination = new File(destinationDir, filename);
        file.transferTo(destination);


        // Guardar en la base de datos
        Image image = new Image();
        image.setFilename(filename);
        image.setStatus("pending");
        return imageRepository.save(image);
    }

    public List<Image> getPendingImages() {
        return imageRepository.findByStatus("pending");
    }

    public void approveImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        image.setStatus("approved");
        imageRepository.save(image);
    }

    public void rejectImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        image.setStatus("rejected");
        imageRepository.save(image);
    }

    public boolean isValidImage(MultipartFile file) {
        // Verificar que el archivo no esté vacío
        if (file.isEmpty()) {
            System.out.println("Denegado: El archivo está vacío.");
            return false;
        }

        // Verificar el tipo de contenido
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            System.out.println("Denegado: Tipo de contenido no válido - " + contentType);
            return false; // No es un tipo de imagen
        }

        // Verificar la extensión del archivo utilizando una lista blanca
        String filename = file.getOriginalFilename();
        if (filename == null || !isAllowedExtension(filename)) {
            System.out.println("Denegado: Extensión no permitida - " + filename);
            return false; // Extensión no permitida
        }

        // Verificar el tamaño del archivo
        long maxSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxSize) {
            System.out.println("Denegado: El archivo es demasiado grande - " + file.getSize() + " bytes");
            return false; // El archivo es demasiado grande
        }




        return true; // El archivo pasó todas las validaciones
    }


    private boolean hasSuspiciousSegments(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            String hex = bytesToHex(bytes);

            // Buscar patrones específicos de archivos embebidos como .rar o .zip
            if (hex.contains("504B0304") || hex.contains("52617221")) { // PK.. (ZIP) o Rar!
                return true;
            }
        } catch (IOException e) {
            return true; // Asumir sospechoso si hay un error
        }
        return false;
    }



    private long estimateImageSize(BufferedImage img, String contentType) {
        int bytesPerPixel = switch (contentType) {
            case "image/jpeg" -> 3; // JPEG usa 3 bytes por píxel (aproximadamente)
            case "image/png" -> 4; // PNG usa 4 bytes por píxel
            default -> 3; // Asumir 3 por defecto
        };
        return (long) img.getWidth() * img.getHeight() * bytesPerPixel;
    }

    private boolean isAllowedExtension(String filename) {
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};
        for (String extension : allowedExtensions) {
            if (filename.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }



    public boolean detectHiddenMessages(MultipartFile file) {
        try {
            // Leer la imagen del archivo MultipartFile
            BufferedImage img = ImageIO.read(file.getInputStream());
            if (img == null) {
                throw new IllegalArgumentException("La imagen no es válida.");
            }

            // Lógica para detectar mensajes ocultos usando LSB (Least Significant Bit)
            int width = img.getWidth();
            System.out.println(width);


            int height = img.getHeight();
            System.out.println(height);
            int lsbCount = 0;

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = img.getRGB(x, y);
                    // Extraer el bit menos significativo
                    int lsb = rgb & 1;
                    // Contar la cantidad de LSB que son 1
                    if (lsb == 1) {
                        lsbCount++;
                    }
                }
            }

            // Definir un umbral para considerar que la imagen tiene esteganografía
            return lsbCount > (width * height) * 0.03; // Por ejemplo, más del 3% de los LSB modificados
        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
            return false; // Retorna false en caso de error
        }
    }
    public boolean detectHiddenMessages2(MultipartFile file) {
        try {
            // Leer la imagen del archivo MultipartFile
            BufferedImage img = ImageIO.read(file.getInputStream());
            if (img == null) {
                throw new IllegalArgumentException("La imagen no es válida.");
            }

            // Obtener dimensiones de la imagen
            int width = img.getWidth();
            int height = img.getHeight();
            int lsbCount = 0;

            // Recorrer cada píxel de la imagen
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = img.getRGB(x, y);
                    // Extraer el bit menos significativo
                    int lsb = rgb & 1;
                    // Contar la cantidad de LSB que son 1
                    if (lsb == 1) {
                        lsbCount++;
                    }
                }
            }

            // Calcular longitud aproximada del mensaje
            int messageLength = lsbCount; // Puedes ajustar esto según tus necesidades

            // Definir umbral para considerar que la imagen tiene esteganografía
            boolean hasStego = lsbCount > (width * height) * 0.01; // Más del 3% de los LSB modificados

            // Imprimir longitud aproximada del mensaje
            if (hasStego) {
                System.out.printf("Stego, longitud aproximada del mensaje: %d%n", messageLength);
            }

            return hasStego; // Retorna si hay esteganografía
        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
            return false; // Retorna false en caso de error
        }
    }
    public void detectHiddenMessages1(MultipartFile file) {
        System.out.println("HIDDEN FUNCTION");
        try {
            // Leer la imagen del archivo MultipartFile
            BufferedImage img = ImageIO.read(file.getInputStream());
            int width = img.getWidth();
            int height = img.getHeight();
            int chunk = 1024;
            double lastPval = 0;
            // Recorremos la imagen a trozos cada vez más grandes
            for (int sz = chunk; sz < height * width; sz += chunk) {
                // Inicializar el histograma
                int[] histogram = new int[256];
                // Calcular el histograma
                for (int y = 0; y < height; y++) {
                    if (y * width > sz) {
                        break;
                    }
                    for (int x = 0; x < width; x++) {
                        int rgb = img.getRGB(x, y);
                        int curPixel = rgb & 0xFF; // Obtener el valor del píxel
                        histogram[curPixel]++;
                    }
                }
                // Obtener las observaciones y expectativas
                double[] obs = new double[128]; // Para barras pares
                double[] exp = new double[128]; // Para barras impares
                int index = 0;
                for (int y = 1; y < histogram.length; y += 2) {
                    int x = histogram[y - 1];
                    int z = (histogram[y - 1] + histogram[y]) / 2;
                    if (x > 0 && z > 0) {
                        obs[index] = x;
                        exp[index] = z;
                        index++;
                    }
                }

                // Calcular chi cuadrado y p-valor
                double chiSquare = 0;
                for (int i = 0; i < index; i++) {
                    chiSquare += Math.pow(obs[i] - exp[i], 2) / exp[i];
                }
                double pval = 1 - chiSquare; // Simplificación para ilustrar; deberías calcular esto correctamente

                //System.out.println("pval"+pval);
                if (pval <= 0.01) {
                    if (lastPval == 0) {
                        System.out.println("Cover");
                        return;
                    } else {
                        System.out.println(lastPval);
                        break;
                    }
                }

                lastPval = pval;
            }

            System.out.printf("Stego, longitud aproximada del mensaje: %d%n", chunk);
        } catch (IOException e) {
            System.out.println("Error al procesar la imagen: " + e.getMessage());
        }
    }
    public static void printUniqueColors(MultipartFile file) throws IOException {
        // Leemos la imagen del MultipartFile
        System.out.println("COLOR FUNCTION");
        BufferedImage image = ImageIO.read(file.getInputStream());
        int width = image.getWidth();
        int height = image.getHeight();

        // Mapa para guardar los colores y su conteo
        Map<Integer, Integer> colors = new HashMap<>();

        // Recorremos la imagen guardando los colores diferentes
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                colors.put(pixel, colors.getOrDefault(pixel, 0) + 1);
            }
        }

        // Imprimimos el número de colores únicos
        System.out.println("Número de colores únicos: " + colors.size()+" "+(width*height));
    }
    public static boolean analyzeImage(MultipartFile file) throws IOException {
        // Cargar la imagen desde el MultipartFile
        System.out.println("ESTEGO FUNCTION VALIDA"+ file.getName());
        BufferedImage img = ImageIO.read(file.getInputStream());
        int width = img.getWidth();
        int height = img.getHeight();

        int x = 0, y = 0, k = 0;

        // Recorrer los píxeles de la imagen
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width - 1; i++) {
                int r = img.getRGB(i, j) & 0xFF; // Valor del píxel actual
                int s = img.getRGB(i + 1, j) & 0xFF; // Valor del píxel de la derecha

                if ((s % 2 == 0 && r < s) || (s % 2 == 1 && r > s)) {
                    x++;
                }
                if ((s % 2 == 0 && r > s) || (s % 2 == 1 && r < s)) {
                    y++;
                }
                if (Math.round(s / 2.0) == Math.round(r / 2.0)) {
                    k++;
                }
            }
        }
        // Verificación de k
        if (k == 0) {
            System.out.println("SPA failed");
            return true;
        }
        // Cálculo de a, b, c
        int a = 2 * k;
        int b = 2 * (2 * x - width * (height - 1));
        int c = y - x;

        // Cálculo de las raíces
        double discriminant = b * b - 4 * a * c;
        if (discriminant < 0) {
            System.out.println("No valid solutions.");
            return true;
        }

        double bp = (-b + Math.sqrt(discriminant)) / (2 * a);
        double bm = (-b - Math.sqrt(discriminant)) / (2 * a);

        double beta = Math.min(bp, bm);
        if (beta > 0.05) {
            System.out.println("stego valor: ");
            System.out.printf("Estimated embedding rate: %f%n", beta);
            return false;
        } else {
            System.out.println("cover");
            return true;
        }

    }
    public void checkHiddenData(MultipartFile file) throws IOException {
        // Convertir el archivo MultipartFile a BufferedImage
        System.out.println("CHECKHIDDEENDATA");
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            System.out.println("Error al leer la imagen. Verifica que el archivo sea una imagen válida.");
            return;
        }

        Color firstCol = new Color(image.getRGB(0, 0));
        int embeddedBitsPerPixel = 1 << (((firstCol.getRed() & 0x1) << 1) + (firstCol.getBlue() & 0x1));

        Integer fileNameLength = null;
        List<Byte> byteList = new ArrayList<>();
        byte currentByte = 0;
        int bitIndex = 0;

        // Recorrer los píxeles de la imagen
        outer:
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = (y == 0 ? 1 : 0); x < image.getWidth(); x++) {
                Color baseCol = new Color(image.getRGB(x, y));
                //System.out.println("color red"+baseCol.getRGB()+"red"+baseCol.getRed()+"green "+baseCol.getGreen()+" "+image.getHeight());
                int[] pixelRGB = new int[]{baseCol.getRed(), baseCol.getGreen(), baseCol.getBlue()};

                for (int c = 0; c < 3; c++) { // 3 canales de color RGB
                    currentByte <<= embeddedBitsPerPixel;
                    currentByte += pixelRGB[c] & numOfBitsToMask(embeddedBitsPerPixel);
                    bitIndex += embeddedBitsPerPixel;

                    // Completar el byte
                    if (bitIndex >= 8) {
                        bitIndex = 0;
                        byteList.add(currentByte);
                        currentByte = 0;

                        if (fileNameLength == null) {
                            // Intentar leer el tamaño del nombre del archivo
                            if (byteList.size() >= 1) {
                                fileNameLength = byteList.get(0) < 0
                                        ? byteList.get(0) + 256
                                        : Integer.valueOf(byteList.get(0));
                                byteList.clear();
                            }
                        } else {
                            // Si se ha leído el tamaño del nombre, buscar más datos
                            if (fileNameLength != null && byteList.size() >= fileNameLength) {
                                // Encontramos un nombre de archivo
                                System.out.println("Se encontraron datos ocultos en la imagen.");
                                return;
                            }
                        }
                    }
                }
            }
        }
        //String contentType = file.getContentType();
        //System.out.println("Tipo de contenido: " + contentType);

        // Si no encontramos datos ocultos
        System.out.println("No se encontraron datos ocultos en la imagen.");
    }


    // Método auxiliar para obtener la máscara de bits
    private int numOfBitsToMask(int numBits) {
        return (1 << numBits) - 1;
    }
    //FUNCIO ESTEGO


    // Método para extraer el mensaje de la imagen
    public String extractMessage(File imageFile) throws IOException, ImageReadException {
        BufferedImage image = Imaging.getBufferedImage(imageFile);
        int width = image.getWidth();
        int height = image.getHeight();
        StringBuilder message = new StringBuilder();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Recuperar los bits menos significativos del color
                char ch = (char) ((r & 0x01) << 7 | ((g & 0x01) << 6) | ((b & 0x01) << 5));

                message.append(ch);
            }
        }
        return message.toString();
    }




    private static boolean containsHiddenData(byte[] imageBytes) {
        // Usa un conjunto para almacenar valores únicos
        Set<Byte> uniqueBytes = new HashSet<>();
        for (byte b : imageBytes) {
            uniqueBytes.add(b);
        }

        // Calcula el ratio de unicidad
        double uniquenessRatio = (double) uniqueBytes.size() / imageBytes.length;

        // Si el ratio es demasiado bajo, podría haber datos ocultos
        return uniquenessRatio < 0.7;
    }
    public static void extractData(MultipartFile file, int length) {
        System.out.println("EXTRACT DATA......");
        try {
            // Convertir MultipartFile a BufferedImage
            BufferedImage image = ImageIO.read(file.getInputStream());
            System.out.print("Extrayendo datos: ");
            int bitMask = 0x00000001; // Definir la máscara de bits para obtener el dígito
            int x = 0;                // Coordenada x inicial
            int y = 0;                // Coordenada y inicial
            int flag;
            int[] data = new int[length]; // Arreglo para almacenar los datos extraídos

            for (int i = 0; i < length; i++) {
                int bit = 0;

                // 8 bits forman un byte
                for (int j = 0; j < 8; j++) {
                    if (x < image.getWidth()) {
                        flag = image.getRGB(x, y) & bitMask; // Obtener el último dígito del píxel
                        x++;
                    } else {
                        x = 0;
                        y++;
                        flag = image.getRGB(x, y) & bitMask; // Obtener el último dígito del píxel
                    }

                    // Almacenar los bits extraídos en un entero
                    bit = (bit >> 1) | (flag == 1 ? 0x80 : 0);
                }
                data[i] = bit; // Almacenar el byte extraído
                System.out.print(data[i] + " "); // Imprimir el byte en formato decimal
            }
            System.out.println(); // Nueva línea al final
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    public static void checkHiddenData222(MultipartFile file) {
        System.out.println("CHECK 222");
        try {
            // Convertir MultipartFile a BufferedImage
            BufferedImage image = ImageIO.read(file.getInputStream());
            boolean hasHiddenData = false;

            // Verificar si hay datos ocultos (puedes ajustar esta lógica según tu implementación)
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    // Aquí se puede definir la lógica para determinar si hay datos ocultos
                    // Por ejemplo, si el bit menos significativo es 1, se considera que hay datos ocultos
                    if ((rgb & 0x00000001) == 1) {
                        hasHiddenData = true;
                        break;
                    }
                }
                if (hasHiddenData) break;
            }

            // Imprimir mensaje según la detección de datos ocultos
            if (hasHiddenData) {
                System.out.println("¡Se han encontrado datos ocultos en la imagen!");
            } else {
                System.out.println("No se encontraron datos ocultos en la imagen.");
            }

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
    }
    public String extractText(MultipartFile file) {
        StringBuilder extractedText = new StringBuilder();

        try {
            // Cargar la imagen desde el MultipartFile
            BufferedImage image = ImageIO.read(file.getInputStream());

            // Verificar que la imagen no sea nula
            if (image == null) {
                System.out.println("El archivo no es una imagen válida.");
                return null;
            }

            // Lógica para extraer texto utilizando el bit menos significativo
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    // Obtener el bit menos significativo del componente azul
                    int lsb = rgb & 0x00000001; // Extraer el LSB
                    // Agregar el LSB a la cadena de texto extraído
                    extractedText.append(lsb);
                }
            }

            // Convertir el texto binario a texto legible
            return bitsToText(extractedText.toString());

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }

        return extractedText.toString(); // Retornar el texto extraído
    }

    private String bitsToText(String bits) {
        StringBuilder text = new StringBuilder();

        // Agrupar los bits en bloques de 8
        for (int i = 0; i < bits.length(); i += 8) {
            String byteString = bits.substring(i, Math.min(i + 8, bits.length()));
            if (byteString.length() < 8) break; // Evitar bloques incompletos
            int charCode = Integer.parseInt(byteString, 2); // Convertir de binario a decimal
            text.append((char) charCode); // Convertir de decimal a carácter
        }

        return text.toString(); // Retornar el texto convertido
    }
    public static double calcularEntropiaImagen(MultipartFile archivo) throws IOException {
        // Obtener los bytes del archivo
        byte[] datosImagen = archivo.getBytes();

        // Map para contar las frecuencias de cada byte
        Map<Byte, Integer> frecuenciaBytes = new HashMap<>();

        for (byte b : datosImagen) {
            frecuenciaBytes.put(b, frecuenciaBytes.getOrDefault(b, 0) + 1);
        }

        // Calcular la entropía
        int totalBytes = datosImagen.length;
        double entropia = 0.0;

        for (Map.Entry<Byte, Integer> entrada : frecuenciaBytes.entrySet()) {
            double probabilidad = (double) entrada.getValue() / totalBytes;
            entropia += -probabilidad * (Math.log(probabilidad) / Math.log(2));
        }

        return entropia;
    }


    public static boolean hasSteganography(MultipartFile file) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                System.out.println("El archivo no es una imagen válida.");
                return false;
            }

            int width = image.getWidth();
            int height = image.getHeight();

            int totalPixels = width * height;
            int totalLSBOnes = 0;

            // Contar cuántos bits menos significativos son 1
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = image.getRGB(x, y);
                    int blue = rgb & 0xFF;
                    int lsb = blue & 1; // Extraer el LSB
                    totalLSBOnes += lsb;
                }
            }

            // Calcular la proporción de LSB que son 1
            double proportionLSBOnes = (double) totalLSBOnes / totalPixels;

            // Evaluar si la proporción es anómala
            // En una imagen normal, la proporción debería estar cerca de 0.5
            if (Math.abs(proportionLSBOnes - 0.5) > 0.05) {
                System.out.println("La imagen tiene indicios de esteganografía.");
                return true;
            } else {
                System.out.println("La imagen no tiene esteganografía detectable.");
                return false;
            }

        } catch (IOException e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            return false;
        }
    }
    public void detectSteganography(MultipartFile file) throws IOException {
        System.out.println("CHI CUADRADO");
        // Leer la imagen
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida.");
        }

        System.out.println("CHI CUADRADO 2");
        int width = image.getWidth();
        int height = image.getHeight();

        // Crear histograma de 256 niveles para el canal azul
        long[] histogram = new long[256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int blue = pixel & 0xFF; // Extraer el canal azul
                histogram[blue]++;
            }
        }
        System.out.println("CHI CUADRADO 3");

        // Calcular los valores observados (pares) y esperados (pares + impares)
        long[] observed = new long[128];
        double[] expected = new double[128];
        for (int i = 0; i < 128; i++) {
            observed[i] = histogram[2 * i] + histogram[2 * i + 1]; // Sumar valores pares y impares

            // Asegurarnos de que los valores esperados no sean cero
            expected[i] = (histogram[2 * i] + histogram[2 * i + 1]) / 2.0;

            // Si el valor esperado es cero o extremadamente pequeño, asignar un valor mínimo positivo
            if (expected[i] <= 0.0001) {
                expected[i] = 0.0001; // Esto previene la división por cero en el test Chi-Cuadrado
            }
        }

        System.out.println("CHI CUADRADO 4");
        // Calcular Chi-Square
        ChiSquareTest chiSquareTest = new ChiSquareTest();
        double pValue = chiSquareTest.chiSquareTest(expected, observed);

        // Resultado basado en el p-value
        if (pValue <= 0.01) {
            System.out.println("La imagen contiene esteganografía.");
        } else {
            System.out.println("La imagen no contiene esteganografía.");
        }
    }
    public void analyzeStegoImage(MultipartFile file) throws IOException{

        System.out.println("ANALISIS ESTEGO IMAGE.");
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IllegalArgumentException("El archivo no es una imagen válida.");
        }

            int width = image.getWidth();
            int height = image.getHeight();
            int[] histogram = new int[256];
            int totalPixels = width * height;

        System.out.println("ANALISIS ESTEGO IMAGE.");
            // Calcular histograma
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int pixel = image.getRGB(x, y);
                    int red = (pixel >> 16) & 0xff;
                    histogram[red]++;
                }
            }

            // Análisis de resultados
            System.out.println("Resultados del análisis de esteganografía:");
            for (int i = 0; i < histogram.length; i++) {
                if (histogram[i] > 0) {
                    double frequency = (double) histogram[i] / totalPixels;
                    System.out.printf("Valor de intensidad %d: %d veces (Frecuencia: %.2f%%)%n", i, histogram[i], frequency * 100);
                }
            }

            // Detección simple de anomalías
            // (Esto es solo un ejemplo, se pueden agregar algoritmos más complejos)
            boolean isSteganographic = detectAnomalies(histogram);
            if (isSteganographic) {
                System.out.println("Se ha detectado posible esteganografía en la imagen.");
            } else {
                System.out.println("No se detectó esteganografía en la imagen.");
            }


    }

    private boolean detectAnomalies(int[] histogram) {
        double threshold = 0.1; // Umbral del 10%
        int totalPixels = 0;
        for (int value : histogram) {
            totalPixels += value;
        }

        for (int i = 0; i < histogram.length; i++) {
            double frequency = (double) histogram[i] / totalPixels;
            if (frequency > threshold) {
                return true; // Se detecta una anomalía
            }
        }
        return false; // No se detecta anomalía
    }
    public static int calculateHistogramDifference(MultipartFile file) throws IOException {
        // Convertir MultipartFile a BufferedImage
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("El archivo no es una imagen válida.");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        // Crear el histograma (256 niveles de gris para escala de grises)
        int[] histogram = new int[256];
        Arrays.fill(histogram, 0);

        // Procesar cada píxel de la imagen
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int gray = (rgb >> 16) & 0xFF; // Extraer el valor de intensidad (componente rojo)
                histogram[gray]++;
            }
        }

        // Calcular las diferencias entre barras consecutivas
        int totalDifference = 0;
        for (int i = 1; i < histogram.length; i += 2) {

            int diff = Math.abs(Math.abs(histogram[i - 1]) - Math.abs(histogram[i]));
            totalDifference += diff;
        }


        int totalPixels=width*height;
        System.out.println(totalPixels);
        if(isHigh(totalDifference,totalPixels)){
            System.out.println("tiene esteganografia");
        }
        return totalDifference;
    }

    public static boolean isHigh(int totalDifference, int totalPixels) {
        // Considerar alto si la diferencia total es mayor al 20% del total de píxeles

        double threshold = 0.2 * totalPixels;
        return totalDifference > threshold;
    }


}
