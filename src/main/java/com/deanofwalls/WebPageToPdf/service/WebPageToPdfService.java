package com.deanofwalls.WebPageToPdf.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.File;
import java.nio.file.Files;

import org.openqa.selenium.print.PrintOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.github.bonigarcia.wdm.WebDriverManager;

@Service
public class WebPageToPdfService {

    @Value("${pdf.storage.directory}")
    private String pdfDirectory;

    public Path convertWebPageToPdf(String url, String outputFileName) throws Exception {
        // Setup Firefox WebDriver using WebDriverManager
        WebDriverManager.firefoxdriver().setup();
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");
        System.out.println("URL received for conversion");
        System.out.println("URL is " + url);


        // Set the path to the Firefox binary
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/usr/bin/firefox"); // Replace with the path to Firefox binary

        FirefoxDriver driver = new FirefoxDriver(options);
        try {
            driver.get(url);

            PrintOptions printOptions = new PrintOptions();
            printOptions.setPageRanges("1");

            byte[] pdfContent = driver.print(printOptions).getContent().getBytes();

            Path storagePath = Paths.get(pdfDirectory);
            Files.createDirectories(storagePath);

            File outputFile = new File(storagePath.toFile(), outputFileName);
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(pdfContent);
            }

            return outputFile.toPath();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
