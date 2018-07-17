/**
* The class <code>FileManager</code> has methods to read and write
* a dataset of/to a .arff file.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package essencials;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import weka.core.Instances;

public class FileManager {
    
     /**
     * Reads a dataset from a .arff file.
     * 
     * @param fileName file path
     * @return dataset
     * @throws java.io.IOException
     */
    public static Instances loadFile(String fileName) throws IOException {
        
        Instances data;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            //load the data
            data = new Instances(reader);
            reader.close();
        }
        return data;
    }
    
    /**
     * Writes string data to a file.
     * 
     * @param data data
     * @param filename file path 
     * @throws java.io.IOException
     */
    public static void saveDataset(Instances data, String filename) throws IOException{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data.toString());
            writer.flush();
            writer.close();
        }
    }
    
    /**
     * Writes string data to a file.
     * 
     * @param data data
     * @param filename file path 
     * @throws java.io.IOException
     */
    public static void saveTextInfo(String data, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(data);
            writer.flush();
            writer.close();
        }
    }
    
    /**
     * Appends a string data to a file
     * 
     * @param data string data
     * @param filename file path
     * @throws java.io.IOException
     */
    public static void appendFile(String data, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.flush();
        }
    }
    
    /**
     * Copies a file from a source to a destination
     * 
     * @param sourcePath source path
     * @param destinationPath destination path
     * @throws java.io.IOException
     */
    public static void copyFile(String sourcePath, String destinationPath) throws IOException {
        Files.copy(Paths.get(sourcePath), new FileOutputStream(destinationPath));
    }
}
