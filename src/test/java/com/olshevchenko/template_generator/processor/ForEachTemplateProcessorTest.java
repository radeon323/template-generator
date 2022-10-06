package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.processor.entity.Person;
import com.olshevchenko.template_generator.utils.TemplateCreator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ForEachTemplateProcessorTest {
    private static final String path = "src/test/resources/testForEachProcess.html";
    private static final Map<String, Object> parameters = Map.of("persons", List.of(new Person(1, "Sasha", 41), new Person(2, "David", 33)));
    private static ForEachTemplateProcessor processor;

    @BeforeAll
    static void init() {
        processor = new ForEachTemplateProcessor(parameters);
    }

    @Test
    void testProcess() {
        String content = new TemplateCreator().create(path).getContent();
        String expectedPage = "<table>\n" +
                            "<thead>\n" +
                            "<tr>\n<th scope=\"col\">ID</th>\n<th scope=\"col\">Name</th>\n<th scope=\"col\">Age</th>\n</tr>\n" +
                            "</thead>\n" +
                            "<tbody>\n" +
                            "<tr>\n<th scope=\"row\">1</th>\n<td>Sasha</td>\n<td>41</td>\n</tr>\n" +
                            "<tr>\n<th scope=\"row\">2</th>\n<td>David</td>\n<td>33</td>\n</tr>\n" +
                            "</tbody>\n" +
                            "</table>";
        String actualPage = processor.process(content);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetParameters() {
        Map<String, String> expectedParams = Map.of("persons",
                "<#list persons as person>\n" +
                    "<tr>\n<th scope=\"row\">${person.id}</th>\n<td>${person.name}</td>\n<td>${person.age}</td>\n</tr>\n" +
                    "</#list>");
        String actualContent = new TemplateCreator().create(path, parameters).getContent();
        Map<String, String> actualParams = processor.getParameters(actualContent);
        assertEquals(expectedParams, actualParams);
    }

    @Test
    void testInsertParameters() {
        Map<String, String> expectedParams = Map.of("persons",
                "<tr>\n<th scope=\"row\">1</th>\n<td>Sasha</td>\n<td>41</td>\n</tr>\n" +
                    "<tr>\n<th scope=\"row\">2</th>\n<td>David</td>\n<td>33</td>\n</tr>");
        String actualContent = new TemplateCreator().create(path, parameters).getContent();
        Map<String, String> actualParams = processor.insertParameters(actualContent);
        assertEquals(expectedParams, actualParams);
    }

    @Test
    void testGetListFromMapValue() {
        List<String> expectedEntities = List.of("id=1, name=Sasha, age=41","id=2, name=David, age=33");
        String key = "persons";
        List<String> actualEntities = processor.getListFromMapValue(key);
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    void testGetKey() {
        String expectedKey = "persons";
        String value = "<#list persons as person>\n" +
                "<tr>\n<th scope=\"row\">${person.id}</th>\n<td>${person.name}</td>\n<td>${person.age}</td>\n</tr>\n" +
                "</#list>";
        String actualKey = processor.getKey(value);
        assertEquals(expectedKey, actualKey);
    }


}