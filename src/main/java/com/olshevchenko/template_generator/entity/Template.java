package com.olshevchenko.template_generator.entity;

import lombok.*;

import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
@ToString
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Template {
    private String content;
    private Map<String, Object> parameters;
}
