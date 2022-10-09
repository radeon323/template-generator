package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.entity.Template;
import com.olshevchenko.template_generator.utils.TemplateCreator;
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
    private static final Pattern includePattern = Pattern.compile("<#includ\\w.+?>");

    @Override
    public void process(Template template) {
        String content = template.getContent();
        String processedContent = content;
        Map<String, String> receivedParameters = getParameters(content);
        for (Map.Entry<String, String> entry : receivedParameters.entrySet()) {
            String path = entry.getKey();
            String contentToReplace = new TemplateCreator().create(path).getContent();
            processedContent = processedContent.replace(entry.getValue(), contentToReplace);
        }
        template.setContent(processedContent);
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
