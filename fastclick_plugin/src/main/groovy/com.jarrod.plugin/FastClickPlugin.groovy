package com.jarrod.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class FastClickPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        AppExtension appExtension = project.extensions.getByType(AppExtension)
        //增加扩展 可配置项
        project.extensions.add("FastClickExtension", FastClickExtension)
        appExtension.registerTransform(new FastClickTransform(project))
    }
}