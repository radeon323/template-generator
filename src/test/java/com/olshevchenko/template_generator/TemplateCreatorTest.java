package com.olshevchenko.template_generator;

import com.olshevchenko.template_generator.entity.Template;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class TemplateCreatorTest {

    @Test
    void testCreateTemplate() {
        String content = "<h1>Hello</h1>";
        Map<String, Object> parameters = Map.of();
        Template expectedTemplate = new Template(content, parameters);
        String path = "src/test/resources/include.html";
        Template actualTemplate = new TemplateCreator().create(path, parameters);
        assertEquals(expectedTemplate, actualTemplate);
    }

}