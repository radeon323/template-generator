package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.TemplateCreator;
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
public class ValueTemplateProcessor implements TemplateProcessor {
    private final String path;
    private final Map<String, Object> parameters;
    private final Pattern usdBracesPattern = Pattern.compile("\\$\\{\\w.+?\\}");

    @Override
    public String process() {
        TemplateCreator creator = new TemplateCreator();
        Template template = creator.create(path, parameters);
        String content = template.getContent();

        String processedContent = content;
        Map<String, String> receivedParameters = getParameters(content);

        for (Map.Entry<String, String> entry : receivedParameters.entrySet()) {
            if (entry.getKey().contains(".")) {
                StringBuilder className = new StringBuilder(entry.getKey());
                className.replace(entry.getKey().indexOf("."), entry.getKey().length(), "");

                StringBuilder fieldName = new StringBuilder(entry.getKey());
                fieldName.replace(0, entry.getKey().indexOf(".") + 1, "");

                Object objectFromParameters = parameters.get(className.toString());
                String simpleName = objectFromParameters.getClass().getSimpleName();
                String objectName = objectFromParameters.toString().replace(simpleName + "(", "").replace(")", "");
                String field = fieldName.toString();

                String[] sentences = objectName.split(", ");
                for (String s : sentences) {
                    String replace = s.replace(field + "=", "");
                    if (s.contains(field)) {
                        processedContent = processedContent.replace(entry.getValue(), replace);
                    }
                }
            } else {
                processedContent = processedContent.replace(entry.getValue(), parameters.get(entry.getKey()).toString());
            }
        }
        return processedContent;
    }

    private Map<String, String> getParameters(String content) {
        Matcher matcherBracket = usdBracesPattern.matcher(content);
        Map<String, String> parameters = new HashMap<>();
        while (matcherBracket.find()) {
            String value = matcherBracket.group();
            String key = value.replaceAll("\\$\\{", "").replaceAll("}", "");
            parameters.put(key,value);
        }
        return parameters;
    }
}
