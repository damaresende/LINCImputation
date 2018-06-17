/**
* The class <code>ConfigurationReader</code> has methods that read the
* configuration parameters defined in a given file or in the default one:
* "files/config/config.txt"
* 
* TODO: this class is incomplete, exceptions must be implemented
*
* @author Oliveira de Resende, Damares
*   e-mail: d.oliveiraresende@gmail.com
*   Federal University of Para (UFPA)
*   Laboratory of Computational Intelligence and Operational Research (LINC)
**/

package essencials;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import exceptions.InvalidMVRateException;

public class ConfigurationParser {
    
    private static String CONFIG_FILE = System.getProperty("user.dir") + File.separator + 
    		"files" + File.separator +"config" + File.separator + "config.txt"; 
            
    private final static Charset ENCODING = StandardCharsets.UTF_8;
            
    private static String INPUT_DIR;
    private static String OUTPUT_DIR;
    private final ArrayList<String> FILES;
    private static ArrayList<String> IGNORE;
    
    private static String IMPUTATION_DIR;
    private static String AMPUTATION_DIR;
    private static String ORIGINALS_DIR;
    
    private String[] imp_types;
    
    private boolean isNGPImpute;
    private boolean isLGPImpute;
    private boolean saveFitness;
    private boolean isToAmpute;
    private boolean isSummary;
    private boolean isClassification;
    private boolean isTimeSeries;
    
    private int H;
    private int mvRate;
    private int numFolds;
    
    private boolean isToEvaluateRMSE;
    private boolean isToEvaluateNRMSE;
    private boolean isToEvaluateAutocorrelation;
    
    /**
     * Constructor. Initializes the parameters to default values and the 
     * configuration file path to the given path.
     * @param config
     */
    public ConfigurationParser(String config) {
        INPUT_DIR = "";
        OUTPUT_DIR = "";
        FILES = new ArrayList<>();
        IGNORE = new ArrayList<>();
        CONFIG_FILE = config;
		isNGPImpute = false;
		isLGPImpute = false;
		saveFitness = false;
		isToAmpute = false;
		isClassification = false;
		isTimeSeries = false;
		mvRate = 0;
		numFolds = 1;
		isSummary = false;
		isToEvaluateRMSE = false;
		isToEvaluateNRMSE = false;
		isToEvaluateAutocorrelation = false;
    }
    
    /**
     * Constructor. Initializes the parameters to default values.
     */
    public ConfigurationParser() {
        INPUT_DIR = "";
        OUTPUT_DIR = "";
        FILES = new ArrayList<>();
        IGNORE = new ArrayList<>();
		isNGPImpute = false;
		isLGPImpute = false;
		saveFitness = false;
		isToAmpute = false;
		isClassification = false;
		isTimeSeries = false;
		mvRate = 0;
		numFolds = 1;
		isSummary = false;
		isToEvaluateRMSE = false;
		isToEvaluateNRMSE = false;
		isToEvaluateAutocorrelation = false;
    }
   
    
    public String getInputDir() {return INPUT_DIR;}
    
    public String getOutputDir() {return OUTPUT_DIR;}
    
    public String getImputationDir() {return IMPUTATION_DIR;}
    
    public String getAmputationDir() {return AMPUTATION_DIR;}
    
    public String getOriginalsDir() {return ORIGINALS_DIR;}
    
    public String[] getImputeTypes() {return imp_types;}
    
    public ArrayList<String> getFileNames() {return FILES;}
    
    public boolean isNGPImpute() {return isNGPImpute;}
    
    public boolean isLGPImpute() {return isLGPImpute;}
    
    public boolean saveFitness() {return saveFitness;}
    
    public boolean isToAmpute() {return isToAmpute;}
    
    public boolean isSummary() {return isSummary;}
    
    public boolean isClassification() {return isClassification;}
    
    public boolean isTimeSeries() {return isTimeSeries;}
    
    public int getH() {return H;}
    
    public int getMVRate() {return mvRate;}
    
    public int getNumFolds() {return numFolds;}
    
    public boolean isToEvaluateRMSE() {return isToEvaluateRMSE;}
    
    public boolean isToEvaluateNRMSE() {return isToEvaluateNRMSE;}
    
    public boolean isToEvaluateAutocorrelation() {return isToEvaluateAutocorrelation;}
    
