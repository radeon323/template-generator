package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.TemplateCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class IncludeTemplateProcessorTest {
    private static final String path = "src/test/resources/testIncludeProcess.html";
    private static IncludeTemplateProcessor processor;

    @BeforeAll
    static void init() {
        processor = new IncludeTemplateProcessor(path);
    }

    @Test
    void testProcess() {
        String expectedPage = "Hello World";
        String actualPage = processor.process();
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetParameters() {
        Map<String, String> expectedParams = Map.of("src/test/resources/include.html", "<#include \"src/test/resources/include.html\">");
        String actualContent = new TemplateCreator().create(path).getContent();
        Map<String, String> actualParams = processor.getParameters(actualContent);
        assertEquals(expectedParams, actualParams);
    }
}