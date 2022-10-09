package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.entity.Template;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class IncludeTemplateProcessorTest {
    private static final String content = "<#include \"src/test/resources/include.html\"> World";
    private static IncludeTemplateProcessor processor;
    private static Template template;

    @BeforeAll
    static void init() {
        processor = new IncludeTemplateProcessor();
        template = new Template(content, Map.of());
    }

    @Test
    void testProcess() {
        String expectedPage = "Hello World";
        processor.process(template);
        assertEquals(expectedPage, template.getContent());
    }

    @Test
    void testGetParameters() {
        Map<String, String> expectedParams = Map.of("src/test/resources/include.html", "<#include \"src/test/resources/include.html\">");
        Map<String, String> actualParams = processor.getParameters(content);
        assertEquals(expectedParams, actualParams);
    }
}