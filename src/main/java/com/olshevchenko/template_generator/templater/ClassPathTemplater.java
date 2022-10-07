package com.olshevchenko.template_generator.templater;

import com.olshevchenko.template_generator.entity.Template;
import com.olshevchenko.template_generator.processor.ForEachTemplateProcessor;
import com.olshevchenko.template_generator.processor.IncludeTemplateProcessor;
import com.olshevchenko.template_generator.processor.ValueTemplateProcessor;
import com.olshevchenko.template_generator.utils.TemplateCreator;

import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class ClassPathTemplater implements Templater {

    @Override
    public String getPage(String path) {
        return getPage(path, Map.of());
    }

    @Override
    public String getPage(String path, Map<String, Object> parameters) {
        TemplateCreator creator = new TemplateCreator();
        Template template = creator.create(path, parameters);

        IncludeTemplateProcessor includeTemplateProcessor = new IncludeTemplateProcessor();
        ForEachTemplateProcessor forEachTemplateProcessor = new ForEachTemplateProcessor(parameters);
        ValueTemplateProcessor valueTemplateProcessor = new ValueTemplateProcessor(parameters);

        String processedTemplate = includeTemplateProcessor.process(template.getContent());
        processedTemplate = forEachTemplateProcessor.process(processedTemplate);
        processedTemplate = valueTemplateProcessor.process(processedTemplate);

        return processedTemplate;
    }
}
