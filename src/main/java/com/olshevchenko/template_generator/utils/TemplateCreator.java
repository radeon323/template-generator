package com.olshevchenko.template_generator.utils;

import com.olshevchenko.template_generator.entity.Template;

import java.io.*;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class TemplateCreator {

    public Template create(String path, Map<String, Object> parameters) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path));) {
            String content = new String(bufferedInputStream.readAllBytes());
            return new Template(content, parameters);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading content from file", e);
        }
    }

    public Template create(String path) {
        return create(path, Map.of());
    }

}
