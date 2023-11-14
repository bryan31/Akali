package com.yomahub.akali.annotation;

import com.yomahub.akali.enums.FlowGradeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AkaliHot {

    FlowGradeEnum grade();

    int count();

    int duration();


}