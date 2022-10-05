package com.olshevchenko.template_generator.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Template {
    private final String content;
    private final Map<String, Object> parameters;
}
