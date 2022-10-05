package com.olshevchenko.template_generator.processor;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ValueTemplateProcessorTest {
    private final String path = "src/test/resources/testValueProcess.html";
    private final Map<String, Object> parameters = Map.of("person", new Person(1, "Sasha", 41));

    @Test
    void testProcess() {
        String expectedPage = "name Sasha age 41 person Person(id=1, name=Sasha, age=41)";
        ValueTemplateProcessor processor = new ValueTemplateProcessor(path, parameters);
        String actualPage = processor.process();
        assertEquals(expectedPage, actualPage);
    }
}

@AllArgsConstructor
@ToString
class Person {
    private int id;
    private String name;
    private int age;
}