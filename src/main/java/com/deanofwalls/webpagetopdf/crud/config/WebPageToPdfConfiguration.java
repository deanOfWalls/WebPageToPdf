package com.deanofwalls.webpagetopdf.crud.config;

import net.sourceforge.htmlunit.corejs.javascript.tools.shell.Environment;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebPageToPdfConfiguration {
    public Environment environment() {
        return new Environment();
    }
}
