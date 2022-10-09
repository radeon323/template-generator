package com.olshevchenko.template_generator.templater;

import com.olshevchenko.template_generator.entity.Template;
import com.olshevchenko.template_generator.processor.ForEachTemplateProcessor;
import com.olshevchenko.template_generator.processor.IncludeTemplateProcessor;
import com.olshevchenko.template_generator.processor.TemplateProcessor;
import com.olshevchenko.template_generator.processor.ValueTemplateProcessor;
import com.olshevchenko.template_generator.utils.TemplateCreator;

import java.util.List;
import java.util.Map;

/**
 * @author Oleksandr Shevchenko
 */
public class ClassPathTemplater implements Templater {
    private final TemplateCreator creator = new TemplateCreator();

    @Override
    public String getPage(String path) {
        return getPage(path, Map.of());
    }

    @Override
    public String getPage(String path, Map<String, Object> parameters) {
        Template template = creator.create(path, parameters);
        List<TemplateProcessor> processors = List.of(new IncludeTemplateProcessor(), new ForEachTemplateProcessor(), new ValueTemplateProcessor());
        for (TemplateProcessor processor : processors) {
            processor.process(template);
        }
        return template.getContent();
    }
}
