package com.cxs.handler;

import com.cxs.base.BaseResult;
import com.cxs.constant.ResponseStateConstant;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.exception.CurrencyException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;


@RestControllerAdvice
@Order(1)
public class CurrencyExceptionHandler {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public CurrencyExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @ExceptionHandler(Exception.class)
    public BaseResult currencyExceptionHandle(Exception e) {
        BaseResult error = BaseResult.error();
        error.setData(new JSONObject());
        if (e instanceof CurrencyException) {
            CurrencyException ex = (CurrencyException) e;
            error.setMsg(ex.getMessage());
            error.setCode(ex.getCode());
        } else if(e instanceof BindException) {
            BindException ex = (BindException) e;
            BindingResult bindingResult = ex.getBindingResult();
            error.setMsg(bindingResult.getFieldError().getDefaultMessage());
            error.setCode(ResponseStateConstant.PARAMETER_ERROR);
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            BindingResult bindingResult = ex.getBindingResult();
            error.setMsg(bindingResult.getFieldError().getDefaultMessage());
            error.setCode(ResponseStateConstant.PARAMETER_ERROR);
        } else if (e instanceof ValidationException) {
            ValidationException ex = (ValidationException) e;
            error.setMsg(ex.getMessage());
            error.setCode(ResponseStateConstant.PARAMETER_ERROR);
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            error.setMsg(ex.getMessage());
            error.setCode(ResponseStateConstant.PARAMETER_ERROR);
        } else if (e instanceof SignatureException) {
            error.setMsg(CurrencyErrorEnum.UNAUTHORIZED.getMsg());
            error.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
        } else if (e instanceof ExpiredJwtException) {
            error.setMsg(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getMsg());
            error.setCode(CurrencyErrorEnum.UNAUTHORIZED_BE_OVERDUE.getCode());
        } else if (e instanceof JwtException) {
            error.setMsg(CurrencyErrorEnum.UNAUTHORIZED.getMsg());
            error.setCode(CurrencyErrorEnum.UNAUTHORIZED.getCode());
        } else if (e instanceof AccessDeniedException) {
            error.setMsg("403 Forbidden");
            error.setCode(ResponseStateConstant.NO_PROMISSION);
        } else if (e instanceof MissingServletRequestParameterException) {
            error.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg());
            error.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
        } else {
            error.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
            error.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
        }
        return error;
    }
}
