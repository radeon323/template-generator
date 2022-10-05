package com.olshevchenko.template_generator;

import com.olshevchenko.template_generator.entity.Template;

import java.io.*;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class TemplateCreator {

    public Template create(String path, Map<String, Object> parameters) {
        Template template;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));) {
            String content = new String(bufferedInputStream.readAllBytes());
            template = new Template(content, parameters);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading content from file", e);
        }
        return template;
    }
}