    /**
     * Reads the parameters specified in the configuration file config.txt
     * and stores them in static variables. 
     * 
     * @return true the configuration file is valid and false otherwise
     * @throws java.io.IOException
     */
    public boolean readConfiguration() throws IOException {
    	Path path = Paths.get(CONFIG_FILE);
        boolean isValid = false;
        
        BufferedReader reader = Files.newBufferedReader(path, ENCODING);
        String line;
        boolean isAll = false;
        boolean isPart = false;
        while ((line = reader.readLine()) != null) {
		    // is valid and is info to be read
        	if(isValid && line.startsWith("$")) {
				String[] flag = line.split(" ");
				switch (flag[0]) {
				    case "$input_dir":
				    	INPUT_DIR = System.getProperty("user.dir") + File.separator + fixFlag(flag); 
				    	break;
				    case "$output_dir":
				    	OUTPUT_DIR = System.getProperty("user.dir") + File.separator + fixFlag(flag);
				    	break;
				    case "$dataset_name":
				    	if(flag[1].compareTo("ALL") == 0) {
				    		isAll = getAllFileNames();
				    	} else {
				    		isPart = true;
				    		FILES.add(fixFlag(flag) + ".arff");
				    	}
				    	if(isPart && isAll) {
				    		FILES.clear();
				    		isValid = false;
				    	} break;
				    case "$imputation_type":
				    	if(flag[1].compareTo("NGP") == 0) {
				    		isNGPImpute = true;
				    	} else if(flag[1].compareTo("LGP") == 0) {
				    		isLGPImpute = true;
				    	} else {
				    		System.err.println("Error. Invalid type. \'$datasets_type\' must be" + " NGP or LGP.");
				    	} break;    
				    case "$num_folds":
				    	numFolds = Integer.parseInt(flag[1]);
				    	break;
				    case "$save_fitness":
				    	saveFitness = flag[1].compareTo("yes")==0;
				    	break;
				    case "$datasets_type":
						if(flag[1].compareTo("CL") == 0) {
						    isClassification = true;
						} else if(flag[1].compareTo("TS") == 0) {
						    isTimeSeries = true;
						} else {
						    System.err.println("Error. Invalid type. \'$datasets_type\' must be"
							    + " CL or TS.");
						} break;    
				    case "$imp_types":
						imp_types = new String[flag.length-1];
						for(int i = 1; i < flag.length; i++)
						    imp_types[i-1] = flag[i];
						break;    
				    case "$mv_rate":
						mvRate = Integer.parseInt(flag[1]);
						if(mvRate < 0)
						    throw new InvalidMVRateException("Error. MV rate mus be grater than 0.");
						else if(mvRate > 90)
						    throw new InvalidMVRateException("Error. MV rate mus be less than 90.");
						break;
				    case "$amp_dir":
						AMPUTATION_DIR = System.getProperty("user.dir") + File.separator 
							+ fixFlag(flag); 
						break;
				    case "$imp_dir":
						IMPUTATION_DIR = System.getProperty("user.dir") + File.separator 
							+ fixFlag(flag); 
						break;
				    case "$ori_dir":
						ORIGINALS_DIR = System.getProperty("user.dir") + File.separator 
							+ fixFlag(flag); 
						break;
				    case "$acor_lag":
						H = Integer.parseInt(flag[1]);
						break;
				    case "$operation":
						if(flag[1].compareTo("AMP") == 0)
						    isToAmpute = true;
						else if(flag[1].compareTo("RMSE") == 0)
						    isToEvaluateRMSE = true;
						else if(flag[1].compareTo("ACOR") == 0)
						    isToEvaluateAutocorrelation = true;
						else if(flag[1].compareTo("NRMSE") == 0)
						    isToEvaluateNRMSE = true;
						else if(flag[1].compareTo("INFO") == 0)
						    isSummary = true;
						else
						    System.err.println("Error! Invalid operation type. "
							    + "It must be INFO, AMP, RMSE, NRMSE or ACOR.");
						break;       
				    default:
				    	System.out.println("ERROR! INVALID FLAG!");
				    	isValid = false;
				    	break;
				}
			// not a comment and has the validation flag
		    } else if(!line.startsWith("#") && (line.compareTo("$configuration_file") == 0)) {
		    	isValid = true;
		    //not a comment, not a blank line and does not have a validation flag
		    } else if(!line.startsWith("#") && (line.compareTo("") != 0) && !isValid) {
		    	System.out.println("ERROR! Invalid configuration file");
		    	break;
		    } else if(isValid && line.startsWith("@")) {
		    	String[] flag = line.split(" ");
				if(flag[0].compareTo("@$dataset_name") == 0) {
				    IGNORE.add(flag[1]);
				    System.out.println("> Skipping file: " + flag[1]);
				} else {
					System.out.println("ERROR! Invalid configuration file");
					break;
				}
		    }
        }  
        
        IGNORE.stream().forEach((s) -> {
        	for(int i = 0; i < FILES.size(); i++)
        		if(s.compareTo(FILES.get(i)) == 0) FILES.remove(i);
        });
        return isValid;
    }
    
    /**
     * Gets the name of every file of type FILE_FORMAT in the directory
     * INPUT_DIR and stores it in a list.
     * 
     * @return true if there are files in the list and false otherwise
     */
    private boolean getAllFileNames() {
        File folder = new File(INPUT_DIR);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".arff")) {
                FILES.add(file.getName());
            }
        }
        return (!FILES.isEmpty());
    }
    
    /**
     * Prints the parameters read in the configuration file.
     */
    public void printParameters() {
        System.out.println("> Input directory: \""+ INPUT_DIR + "\"\n> Output directory: \"" + OUTPUT_DIR + "\"\n> Input files (" + FILES.size() + "):");
        FILES.stream().forEach((file) -> {
        	System.out.println("  >> " + file);
        });
    }
    
    /**
     * Adjusts the input string to a valid format. Remove quotes and
     * change path separator according to the platform used.
     * 
     * @param flag a two size array with the macro and value of the flag
     * @return a valid value for the correspondent macro
     */
    public String fixFlag(String[] flag) {
        String name = "";
        
        for(int i = 0; i < flag[1].length(); i++) {
            if(flag[1].charAt(i) != '\"')
                if(flag[1].charAt(i) == '\\' || flag[1].charAt(i) == '/')
                    name = name + File.separator;
                else
                    name = name +flag[1].charAt(i);
        }
            
        if(name.charAt(name.length() - 1) != File.separator.charAt(0) && flag[0].compareTo("$dataset_name") != 0)
            name = name + File.separator;
        
        return name;
    }
}