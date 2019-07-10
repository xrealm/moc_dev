package com.mao.dev_compiler.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created by Mao on 2019-07-10.
 */
public class ClassCreatorProxy {

    private String bindClassName;
    private String packageName;
    private TypeElement typeElement;
    private Map<Integer, VariableElement> variableElementMap = new HashMap<>();

    public ClassCreatorProxy(Elements elementUtils, TypeElement classElement) {
        this.typeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        this.packageName = packageElement.getQualifiedName().toString();
        this.bindClassName = classElement.getSimpleName().toString() + "_ViewBinding";
    }

    public void putElement(int id, VariableElement variableElement) {
        variableElementMap.put(id, variableElement);
    }

    public String getProxyClassFullName() {
        return packageName + "." + bindClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getPackageName() {
        return packageName;
    }

    public String generateJavaCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import com.mao.apt_lib.*;\n");
        sb.append('\n');
        sb.append("public class ").append(bindClassName);
        sb.append(" {\n");

        generateMethod(sb);
        sb.append('\n');
        sb.append("}\n");
        return sb.toString();
    }

    private void generateMethod(StringBuilder sb) {
        sb.append("public void bind(" + typeElement.getQualifiedName() + " host ) {\n");
        for (int id : variableElementMap.keySet()) {
            VariableElement element = variableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
            sb.append("host." + name).append(" = ");
            sb.append("(" + type + ")(((android.app.Activity)host).findViewById( " + id + "));\n");
        }
        sb.append("  }\n");
    }

    public TypeSpec generateJavaCodeByPoet() {

        ClassName host = ClassName.bestGuess(typeElement.getQualifiedName().toString());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(host, "host");

        for (Map.Entry<Integer, VariableElement> entry : variableElementMap.entrySet()) {
            Integer id = entry.getKey();
            VariableElement element = variableElementMap.get(id);
            String name = element.getSimpleName().toString();
            String type = element.asType().toString();
//            builder.addStatement("host.$S = ($S)(((android.app.Activity)host).findViewById($D));", name, type, id);
            builder.addStatement("host." + name + " = " + "(" + type + ")(((android.app.Activity)host).findViewById( " + id + "));\n");

        }
        MethodSpec methodSpec = builder.build();

        TypeSpec typeSpec = TypeSpec.classBuilder(bindClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec)
                .build();
        return typeSpec;
    }
}
