package com.cxs.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cxs.constant.ResponseStateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.json.JSONObject;


@ApiModel(value = "结果", description = "统一结果处理Bean")
public class BaseResult<T> {
    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "信息")
    private String msg;
    @ApiModelProperty(value = "数据")
    @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
    private T data;

    public BaseResult() {
    }

    public BaseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public BaseResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public BaseResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public BaseResult setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 操作成功
     *
     * @return
     */
    public static BaseResult ok() {
        BaseResult BaseResult = new BaseResult();
        BaseResult.setMsg("操作成功");
        BaseResult.setCode(ResponseStateConstant.OPERA_SUCCESS);
        BaseResult.setData(new JSONObject());
        return BaseResult;
    }

    public static BaseResult ok(String msg) {
        BaseResult BaseResult = new BaseResult();
        BaseResult.setCode(ResponseStateConstant.OPERA_SUCCESS);
        BaseResult.setData(new JSONObject());
        BaseResult.setMsg(msg);
        return BaseResult;
    }

    /**
     * 操作失败
     *
     * @return
     */
    public static BaseResult error() {
        BaseResult BaseResult = new BaseResult();
        BaseResult.setCode(ResponseStateConstant.OPERA_FAIL);
        BaseResult.setData(new JSONObject());
        return BaseResult;
    }

    public static BaseResult error(String msg) {
        BaseResult BaseResult = new BaseResult();
        BaseResult.setCode(ResponseStateConstant.OPERA_FAIL);
        BaseResult.setData(new JSONObject());
        BaseResult.setMsg(msg);
        return BaseResult;
    }

    public Boolean resOk(){
        return this.code == ResponseStateConstant.OPERA_SUCCESS;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
