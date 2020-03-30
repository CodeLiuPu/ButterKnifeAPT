package com.update.apt_compiler.processor;


import com.google.auto.service.AutoService;
import com.update.apt_annotation.BindView;
import com.update.apt_compiler.Consts;
import com.update.apt_compiler.utils.Log;
import com.update.apt_compiler.utils.Utils;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.annotation.processing.Filer;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


@AutoService(Processor.class) // 注册注解处理器

/**
 * 指定使用的 Java版本 替代 {@link AbstractProcessor#getSupportedSourceVersion()} 函数
 * 声明我们注解支持的JDK版本
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)

/**
 * 注册给那些注解的 替代 {@link AbstractProcessor#getSupportedAnnotationTypes()} 函数
 * 声明我们要处理哪一些注解 该方法返回字符串的集合标识该处理器用于处理哪些注解
 */
@SupportedAnnotationTypes({Consts.ANN_TYPE_BIND})
public class ButterKnifeProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Filer filer;
    private Log log;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        log = Log.newLog(processingEnv.getMessager());
        log.i("init()");
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.i("process()");
        // 使用了需要处理的注解
        if (Utils.isNotEmpty(annotations)) {
            // 获取所有被 BindView注解 的元素集合
            Set<? extends Element> routeElements = roundEnv.getElementsAnnotatedWith(BindView.class);
            if (Utils.isNotEmpty(routeElements)) {
                log.i("BindView Class === " + routeElements.size());
//                parseBindView(routeElements);
            }
            return true;
        }
        return false;
    }
// https://github.com/jsntjinjin/ButterKnifeDemo/blob/master/butterknife-compiler/src/main/java/com/fastaoe/butterknife/compile/ButterKnifeProcessor.java
    private void parseBindView(Set<? extends Element> routeElements) {
        // 1. 将获取到的bindview细分到每个class
        Map<Element, List<Element>> elementMap = new LinkedHashMap<>();

        for (Element element : routeElements) {
            // 返回activity
            Element enclosingElement = element.getEnclosingElement();

            List<Element> bindViewElements = elementMap.get(enclosingElement);
            if (Utils.isEmpty(bindViewElements)) {
                bindViewElements = new ArrayList<>();
                elementMap.put(enclosingElement, bindViewElements);
            }
            bindViewElements.add(element);
        }

        // 2. 生成代码









    }
}
