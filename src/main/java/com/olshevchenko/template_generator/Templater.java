package com.olshevchenko.template_generator;

import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public interface Templater {
    String getPage(String path);
    String getPage(String path, Map<String, String> parameters);
}
