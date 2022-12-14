package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.entity.Template;
import com.olshevchenko.template_generator.processor.entity.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ValueTemplateProcessorTest {
    private static final String content = "name ${person.name} age ${person.age} person ${person}";
    private static final Map<String, Object> parameters = Map.of("person", new Person(1, "Sasha", 41));
    private static ValueTemplateProcessor processor;
    private static Template template;

    @BeforeAll
    static void init() {
        processor = new ValueTemplateProcessor();
        template = new Template(content, parameters);
    }

    @Test
    void testProcess() {
        String expectedPage = "name Sasha age 41 person Person(id=1, name=Sasha, age=41)";
        processor.process(template);
        assertEquals(expectedPage, template.getContent());
    }

    @Test
    void testGetParameters() {
        Map<String, String> expectedParams = Map.of("person.age", "${person.age}","person.name", "${person.name}","person", "${person}");
        Map<String, String> actualParams = processor.getParameters(content);
        assertEquals(expectedParams, actualParams);
    }

    @Test
    void testGetEntryClassName() {
        String expectedClassName = "person";
        Map<String, String> expectedParams = Map.of("person.age", "${person.age}","person.name", "${person.name}","person", "${person}");
        Map.Entry<String, String> entry = expectedParams.entrySet().stream().filter(e -> e.getKey().equals("person.age")).findFirst().get();
        String actualClassName = processor.getEntryClassName(entry);
        assertEquals(expectedClassName, actualClassName);
    }

    @Test
    void testGetEntryFieldName() {
        String expectedFieldName = "age";
        Map<String, String> expectedParams = Map.of("person.age", "${person.age}","person.name", "${person.name}","person", "${person}");
        Map.Entry<String, String> entry = expectedParams.entrySet().stream().filter(e -> e.getKey().equals("person.age")).findFirst().get();
        String actualFieldName = processor.getEntryFieldName(entry);
        assertEquals(expectedFieldName, actualFieldName);
    }


}