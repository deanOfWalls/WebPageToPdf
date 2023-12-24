package com.deanofwalls.webpagetopdf.crud.service;

import com.deanofwalls.webpagetopdf.selenium.PrintableWebPage;
import com.deanofwalls.webpagetopdf.selenium.WebDriverFactory;
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

    public Path convertWebPageToPdf(String url, String outputFileName) {
        return this.convertWebPageToPdf(WebDriverFactory.HEADLESS_FIREFOX, url, outputFileName);
    }


    public Path convertWebPageToPdf(WebDriverFactory webDriverFactory, String url, String outputFileName) {
        // Setup Firefox WebDriver using WebDriverManager
        final PrintableWebPage printableWebPage = new PrintableWebPage(webDriverFactory, url);
        return printableWebPage.printToFile(pdfDirectory, outputFileName);
    }
}
