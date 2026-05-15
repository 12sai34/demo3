//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class YatraExcelValidation {
    public static void validateOffersPage(WebDriver driver) throws Exception {
        String excelPath = System.getProperty("user.dir") + "\\resources\\YatraTestData.xlsx";
        FileInputStream fis = new FileInputStream(excelPath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        CellStyle passStyle = workbook.createCellStyle();
        passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        CellStyle failStyle = workbook.createCellStyle();
        failStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
        failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Row titleRow = sheet.getRow(1);
        String expectedTitle = titleRow.getCell(1).getStringCellValue();
        String actualTitle = driver.getTitle();
        titleRow.createCell(2).setCellValue(actualTitle);
        Cell titleStatus = titleRow.createCell(3);
        if (actualTitle.equals(expectedTitle)) {
            titleStatus.setCellValue("PASS");
            titleStatus.setCellStyle(passStyle);
        } else {
            titleStatus.setCellValue("FAIL");
            titleStatus.setCellStyle(failStyle);
        }

        Row bannerRow = sheet.getRow(2);
        String expectedBanner = bannerRow.getCell(1).getStringCellValue();
        String actualBanner = driver.findElement(By.cssSelector("h2.wfull.bxs")).getText();
        bannerRow.createCell(2).setCellValue(actualBanner);
        Cell bannerStatus = bannerRow.createCell(3);
        if (actualBanner.equals(expectedBanner)) {
            bannerStatus.setCellValue("PASS");
            bannerStatus.setCellStyle(passStyle);
        } else {
            bannerStatus.setCellValue("FAIL");
            bannerStatus.setCellStyle(failStyle);
        }

        fis.close();
        FileOutputStream fos = new FileOutputStream(excelPath);
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    public static void cool() {
        System.out.println("hello");
    }
}
