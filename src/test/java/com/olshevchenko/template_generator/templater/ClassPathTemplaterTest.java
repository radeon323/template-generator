package com.olshevchenko.template_generator.templater;

import com.olshevchenko.template_generator.processor.entity.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oleksandr Shevchenko
 */
class ClassPathTemplaterTest {
    private static final String path = "src/test/resources/test.html";
    private static final String pathW = "src/test/resources/include.html";
    private static final Map<String, Object> parameters = new HashMap<>();
    private static ClassPathTemplater classPathTemplater;


    @BeforeAll
    static void init() {
        parameters.put("persons", List.of(new Person(1, "Sasha", 41), new Person(2, "David", 33)));
        parameters.put("person", new Person(3, "Vasil", 20));
        classPathTemplater = new ClassPathTemplater();
    }

    @Test
    void testGetPage() {
        String expectedPage = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Title</title>
                </head>
                <body>

                Hello
                <br>
                <h1>i'm Person(id=3, name=Vasil, age=20)</h1>

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
                </table>


                </body>
                </html>""";
        String actualPage = classPathTemplater.getPage(path, parameters);
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testGetPageWithoutParameters() {
        String expectedPage = "Hello";
        String actualPage = classPathTemplater.getPage(pathW);
        assertEquals(expectedPage, actualPage);
    }
}