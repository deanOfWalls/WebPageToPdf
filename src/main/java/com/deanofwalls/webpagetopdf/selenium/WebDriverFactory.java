package com.deanofwalls.webpagetopdf.selenium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.function.Function;
import java.util.function.Supplier;
/**
 * Produces new instances of WebDrivers by Type
 */
public enum WebDriverFactory {
    CHROME(DesiredCapabilitiesFactory::getChrome, ChromeDriver::new),
    FIREFOX(DesiredCapabilitiesFactory::getFirefox, FirefoxDriver::new),
    HEADLESS_CHROME(DesiredCapabilitiesFactory::getHeadlessChrome, ChromeDriver::new),
    HEADLESS_FIREFOX(DesiredCapabilitiesFactory::getHeadlessFirefox, FirefoxDriver::new),
    PHANTOMJS(DesiredCapabilitiesFactory::getPhantomJs, PhantomJSDriver::new),
    HTMLUNIT(DesiredCapabilitiesFactory::getHtmlUnit, HtmlUnitDriver::new);
    private final Function<Capabilities, WebDriver> webDriverConstructor;
    private final Supplier<Capabilities> capabilitiesSupplier;

    WebDriverFactory(Supplier<Capabilities> capabilitiesSupplier, Function<Capabilities, WebDriver> constructor) {
        this.webDriverConstructor = constructor;
        this.capabilitiesSupplier = capabilitiesSupplier;
    }
    public RemoteWebDriver create() {
        return (RemoteWebDriver) webDriverConstructor.apply(capabilitiesSupplier.get());
    }
}
