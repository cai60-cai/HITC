package com.cxs.valid.annotation;

import com.cxs.valid.LengthConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LengthConstraintValidator.class)
public @interface ParamLengthValid {
	boolean onlyChinese() default false;
	int max();
    String message() default "字段长度不匹配";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}