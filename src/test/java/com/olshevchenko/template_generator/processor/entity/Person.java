package com.olshevchenko.template_generator.processor.entity;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * @author Oleksandr Shevchenko
 */
@AllArgsConstructor
@ToString
public
class Person {
    private int id;
    private String name;
    private int age;
}
