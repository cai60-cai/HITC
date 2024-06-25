package com.cxs.exception;

import com.cxs.enums.CurrencyErrorEnum;
import lombok.Data;

  
@Data
public class CurrencyException extends RuntimeException{
    private Integer code;
    public CurrencyException() {
        super();
    }

    public CurrencyException(CurrencyErrorEnum errorEnum) {
        super(errorEnum.getMsg());
        this.code = errorEnum.getCode();

    }
}
