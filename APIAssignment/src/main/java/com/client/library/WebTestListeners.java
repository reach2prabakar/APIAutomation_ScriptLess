/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  org.openqa.selenium.WebDriver
 *  org.testng.IClass
 *  org.testng.IInvokedMethod
 *  org.testng.IResultMap
 *  org.testng.IRetryAnalyzer
 *  org.testng.ISuite
 *  org.testng.ISuiteListener
 *  org.testng.ITestContext
 *  org.testng.ITestListener
 *  org.testng.ITestNGMethod
 *  org.testng.ITestResult
 *  org.testng.Reporter
 *  org.testng.internal.ConstructorOrMethod
 *  org.testng.internal.Utils
 *  org.testng.xml.XmlTest
 */
package com.client.library;


import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;



public class WebTestListeners
implements ITestListener,
ISuiteListener {
    public String sFilename;
    private static final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";
    private static String screenshotPath;
    Date d ; 
    Date d1 ;
    int totalTime = 0;
    int num =1;
   //EnvParameters env = new
   public static String getScreenshotPath() {
        return screenshotPath;
    }
 
    public void onFinish(ITestContext context) {
       System.out.println("finish");
        
    }

    public void onFinish(ISuite arg0) {
    try {
    	
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void onStart(ISuite arg0) {
        
    	String directory = System.getProperty("user.dir")+"/src/test/resources/DownloadFiles";
		new File(directory).listFiles();
		/*try {
			//FileUtils.cleanDirectory(FileUtils.getFile(directory));
		} catch (IOException e) {
			
			e.printStackTrace();
		}*/
        System.out.println("................. on start .......................");
    }
    
    public void onStart() {
    
    	 
        
    }

    public String getCurrentDateTime(String format){
    	DateFormat dateFormat = new SimpleDateFormat(format);
    	Date date = new Date();
    	return dateFormat.format(date);
    }
    
    public void onTestStart(ITestResult iTestResult){
    	String classname = iTestResult.getMethod().getTestClass().getRealClass().getSimpleName();
    	String sTestMethodName = iTestResult.getMethod().getMethodName();
        
    	System.out.println(sTestMethodName);
    	System.out.println(classname);
    	
    }
    
   
	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onTestSuccess(ITestResult result) {
		
	}

	@Override
	public void onTestFailure(ITestResult result) {
		
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}
	
	
}

