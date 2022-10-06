package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.TemplateCreator;
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
    private static final Pattern usdBracesPattern = Pattern.compile("\\$\\{\\w.+?}");
    private final Map<String, Object> parameters;
    private final String path;

    @Override
    public String process() {
        String content = new TemplateCreator().create(path, parameters).getContent();
        String processedContent = content;
        Map<String, String> receivedParameters = getParameters(content);

        for (Map.Entry<String, String> entry : receivedParameters.entrySet()) {
            if (entry.getKey().contains(".")) {
                String className = getEntryClassName(entry);
                String fieldName = getEntryFieldName(entry);

                Object objectFromParameters = parameters.get(className);
                String classNameFormParameters = objectFromParameters.getClass().getSimpleName();
                String entityFields = objectFromParameters.toString().replace(classNameFormParameters + "(", "").replace(")", "");
                String[] fields = entityFields.split(", ");
                for (String field : fields) {
                    String replace = field.replace(fieldName + "=", "");
                    if (field.contains(fieldName)) {
                        processedContent = processedContent.replace(entry.getValue(), replace);
                    }
                }
            } else {
                processedContent = processedContent.replace(entry.getValue(), parameters.get(entry.getKey()).toString());
            }
        }
        return processedContent;
    }

    String getEntryClassName(Map.Entry<String, String> entry) {
        StringBuilder className = new StringBuilder(entry.getKey());
        className.replace(entry.getKey().indexOf("."), entry.getKey().length(), "");
        return className.toString();
    }

    String getEntryFieldName(Map.Entry<String, String> entry) {
        StringBuilder fieldName = new StringBuilder(entry.getKey());
        fieldName.replace(0, entry.getKey().indexOf(".") + 1, "");
        return fieldName.toString();
    }

    Map<String, String> getParameters(String content) {
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
