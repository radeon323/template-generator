package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.entity.Template;
import com.olshevchenko.template_generator.processor.entity.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ForEachTemplateProcessorTest {
    private static final String content = """
            <table>
            <thead>
            <tr>
            <th scope="col">ID</th>
            <th scope="col">Name</th>
            <th scope="col">Age</th>
            </tr>
            </thead>
            <tbody>
            <#list persons as person>
            <tr>
            <th scope="row">${person.id}</th>
            <td>${person.name}</td>
            <td>${person.age}</td>
            </tr>
            </#list>
            </tbody>
            </table>""";
    private static final Map<String, Object> parameters = Map.of("persons", List.of(new Person(1, "Sasha", 41), new Person(2, "David", 33)));
    private static ForEachTemplateProcessor processor;
    private static Template template;

    @BeforeAll
    static void init() {
        processor = new ForEachTemplateProcessor();
        template = new Template(content, parameters);
    }

    @Test
    void testProcess() {
        String expectedPage = """
                <table>
                <thead>
                <tr>
                <th scope="col">ID</th>
                <th scope="col">Name</th>
                <th scope="col">Age</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                <th scope="row">1</th>
                <td>Sasha</td>
                <td>41</td>
                </tr>
                <tr>
                <th scope="row">2</th>
                <td>David</td>
                <td>33</td>
                </tr>
                </tbody>
                </table>""";
        processor.process(template);
        assertEquals(expectedPage, template.getContent());
    }

    @Test
    void testGetParameters() {
        Map<String, String> expectedParams = Map.of("persons",
                """
                        <#list persons as person>
                        <tr>
                        <th scope="row">${person.id}</th>
                        <td>${person.name}</td>
                        <td>${person.age}</td>
                        </tr>
                        </#list>""");
        Map<String, String> actualParams = processor.getParameters(content);
        assertEquals(expectedParams, actualParams);
    }

    @Test
    void testInsertParameters() {
        Map<String, String> expectedParams = Map.of("persons",
                """
                        <tr>
                        <th scope="row">1</th>
                        <td>Sasha</td>
                        <td>41</td>
                        </tr>
                        <tr>
                        <th scope="row">2</th>
                        <td>David</td>
                        <td>33</td>
                        </tr>""");
        Map<String, String> actualParams = processor.insertParameters(new Template(content, parameters));
        assertEquals(expectedParams, actualParams);
    }

    @Test
    void testGetListFromMapValue() {
        List<String> expectedEntities = List.of("id=1, name=Sasha, age=41","id=2, name=David, age=33");
        String key = "persons";
        List<String> actualEntities = processor.getListFromMapValue(key,parameters);
        assertEquals(expectedEntities, actualEntities);
    }

    @Test
    void testGetKey() {
        String expectedKey = "persons";
        String value = """
                <#list persons as person>
                <tr>
                <th scope="row">${person.id}</th>
                <td>${person.name}</td>
                <td>${person.age}</td>
                </tr>
                </#list>""";
        String actualKey = processor.getKey(value);
        assertEquals(expectedKey, actualKey);
    }


}