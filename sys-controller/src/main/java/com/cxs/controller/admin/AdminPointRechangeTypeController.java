package com.cxs.controller.admin;

import com.cxs.aspect.annotation.MarkLog;
import com.cxs.base.BaseResult;
import com.cxs.dto.admin.point.SaveOrUpdatePointRechargeTypeDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.service.PointRechargeTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.text.NumberFormat;


@RestController
@RequestMapping("/admin/rechangeType")
@Api(tags = "管理员积分充值控制器")
public class AdminPointRechangeTypeController {

    @Autowired
    private PointRechargeTypeService pointRechargeTypeService;

    @GetMapping("/getPointRechargeTypeList")
    @ApiOperation("获取积分充值类型处理器")
    public BaseResult adminGetPointRechargeTypeList(HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        pointRechargeTypeService.adminGetPointRechargeTypeList(request, result);
        return result;
    }

    @DeleteMapping("/removePointRechargeType/{id}")
    @ApiOperation("删除积分充值类型处理器")
    @MarkLog(desc = "管理员删除积分充值类型")
    public BaseResult removePointRechargeType(@PathVariable("id") Integer id, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        pointRechargeTypeService.removePointRechargeType(id, request, result);
        return result;
    }

    @PostMapping("/saveOrPointRechargeType")
    @ApiOperation("新增或修改积分充值类型处理器")
    @MarkLog(desc = "管理员新增或修改积分充值类型")
    public BaseResult saveOrUpdatePointRechargeType(@RequestBody @Validated SaveOrUpdatePointRechargeTypeDTO dto, HttpServletRequest request){
        BaseResult result = BaseResult.ok();
        if (!saveOrUpdatePointRechargeTypeParamHandle(dto, result)) {
            return result;
        }
        Integer id = dto.getId();
        if (ObjectUtils.isEmpty(id)) {
            pointRechargeTypeService.adminSavePointRechargeType(dto, request, result);
        } else {
            pointRechargeTypeService.adminUpdatePointRechargeType(dto, request, result);
        }
        return result;
    }

    private boolean saveOrUpdatePointRechargeTypeParamHandle(SaveOrUpdatePointRechargeTypeDTO dto, BaseResult result) {
        Double discount = dto.getDiscount();
        if (!ObjectUtils.isEmpty(discount)) {
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMaximumFractionDigits(1);
            format.setRoundingMode(RoundingMode.DOWN);
            dto.setDiscount(new Double(format.format(discount)));
            if (dto.getDiscount() < 0.1D || dto.getDiscount() > 10.0D) {
                result.setCode(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.REQUEST_PARAMETER_ERROR.getMsg() + ",discount范围在0.1-10.0之间");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Double d = new Double("9.57212122");
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);
        format.setRoundingMode(RoundingMode.DOWN);
        System.out.println(format.format(d));
    }
}
