package com.mao.dev_compiler.processor;

import com.google.auto.service.AutoService;
import com.mao.dev_annotation.BindView;
import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Created by Mao on 2017/12/11.
 */

@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Elements mElementUtils;
    private Map<String, ClassCreatorProxy> mProxyMap = new HashMap<>();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new HashSet<>();
        supportTypes.add(BindView.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "processing....");
        mProxyMap.clear();

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(BindView.class);
        for (Element element : elements) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement classElement = (TypeElement) variableElement.getEnclosingElement();
            String fullClassName = classElement.getQualifiedName().toString();
            ClassCreatorProxy proxy = mProxyMap.get(fullClassName);
            if (proxy == null) {
                proxy = new ClassCreatorProxy(mElementUtils, classElement);
                mProxyMap.put(fullClassName, proxy);
            }
            BindView bindAnnotation = variableElement.getAnnotation(BindView.class);
            int id = bindAnnotation.value();
            proxy.putElement(id, variableElement);
        }
        //创建java文件
//        for (Map.Entry<String, ClassCreatorProxy> entry : mProxyMap.entrySet()) {
//            ClassCreatorProxy proxy = entry.getValue();
//            mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxy.getProxyClassFullName());
//            try {
//                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(proxy.getProxyClassFullName(), proxy.getTypeElement());
//                Writer writer = jfo.openWriter();
//                writer.write(proxy.generateJavaCode());
//                writer.flush();
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> crate " + proxy.getProxyClassFullName() + " error");
//            }
//        }

        for (Map.Entry<String, ClassCreatorProxy> entry : mProxyMap.entrySet()) {
            ClassCreatorProxy proxy = entry.getValue();
            mMessager.printMessage(Diagnostic.Kind.NOTE, " --> create " + proxy.getProxyClassFullName());
            JavaFile javaFile = JavaFile.builder(proxy.getPackageName(), proxy.generateJavaCodeByPoet()).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
                mMessager.printMessage(Diagnostic.Kind.NOTE, " --> crate " + proxy.getProxyClassFullName() + " error");
            }
        }
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process finish ...");
        return true;
    }
}
