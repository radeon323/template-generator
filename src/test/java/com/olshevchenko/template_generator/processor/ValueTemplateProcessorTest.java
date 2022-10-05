package com.olshevchenko.template_generator.processor;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ValueTemplateProcessorTest {
    private static final String path = "src/test/resources/testValueProcess.html";
    private static final Map<String, Object> parameters = Map.of("person", new Person(1, "Sasha", 41));
    private static ValueTemplateProcessor processor;

    @BeforeAll
    static void init() {
        processor = new ValueTemplateProcessor(path, parameters);
    }

    @Test
    void testProcess() {
        String expectedPage = "name Sasha age 41 person Person(id=1, name=Sasha, age=41)";
        String actualPage = processor.process();
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetContent() {
        String expectedContent = "name ${person.name} age ${person.age} person ${person}";
        String actualContent = processor.getContent();
        assertEquals(expectedContent, actualContent);
    }

    @Test
    void testGetParameters() {
        Map<String, String> expectedParams = Map.of("person.age", "${person.age}","person.name", "${person.name}","person", "${person}");
        String actualContent = processor.getContent();
        Map<String, String> actualParams = processor.getParameters(actualContent);
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

@AllArgsConstructor
@ToString
class Person {
    private int id;
    private String name;
    private int age;
}