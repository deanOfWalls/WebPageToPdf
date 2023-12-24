package com.deanofwalls.webpagetopdf.selenium;

import org.openqa.selenium.print.PrintOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PrintableWebPage {
    private WebDriverFactory webDriverFactory;
    private String url;
    private RemoteWebDriver driver;

    public PrintableWebPage(WebDriverFactory webDriverFactory, String url) {
        this.webDriverFactory = webDriverFactory;
        this.url = url;
    }

    public RemoteWebDriver getDriver() {
        if (driver == null) {
            this.driver = webDriverFactory.create();
        }
        return driver;
    }

    public Path printToFile(String outputDirectory, String outputFileName) {
        final PrintOptions printOptions = new PrintOptions();
        printOptions.setPageRanges("1");
        getDriver().get(url);

        try {
            final Path storagePath = Paths.get(outputDirectory);
            final File outputFile = new File(storagePath.toFile(), outputFileName);
            final FileOutputStream outputStream = new FileOutputStream(outputFile);
            final byte[] pdfContent = getDriver()
                    .print(printOptions)
                    .getContent()
                    .getBytes();

            Files.createDirectories(storagePath);
            outputStream.write(pdfContent);
            return outputFile.toPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                getDriver().quit();
            } catch (Throwable t) {
            }
        }
    }
}
