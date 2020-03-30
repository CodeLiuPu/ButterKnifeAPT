package com.update.apt_compiler.processor;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import com.update.apt_annotation.BindView;
import com.update.apt_compiler.Constants;
import com.update.apt_compiler.utils.Log;
import com.update.apt_compiler.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import jdk.nashorn.internal.runtime.ConsString;

/**
 * 用来生成 META-INF/services/javax.annotation.processing.Processor 文件
 */
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
@SupportedAnnotationTypes({Constants.ANN_TYPE_BIND})
public class ButterKnifeProcessor extends AbstractProcessor {

    // 操作Element工具类 (类、函数、属性都是Element)
    private Elements elementUtils;

    // 文件生成器 类/资源，Filter用来创建新的类文件，class文件以及辅助文件
    private Filer filer;

    // 用来报告错误，警告和其他提示信息
    private Log log;

    // key:类节点, value:被@BindView注解的属性集合
    private Map<TypeElement, List<VariableElement>> tempBindViewMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        log = Log.newLog(processingEnv.getMessager());
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        log.i("ButterKnifeProcessor init Completed");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // 使用了需要处理的注解
        if (Utils.isNotEmpty(annotations)) {
            log.i("ButterKnifeProcessor process start");

            // 获取所有被 BindView注解 的元素集合
            Set<? extends Element> bindViewElements = roundEnv.getElementsAnnotatedWith(BindView.class);
            if (Utils.isNotEmpty(bindViewElements)) {
                collectBindView(bindViewElements);
                createBindViewJavaFile();
            }
            return true;
        }
        return false;
    }

    // 将获取到的bindview细分到每个class
    private void collectBindView(Set<? extends Element> elements) {
        for (Element element : elements) {
            // 将 节点 转换为 属性节点
            VariableElement fieldElement = (VariableElement) element;
            // 获取 节点 所在的 类节点
            TypeElement enclosingElement = (TypeElement) fieldElement.getEnclosingElement();

            log.i("@BindView class >>> " + enclosingElement.getSimpleName() + ", field >>> " + fieldElement.getSimpleName());

            if (tempBindViewMap.containsKey(enclosingElement)) {
                tempBindViewMap.get(enclosingElement).add(fieldElement);
            } else {
                List<VariableElement> fields = new ArrayList<>();
                fields.add(fieldElement);
                tempBindViewMap.put(enclosingElement, fields);
            }
        }
    }

    // 生成代码
    private void createBindViewJavaFile() {
        // 判断是否有需要生成的类文件
        if (Utils.isNotEmpty(tempBindViewMap)) {
            // public final class xxxActivity_ViewBinding implements Unbinder
            // 获取ViewBinder接口类型（生成类文件需要实现的接口）
//            TypeElement viewBinderType = elementUtils.getTypeElement(Constants.VIEWBINDER);

            TypeElement viewType = elementUtils.getTypeElement(Constants.VIEW);

            for (Map.Entry<TypeElement, List<VariableElement>> entrySet : tempBindViewMap.entrySet()) {
                TypeElement enclosingElement = entrySet.getKey();
                List<VariableElement> bindViewElements = entrySet.getValue();

                String activityClassNameStr = enclosingElement.getSimpleName().toString();
                ClassName activityClassName = ClassName.bestGuess(activityClassNameStr);
                TypeSpec.Builder classBuilder =
                        TypeSpec.classBuilder(activityClassNameStr + "_ViewBinding")
                                .addModifiers(Modifier.FINAL, Modifier.PUBLIC)
                                .addField(activityClassName, "target", Modifier.PRIVATE);

                // 获取包名
                String packageName = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();

                try {
                    JavaFile.builder(packageName, classBuilder.build())
                            .addFileComment("This file is Auto generated by ButterKnife, do not modify ")
                            .build().writeTo(filer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
