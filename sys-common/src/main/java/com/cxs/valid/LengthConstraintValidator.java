package com.cxs.valid;

import com.cxs.valid.annotation.ParamLengthValid;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LengthConstraintValidator implements ConstraintValidator<ParamLengthValid,String> {

	private boolean onlyChinese = false;

	private Integer maxlength = 0;

	private static final String pattern = "[\\u4e00-\\u9fa5]+";

    @Override
    public void initialize(ParamLengthValid paramLengthValidAnnotation) {
        onlyChinese = paramLengthValidAnnotation.onlyChinese();
        maxlength = paramLengthValidAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (StringUtils.hasLength(s)) {
            return characterLengthValid(s.trim());
        } else {
            return true;
        }
    }

    private boolean characterLengthValid(String s) {
        if (onlyChinese) {
            Pattern r = Pattern.compile(pattern);
            StringBuilder sb = new StringBuilder();
            Matcher m = r.matcher(s);
            while (m.find()) {
                sb.append(m.group());
            }
            return sb.toString().length() <= maxlength;
        } else {
            return s.length() <= maxlength;
        }
    }
}