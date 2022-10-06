package com.olshevchenko.template_generator.processor;

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
    private final Map<String, Object> parameters;

    @Override
    public String process(String content) {
        String processedContent = content;
        Map<String, String> receivedParameters = getParameters(content);
        Map<String, String> insertedParameters = insertParameters(content);
        for (Map.Entry<String, String> entry : receivedParameters.entrySet()) {
            processedContent = processedContent.replace(entry.getValue(), insertedParameters.get(entry.getKey()));
        }
        return processedContent;
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

    Map<String, String> insertParameters(String content) {
        Matcher matcherList = listFinderPattern.matcher(content);
        Map<String, String> params = new HashMap<>();
        while (matcherList.find()) {
            String value = matcherList.group();
            String key = getKey(value);
            List<String> entities = getListFromMapValue(key);

            StringBuilder result = new StringBuilder();
            for (String entity : entities) {
                Map<String, Object> substring = Map.of(key.substring(0, key.length() - 1), entity);
                ValueTemplateProcessor processor = new ValueTemplateProcessor(substring);
                String singleSub = processor.process(value).replaceAll("<#list .+>", "").replaceAll("</#list>", "").trim();
                result.append(singleSub).append("\n");
            }

            params.put(key, result.toString().trim());
        }
        return params;
    }

    List<String> getListFromMapValue(String key) {
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
