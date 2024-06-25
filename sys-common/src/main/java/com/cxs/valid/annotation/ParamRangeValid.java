package com.cxs.valid.annotation;

import com.cxs.valid.RangeConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RangeConstraintValidator.class)
public @interface ParamRangeValid {
	String[] ranges();
    String message() default "范围不允许";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}