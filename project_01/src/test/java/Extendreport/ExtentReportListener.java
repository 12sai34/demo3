package Extendreport;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import screenshotspages.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.io.File;

public class ExtentReportListener implements ITestListener {

    private ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {

        ExtentSparkReporter spark =
                new ExtentSparkReporter(
                        System.getProperty("user.dir") + "/reports/ExtentReport.html");

        extent = new ExtentReports();
        extent.attachReporter(spark);


        String browserName = (String) context.getAttribute("browser");

        extent.setSystemInfo("Computer Name", "Localhost");
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Tester Name", "Chintala Sai Ganesh");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo(
                "Browser",
                browserName != null ? browserName.toUpperCase() : "UNKNOWN"
        );
    }

    @Override
    public void onTestStart(ITestResult result) {
        test.set(extent.createTest(result.getName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().log(Status.FAIL, result.getThrowable());
    }


    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}