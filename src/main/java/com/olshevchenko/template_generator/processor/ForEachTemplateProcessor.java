package com.olshevchenko.template_generator.processor;

import com.olshevchenko.template_generator.entity.Template;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oleksandr Shevchenko
 */
@AllArgsConstructor
public class ForEachTemplateProcessor implements TemplateProcessor {
    private static final Pattern listFinderPattern = Pattern.compile("<#list .+>(\\D|\\w)*?#list>");
    private static final Pattern listHeaderFinderPattern = Pattern.compile("<#list .+ as");
    private static final Pattern entityFromListPattern = Pattern.compile("\\(\\w.+?\\)");

    @Override
    public void process(Template template) {
        String content = template.getContent();
        String processedContent = content;
        Map<String, String> receivedParameters = getParameters(content);
        Map<String, String> insertedParameters = insertParameters(template);
        for (Map.Entry<String, String> entry : receivedParameters.entrySet()) {
            processedContent = processedContent.replace(entry.getValue(), insertedParameters.get(entry.getKey()));
        }
        template.setContent(processedContent);
    }

    Map<String, String> getParameters(String content) {
        Matcher matcherList = listFinderPattern.matcher(content);
        Map<String, String> params = new HashMap<>();
        while (matcherList.find()) {
            String value = matcherList.group();
            String key = getKey(value);
            params.put(key,value);
        }
        return params;
    }

    Map<String, String> insertParameters(Template template) {
        String content = template.getContent();
        Matcher matcherList = listFinderPattern.matcher(content);
        Map<String, String> params = new HashMap<>();

        while (matcherList.find()) {
            String value = matcherList.group();
            String key = getKey(value);
            List<String> entities = getListFromMapValue(key, template.getParameters());

            StringBuilder result = new StringBuilder();
            for (String entity : entities) {
                Map<String, Object> substring = Map.of(key.substring(0, key.length() - 1), entity);
                ValueTemplateProcessor processor = new ValueTemplateProcessor();
                Template processedTemplate = new Template(value, substring);
                processor.process(processedTemplate);
                String singleSub = processedTemplate.getContent().replaceAll("<#list .+>", "").replaceAll("</#list>", "").trim();
                result.append(singleSub).append("\n");
            }
            params.put(key, result.toString().trim());
        }
        return params;
    }

    List<String> getListFromMapValue(String key, Map<String, Object> parameters) {
        String stringFromMapOfLists = parameters.get(key).toString().replace("[","").replace("]","");
        List<String> entities = new ArrayList<>();
        Matcher match = entityFromListPattern.matcher(stringFromMapOfLists);
        while(match.find()) {
            entities.add(match.group().replace("(","").replace(")",""));
        }
        return entities;
    }

    String getKey(String value) {
        Matcher matcherHeader = listHeaderFinderPattern.matcher(value);
        String key = "";
        while (matcherHeader.find()) {
            key = matcherHeader.group().replace("<#list ", "").replace(" as", "").trim();
        }
        return key;
    }


}
