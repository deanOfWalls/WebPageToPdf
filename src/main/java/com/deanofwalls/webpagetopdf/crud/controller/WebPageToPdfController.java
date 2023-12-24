package com.deanofwalls.webpagetopdf.crud.controller;

import com.deanofwalls.webpagetopdf.crud.service.WebPageToPdfService;
import com.deanofwalls.webpagetopdf.crud.model.WebPageToPdfModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/pdf")
public class WebPageToPdfController {

    private final WebPageToPdfService webPageToPdfService;
    private final Environment env;
    private final String pdfDirectory;

    @Autowired
    public WebPageToPdfController(WebPageToPdfService webPageToPdfService,
                                  Environment env,
                                  @Value("${pdf.storage.directory}") String pdfDirectory) {
        this.webPageToPdfService = webPageToPdfService;
        this.env = env;
        this.pdfDirectory = pdfDirectory;
    }

    @PostMapping("/convert")
    public ResponseEntity<Object> convertToPdf(@RequestBody WebPageToPdfModel request) {
        try {
            Path pdfPath = webPageToPdfService.convertWebPageToPdf(request.getUrl(), request.getOutputFileName());
            String downloadUrl = "http://localhost:8080/pdf/download/" + pdfPath.getFileName().toString();
            return ResponseEntity.ok().body("PDF generated successfully. Download URL: " + downloadUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error in PDF generation: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String fileName) {
        Path file = Paths.get(pdfDirectory).resolve(fileName);
        try {
            if (!Files.exists(file)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(file.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName().toString() + "\"")
                    .body(resource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e) {
                // Log the exception
            }
        }
    }
}
