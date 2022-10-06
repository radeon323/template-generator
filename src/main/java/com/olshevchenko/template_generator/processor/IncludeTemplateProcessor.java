package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.utils.TemplateCreator;
import com.olshevchenko.template_generator.entity.Template;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oleksandr Shevchenko
 */
@AllArgsConstructor
public class IncludeTemplateProcessor implements TemplateProcessor {
    private static final Pattern includePattern = Pattern.compile("<#\\w.+?>");

    @Override
    public String process(String content) {
        String processedContent = content;
        Map<String, String> receivedParameters = getParameters(content);
        for (Map.Entry<String, String> entry : receivedParameters.entrySet()) {
            String path = entry.getKey();
            Template template = new TemplateCreator().create(path);
            processedContent = processedContent.replace(entry.getValue(), template.getContent());
        }
        return processedContent;
    }

    Map<String, String> getParameters(String content) {
        Matcher matcherInclude = includePattern.matcher(content);
        Map<String, String> parameters = new HashMap<>();
        while (matcherInclude.find()) {
            String value = matcherInclude.group();
            String key = value.replaceAll("<#include\\s", "").replaceAll(">", "").replaceAll("\"", "");
            parameters.put(key,value);
        }
        return parameters;
    }
}
