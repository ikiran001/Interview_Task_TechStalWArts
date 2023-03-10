//Listener class used to generate extent report



package com.TechStalWarts.utilities;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Reporting extends TestListenerAdapter{
	public ExtentHtmlReporter htmlReporter;
	public ExtentReports extent;
	public ExtentTest logger;

	@Override
	public void onTestSuccess(ITestResult tr) {
		logger=extent.createTest(tr.getName());
		logger.log(Status.PASS, MarkupHelper.createLabel(tr.getName(), ExtentColor.GREEN));
		super.onTestSuccess(tr);
	}


	@Override
	public void onTestFailure(ITestResult tr) {
		logger=extent.createTest(tr.getName());
		logger.log(Status.FAIL, MarkupHelper.createLabel(tr.getName(), ExtentColor.RED));

		String screenShotPath=System.getProperty("user.dir")+"\\screenShots\\"+tr.getName()+".png";

		File f=new File(screenShotPath);
		if(f.exists()) {
			try {
				logger.fail("ScreenShot is below:" +logger.addScreenCaptureFromPath(screenShotPath));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		super.onTestFailure(tr);
	}


	@Override
	public void onTestSkipped(ITestResult tr) {
		logger=extent.createTest(tr.getName());
		logger.log(Status.SKIP, MarkupHelper.createLabel(tr.getName(), ExtentColor.ORANGE));
		super.onTestSkipped(tr);
	}
	@Override
	public void onStart(ITestContext testContext) {
		String timeStamp= new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss").format(new Date());
		String repName="Test-report-"+timeStamp+".html";


		htmlReporter=new ExtentHtmlReporter(System.getProperty("user.dir")+"/test-output/"+repName);
		htmlReporter.loadXMLConfig(System.getProperty("user.dir")+"/extent-config.xml");

		extent=new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("Host Name", "Local Host");
		extent.setSystemInfo("Environement", "QA");
		extent.setSystemInfo("User", "Kiran");

		htmlReporter.config().setDocumentTitle("TechStalWarts Test Project");
		htmlReporter.config().setReportName("Functional Test Report");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.DARK);
		super.onStart(testContext);
	}
	@Override
	public void onFinish(ITestContext testContext) {
		extent.flush();
		super.onFinish(testContext);
	}





}
