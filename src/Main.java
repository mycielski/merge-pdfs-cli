package src;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    /**
     * Concatenates pdfs in the order they were passed as command line arguments.
     *
     * @param args paths to pdf files to be merged, in order.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Must specify more than one file.");
        }
        PDFMergerUtility mergerUtility = new PDFMergerUtility();
        String newFilename = generateNewFilename();
        mergerUtility.setDestinationFileName(newFilename);
        for (String filepath : args) {
            try {
                mergerUtility.addSource(new File(filepath));
            } catch (FileNotFoundException e) {
                System.err.println("Fatal error, file " + filepath + " not found!");
                return;
            }
        }
        try {
            mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            System.out.println("Files merged successfully to " + newFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

}
