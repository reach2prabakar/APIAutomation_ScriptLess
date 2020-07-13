package com.client.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.ITestNGMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.jayway.restassured.path.json.JsonPath;


public class AutomationBuddy {


    
    
    public final static String env = "test";
    
    public static String propFileName = "Project.properties";
    
    public static String environment;
    
    public static String emailrec;

	public final static String systemdir = System.getProperty("user.dir");
	
	public final static String resourcefolder = "//src//test//resources//";
	
    public final static String testdatafolder = "//src//test//resources//testdata//";
    
    public final static String resultfolder = System.getProperty("user.dir")+"//test-output//Results";
    
    public final static LinkedHashMap<String, String> globalmap = new LinkedHashMap<>();
        
    public final static HashMap<String,HashMap<String, String>> preconditionmap = new HashMap<String, HashMap<String, String>>();
    
     public static void setenvironment(String env){
    	environment = env;
    }
    
    public static String getenv(){
    	if(environment.equalsIgnoreCase("pp")){
    		return "";
    	}
    	else{
    		throw new RuntimeException("Environment is not set");
    	}
    	
    }
    
    /**
	 * Getting Properties Values
	 * 
	 *
	 */
	public static String getPropValues(String propKey) throws IOException {
		try {
			Properties prop = new Properties();
			//System.out.println("Global.propFilename : === "+Global.propFileName);
			File file = new File(systemdir+resourcefolder+propFileName);
			FileInputStream fileInput = new FileInputStream(file);
			prop.load(fileInput);
			String PropValue = prop.getProperty(propKey);
			return PropValue;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("property file '" + systemdir+resourcefolder+propFileName + "' not found in the classpath");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    /**
     * .
     * 
     * @param emailre
     *            
     */
    public static void setemailrec(String emailre){
    	emailrec = emailre;
    }
    
    public static String getemailrec(){
    	
    	return emailrec;
    	
    }
    
     public static String testdatafolder()
    {
    	return systemdir+testdatafolder;
    }
    
    
    public HashSet<String> dataProvider(int row) throws IOException{
    	HashSet<String> set = new HashSet<String>();
    	String dir = System.getProperty("user.dir");  //test.getMethodName()
        FileInputStream filesuite = new FileInputStream(new File(dir+"//TestSuite.xlsx"));
        XSSFWorkbook workbooksuite = new XSSFWorkbook(filesuite);
        XSSFSheet sheet = workbooksuite.getSheetAt(0);
        String Folder = sheet.getRow(row).getCell(1).getStringCellValue();
        String File = sheet.getRow(row).getCell(2).getStringCellValue();
        String Run = sheet.getRow(row).getCell(3).getStringCellValue();
        if(Run.toLowerCase().equals("yes")) {
        	set.add(Folder+"//"+File);
        }
        workbooksuite.close();
        filesuite.close();
        
		return set;
    }
    
    
    public static Object[][] dataprovider(ITestNGMethod test) throws Exception{
    	Object[][] data = null ;
    	String[] key;
    	LinkedHashMap<String, String> assertmap = new LinkedHashMap<String, String>();
    	LinkedHashMap<Object[], String> inputdatamap = new LinkedHashMap<Object[], String>();
    	String jsoninput = null;
    	String method = null;
    	String resource =null;
    	int rowcount =0;
    	
    //// suite 
    	
    	ArrayList<String> set = new ArrayList<String>();
    	String dir = System.getProperty("user.dir");  //test.getMethodName()
        FileInputStream filesuite = new FileInputStream(new File(dir+"//TestSuite.xlsx"));
        XSSFWorkbook workbooksuite = new XSSFWorkbook(filesuite);
        XSSFSheet sheetsuite = workbooksuite.getSheetAt(0);
        int suiterow = sheetsuite.getLastRowNum();
        String Folder = null; 
        String File = null;
        String Run = null;
        
        for (int i = 1; i <=suiterow; i++) {
        	Folder = sheetsuite.getRow(i).getCell(1).getStringCellValue();
            File = sheetsuite.getRow(i).getCell(2).getStringCellValue();
            Run = sheetsuite.getRow(i).getCell(3).getStringCellValue();
            if(Run.toLowerCase().equals("yes")) {
            	set.add(Folder+"//"+File);
            }
		}
        
        
        workbooksuite.close();
        filesuite.close();    	
    	
    	
   ////
    	try
        {
    		for (int i = 0; i <set.size(); i++) {
    			LinkedHashMap<String, String> hmap = new LinkedHashMap<String, String>();
    			FileInputStream file = new FileInputStream(new File(dir+"//src//test//resources//testdata//"+set.get(i)+".xlsx"));
    			XSSFWorkbook workbook = new XSSFWorkbook(file);
	            XSSFSheet sheet0 = workbook.getSheetAt(0);
	            XSSFSheet sheet = workbook.getSheetAt(1);
	            jsoninput = sheet0.getRow(1).getCell(2).getStringCellValue();
	            method =sheet0.getRow(1).getCell(0).getStringCellValue();
	            resource =sheet0.getRow(1).getCell(1).getStringCellValue();
	            rowcount =  sheet.getPhysicalNumberOfRows();
	            resource = resource + ","+set.get(i).split("//")[1];
	            String propertyname = null;
	            try {
		            String propertytransfer =sheet0.getRow(1).getCell(3).getStringCellValue();
		            System.out.println(propertytransfer);
		            String target = propertytransfer.split("\n")[2].split("=")[1];
		            String jsonpath = propertytransfer.split("\n")[1].split("=")[1];
		            propertyname = propertytransfer.split("\n")[0].split("=")[1];
		            propertytransfer(propertytransfer,rowcount);
		            resource = resource+","+target+","+propertyname+","+jsonpath;
	            }catch(Exception e) {
	            	
	            }
	           
	            int colcount = sheet.getRow(0).getPhysicalNumberOfCells();
	            //data = new Object[rowcount-1][2];
	            key = new String[colcount];
	            Iterator<Row> rowIterator = sheet.iterator();
		       String resourcedupliacte = null;
		       int loopforproperty = 1;
		       boolean errorprint = false;
		       int rowc=0;
	            while (rowIterator.hasNext()) 
	            {
	            	resourcedupliacte = resource+","+loopforproperty++;
	            	int count = 0;
	            	String value;
	                Row row = rowIterator.next();
	                Iterator<Cell> cellIterator = row.cellIterator();
	                 if(rowc!=0) {
		                while (cellIterator.hasNext()) 
		                {
		                    Cell cell = cellIterator.next();
		                    //Check the cell type and format accordingly
		                    switch (cell.getCellType()) 
		                    {
		                        case Cell.CELL_TYPE_STRING:
		                        	
		                        	value = cell.getStringCellValue();
		                        	hmap.put(key[count++], value);
		                            break;
		                            
		                        case Cell.CELL_TYPE_NUMERIC:
		                        	value = String.valueOf((int)cell.getNumericCellValue());
		                        	hmap.put(key[count++], value);
			                         break;
		                     }
		                }
		                StrSubstitutor substitor				= new StrSubstitutor(hmap);
		                String jjsoninput = substitor.replace(jsoninput);
//regexreplacement		
		                resourcedupliacte = substitor.replace(resourcedupliacte);
		                if(jjsoninput.contains("${")) {
		                	jjsoninput= jjsoninput.replaceAll("\\$\\{", "\\$\\{"+(rowc+1));
						}
		                if(resourcedupliacte.contains("${")){
		                	resourcedupliacte= resourcedupliacte.replaceAll("\\$\\{", "\\$\\{"+(rowc+1));
		                }
		                assertmap =  getAssertionData(workbook, hmap);
		                Object[] localarray = new Object[2];
		                localarray[0] = jjsoninput;
		                localarray[1] = assertmap;
		                inputdatamap.put(localarray,method+","+resourcedupliacte);
		              //  data[rowc-1][0] = localarray;
		              //  data[rowc-1][1] = method+","+resource;
		              }else {
		            	while (cellIterator.hasNext()) 
		                {
		                    Cell cell = cellIterator.next();
		                    try {
		                    	key[count++] = cell.getStringCellValue();
		                    }catch(Exception e) {
		                    	System.out.println("The Data coloumn header should be string");
		                    }
		                     
		                }
		            }
	                 rowc++;
	            }
	            if(errorprint) {
	            	System.out.println("Check the property transfer in test case" + set.get(i));
	            	System.out.println("Check the property transfer target has a valid test case name");
	            	System.out.println("The Target test case should be executed after the test case" + set.get(i) );
	            }
	            workbook.close();
	            file.close();
          }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    	
    	int i =0;
    	data = new Object[inputdatamap.size()][2];
    	for (Object[] inputdatamapkey : inputdatamap.keySet()) {
    		 data[i][1] = inputdatamap.get(inputdatamapkey);
             data[i][0] = inputdatamapkey;
             i++;
		}
    	return data;
    }
    
   public static LinkedHashMap<String, String> getAssertionData(XSSFWorkbook workbook,LinkedHashMap<String, String> hmap) {
    	LinkedHashMap<String, String> assertmap = new LinkedHashMap<String, String>();
    	String[] key;
    	String assertvalue;
    	XSSFSheet sheet = workbook.getSheet("Assertion");
    	try {
	    	int colcount = sheet.getRow(0).getPhysicalNumberOfCells();
	        key = new String[colcount];
	    	int rowcount = sheet.getLastRowNum();
	    	int cellcount = sheet.getRow(0).getLastCellNum();
			int countforcell = cellcount;
			int countforrow = rowcount;
			int loopcount = (rowcount)*cellcount;
			
			for (int i = 0; i <cellcount; i++) {
				Cell cell = sheet.getRow(0).getCell(i);
				switch (cell.getCellType()) 
	            {
	                case Cell.CELL_TYPE_STRING:
	                	key[i] = cell.getStringCellValue().replaceAll("\\s", "");;
	                	break;
	             }
			}
	    	for (int i = 0; i <loopcount; i++) {
	    		if(countforcell==0) {
	    			countforrow = countforrow-1;
	    		}
				Cell cell = sheet.getRow(rowcount-(countforrow-1)).getCell(cellcount-countforcell);
				
				if(cell!=null) {
					switch (cell.getCellType()) 
		            {
						case Cell.CELL_TYPE_BLANK:
							break;
		                case Cell.CELL_TYPE_STRING:
		                	assertmap.put(key[i],cell.getStringCellValue());
		                	break;
		                	
		                case Cell.CELL_TYPE_NUMERIC:
		                	assertmap.put(key[i],cell.getStringCellValue());
		                	break;
		             }
				}
				countforcell--;
	    	}
	    	StrSubstitutor substitor				= new StrSubstitutor(hmap);
	       
	    	for (String Keys : assertmap.keySet()) {
				assertvalue = substitor.replace(assertmap.get(Keys));
				assertmap.put(Keys,assertvalue);
			}
    	}catch(NullPointerException e){
    		System.out.println("No Assertion is created for the test case, Default Assertion will be done");
    	}
    	 
    	return assertmap;
    }
    
   public static void propertytransfer(String property, int loopcount) throws FileNotFoundException {
   	LinkedHashMap<String, String> propertymap = new LinkedHashMap<String, String>();
   	try {
   		String[] propertyarr = property.split("\n");
   		String propref = propertyarr[0].split("=")[1];
   		String target = propertyarr[2].split("=")[1];
   		for (int i = 2; i <=loopcount; i++) {
//regexreplacement
   			propertymap.put(i+propref, "");
			}
   		preconditionmap.put(target,propertymap);
   	 }catch (Exception e) {
			e.printStackTrace();
		}
   }
   
   public static void assignproperty(String mapname,JsonPath jp, int loopcount,String propertyname,String jpath) {
	   String response;
	  if(jpath.equalsIgnoreCase("Response")){
		  response = jp.prettify();
	  }else {
		  response = null;
	  }
	  String res = jp.getString(jpath);
//regexreplacement	   
	  HashMap<String, String> map =  preconditionmap.get(mapname);
	  map.put(loopcount+propertyname,res);
	  preconditionmap.put(mapname, map);
	}
   
   public static void assignproperty(String mapname,int loopcount,String propertyname,String jpath) {
	    
	  HashMap<String, String> map =  preconditionmap.get(mapname);
	  map.put(loopcount+propertyname,jpath);
//regexreplacement 
   }
   
   public static String assignproperty(String jsoninput, String mapname, int loopcount) {
	   String updatejsoninput = null;
	  	 try {
	  		 HashMap<String, String> map =  preconditionmap.get(mapname);
	  		 if(map==null) {
	  			// throw new Exception("Property transfer for "+ mapname +" is not found");
	  		 }
			  StrSubstitutor substitor = new StrSubstitutor(map); 
			  updatejsoninput = substitor.replace(jsoninput); 
			 }catch(Exception e) { 
			  	e.printStackTrace();
		  }
		return updatejsoninput;
		 
   }
   
   
    public static void assertvalidation(String json , LinkedHashMap<String, String> hmap) {
    	JsonPath jp = new JsonPath(json);
    	System.out.println("output:"+json);
    	if(hmap.size()!=0) {
	    	for (String key: hmap.keySet()) {
	    		String hmapvalue = hmap.get(key); 
	    		if(key.toLowerCase().contains("contains")) {
	    			String result = json;
	    			if(result.toLowerCase().contains(hmapvalue)) {
	    				System.out.println("pass");
	    			}else {
	    				System.out.println("Failed with value " + result);
	    			}
	    		}else if(key.toLowerCase().trim().contains("jsonpath")) {
	    			
	    		}else if(key.toLowerCase().trim().contains("jsonvalue")) {
	    			String root = hmapvalue.split("=")[0];
	    			String tovalidate = hmapvalue.split("=")[1].trim();
	    			String fromresponse = jp.getString(root).trim();
	    			boolean pass = false;
	    			if(fromresponse.contains("[")&&fromresponse.contains(",")) {
	    				String[] fromres = fromresponse.split(",");
	    				for (String string : fromres) {
	    					String replaced = string.replaceAll("\\[|\\]", "").trim();
	    					if(tovalidate.equals(replaced)) {
	    	    				pass =true;
	    	    			}
						}
	    			}else {
	    				if(tovalidate.equals(fromresponse)) {
    	    				pass =true;
    	    			}
	    			}
	    			
	    			if(pass) {
	    				System.out.println("pass");
	    			}else {
	    				System.out.println("fail ");
	    			}
	    			
	    		}
	    		
			}
    	}
    }

	

	
	
    
    
}
