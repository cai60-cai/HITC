package com.cxs.valid;

import com.cxs.valid.annotation.ParamRangeValid;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RangeConstraintValidator implements ConstraintValidator<ParamRangeValid, Object> {

    private String[] ranges = new String[]{};

    @Override
    public void initialize(ParamRangeValid paramRangeValid) {
        this.ranges = paramRangeValid.ranges();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (!ObjectUtils.isEmpty(ranges) && ranges.length > 0 && !ObjectUtils.isEmpty(value)) {
            List<String> rangeList = Arrays.stream(ranges).collect(Collectors.toList());
            if (rangeList.contains(value instanceof Integer ? value + "" : value instanceof String ? value : value)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}