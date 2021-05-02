package src;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Main {

    /**
     * Concatenates pdfs in the order they were passed as command line arguments.
     *
     * @param args paths to pdf files to be merged, in order.
     */
    public static void main(String[] args) {
        if (args.length < 1) { // do nothing if no arguments given
            System.out.println("Must specify more than one file or a directory.");

        } else if (args.length == 1) { // if only one argument
            String argument = args[0];
            File file = new File(argument);

            if (!file.exists()) { // checks if file exists
                System.out.println("File " + argument + " does not exist.");

            } else if (file.isDirectory()) { // if file is a directory it merges all pdfs in directory
                File[] documents = getPdfsArrayFromDirectory(argument);
                if (documents.length < 2) { // does not merge if there are less than 2 pdfs in directory
                    System.out.println("There are no pdfs to merge in this directory.");
                } else {
                    mergePdfs(documents);
                }

            } else {
                System.out.println("You need to specify more than one file or a directory.");
            }
        } else {
            File[] files = new File[args.length];
            for (int i = 0; i < args.length; i++) {
                files[i] = new File(args[i]);
            }
            mergePdfs(files);
        }
    }

    /**
     * Adds all pdfs from directory to a File array and returns the array, sorted lexicographically.
     *
     * @param path path to directory
     * @return array of all pdfs in directory
     */
    private static File[] getPdfsArrayFromDirectory(String path) {
        File[] files = new File(path).listFiles(pathname -> {
            // accepts file only if its path (converted to lowercase) contains ".pdf"
            return pathname.getName().toLowerCase().contains(".pdf");
        });
        if (files != null) {
            Arrays.sort(files); // sorts the array lexicographically
        }
        return files;
    }

    /**
     * Generates filename for the merged pdf file based on current time. CAUTION! DO NOT USE '_' CHARACTER IN FILENAME!
     *
     * @return new filename
     */
    private static String generateNewFilename() {
        Date date = new Date();
        SimpleDateFormat timeNow = new SimpleDateFormat("MMMdd-HHmmss");
        return "./merged-" + timeNow.format(date) + ".pdf";
    }

    /**
     * Merges files into one.
     *
     * @param files files to merge, in order.
     */
    private static void mergePdfs(File... files) {
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        String newFilename = generateNewFilename();
        mergerUtility.setDestinationFileName(newFilename);
        for (File file : files) {
            try {
                mergerUtility.addSource(file);
            } catch (FileNotFoundException e) {
                System.err.println("Fatal error, file " + file.getName() + " not found!");
                System.exit(1);
            }
        }
        try {
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            System.out.println("Files merged successfully to " + newFilename);
            System.out.println("Files merged successfully.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
