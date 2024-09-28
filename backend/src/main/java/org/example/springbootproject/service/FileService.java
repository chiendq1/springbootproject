package org.example.springbootproject.service;

import org.example.springbootproject.repository.ParameterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.util.Map;

@Service
public class FileService extends BaseService {

    @Autowired
    private ParameterRepository parameterRepository;

    public String parseThymeleafTemplate(String templateName, Map<String, Object> variables) {
        String htmlContent = parameterRepository.findByName(templateName).getValue();
        // Use StringTemplateResolver to process raw HTML content
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode("HTML");

        // Initialize the template engine
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Set up the context with variables
        Context context = new Context();
        context.setVariables(variables);

        // Process the raw HTML content and return the rendered string
        return templateEngine.process(htmlContent, context);
    }

    public ByteArrayOutputStream generatePdfFromHtml(String htmlContent) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Generate PDF with ITextRenderer
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);

            outputStream.close();
            return outputStream;
        } catch(Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

        return null;
    }

}