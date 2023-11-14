package com.yomahub.akali.annotation;

import com.yomahub.akali.enums.AkaliStrategyEnum;
import com.yomahub.akali.enums.FlowGradeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AkaliFallback {

    FlowGradeEnum grade();

    int count();
}
