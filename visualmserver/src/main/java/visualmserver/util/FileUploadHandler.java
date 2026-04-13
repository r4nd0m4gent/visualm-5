package visualmserver.util;

import visualmserver.exceptions.InvalidDataException;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import javax.imageio.ImageIO;

public class FileUploadHandler {
    private static String PARENT_DIR = new File(System.getProperty("user.dir")).toString();
    public static List<String> WHITELIST = Arrays.asList("jpg", "jpeg", "png");
    private static final int MAX_DIMENSION = 800;
    private static final float JPEG_QUALITY = 0.75f;

    /**
     * Creates a file from a Base64 String and writes it to the given path on the server
     *
     * @param data         - Base64 String for the file that needs to be created
     * @param path         - Path where the file needs to be saved
     * @param absolutePath - Absolute path of the file for reference
     * @return Absolute path where the file is stored
     * @throws IOException
     */
    public static String upload(String data, String path, String absolutePath) throws IOException {

        String[] dataParts = data.split(",");
        // data={type}/{extension};base64. First split the ;base64 part and then split the everything after the / to get the file extension
        String fileExtension = dataParts[0].split(";")[0].split("/")[1];

        // Check for valid data types
        if (!WHITELIST.contains(fileExtension)) {
            throw new InvalidDataException("File is not of type jpg or png");
        }

        byte[] imgBytes = resizeImage(Base64.getDecoder().decode(dataParts[1]), fileExtension);

        File dir = new File(String.format("%s%s", PARENT_DIR, path));
        String fileName = String.format("%s.%s", UUID.randomUUID().toString(), fileExtension);

        // Create directories if not created yet
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(String.format("%s/%s", dir, fileName))) {
            fos.write(imgBytes);
            fos.flush();
        }

        // return absolute path instead of relative path
        return String.format("%s%s", absolutePath, fileName);
    }

    public static String upload(String data, String path) throws IOException {
        String[] dataParts = data.split(",");
        // data={type}/{extension};base64. First split the ;base64 part and then split the everything after the / to get the file extension
        String fileExtension = dataParts[0].split(";")[0].split("/")[1];

        // Check for valid data types
        if (!WHITELIST.contains(fileExtension)) {
            throw new InvalidDataException("File is not of type jpg or png");
        }

        byte[] imgBytes = resizeImage(Base64.getDecoder().decode(dataParts[1]), fileExtension);
        File dir = new File(String.format("%s%s", PARENT_DIR, path));

        String fileName = String.format("%s.%s", UUID.randomUUID().toString(), fileExtension);

        // Create directories if not created yet
        if (!dir.exists()) {
            dir.mkdirs();
        }

        System.out.printf("%s/%s\n%s\n", dir, fileName, PARENT_DIR);

        try (FileOutputStream fos = new FileOutputStream(String.format("%s/%s", dir, fileName))) {
            fos.write(imgBytes);
            fos.flush();
        }

        // return absolute path instead of relative path
        return String.format("%s%s", path, fileName);
    }

    public static String getFileBase64(String path) throws IOException {
        File requestedFile = new File(String.format("%s%s", PARENT_DIR, path));

        if (requestedFile.exists()) {
            byte[] encoded = Files.readAllBytes(requestedFile.toPath());

            return Base64.getEncoder().encodeToString(encoded);
        }

        return null;
    }

    private static byte[] resizeImage(byte[] originalBytes, String extension) throws IOException {
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(originalBytes));
        if (original == null) {
            return originalBytes;
        }

        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= MAX_DIMENSION && height <= MAX_DIMENSION) {
            return originalBytes;
        }

        double scale = Math.min((double) MAX_DIMENSION / width, (double) MAX_DIMENSION / height);
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType() != 0 ? original.getType() : BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();

        String formatName = extension.equals("png") ? "png" : "jpg";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resized, formatName, baos);
        return baos.toByteArray();
    }

}
