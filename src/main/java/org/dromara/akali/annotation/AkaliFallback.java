package org.dromara.akali.annotation;

import org.dromara.akali.enums.FlowGradeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AkaliFallback {

    FlowGradeEnum grade();

    int count();
}
