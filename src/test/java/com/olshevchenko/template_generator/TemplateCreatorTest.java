package com.olshevchenko.template_generator;

import com.olshevchenko.template_generator.entity.Template;
import com.olshevchenko.template_generator.utils.TemplateCreator;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class TemplateCreatorTest {
    private static final String path = "src/test/resources/include.html";
    private static final Map<String, Object> parameters = Map.of();

    @Test
    void testCreateTemplate() {
        String content = "Hello";
        Template expectedTemplate = new Template(content, parameters);
        Template actualTemplate = new TemplateCreator().create(path, parameters);
        assertEquals(expectedTemplate, actualTemplate);
    }
}