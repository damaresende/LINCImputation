/**
* The class <code>FileManager</code> has methods to read and write
* a dataset of/to a .arff file.
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    public static void writeFile(String data, String filename) throws IOException {
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
}
