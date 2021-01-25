package com.cs.parking.controller;

import com.cs.parking.base.DTO.ResultDTO;
import com.cs.parking.base.baseinterface.ParameterVerify;
import com.cs.parking.base.baseinterface.VerifyToken;
import com.cs.parking.base.utils.ResultUtils;
import com.cs.parking.code.BaseCode;
import com.cs.parking.code.ParameterCode;
import com.cs.parking.dto.CouponsDTO;
import com.cs.parking.exception.ErrorException;
import com.cs.parking.exception.SystemException;
import com.cs.parking.pojo.Coupons;
import com.cs.parking.service.CouponsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @ClassName CouponsController
 * @Description TODO
 * @Author QQ163
 * @Date 2021/1/18 15:03
 **/
@RequestMapping("/coupons")
@Controller
@Api(tags = {"优惠卷信息操作接口"})
public class CouponsController {
    @Autowired
    CouponsService couponsService;

    @PostMapping("/newCoupons")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "添加优惠卷")
    public ResultDTO newCoupons(
            @ApiParam(value = "优惠卷简称",required = true)@RequestParam(value = "name",required = true)String name,
            @ApiParam(value = "优惠价格",required = true)@RequestParam(value = "price",required = true)Double price,
            @ApiParam(value = "数量",required = true)@RequestParam(value = "number",required = true)Integer number,
            @ApiParam(value = "优惠卷生效时间",required = true)@RequestParam(value = "startTime",required = true) Date startTime,
            @ApiParam(value = "优惠卷失效时间",required = true)@RequestParam(value = "endTime",required = true)Date endTime,
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "parkingSpaceId",required = true)Integer parkingSpaceId
    ){
        try {
            Coupons coupons = new Coupons(name,price,number,startTime,endTime,parkingSpaceId,false);
            couponsService.insert(coupons);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @PutMapping("/changeCoupons")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "修改优惠卷")
    public ResultDTO<CouponsDTO> changeCoupons(
            @ApiParam(value = "优惠卷id",required = true)@RequestParam(value = "cid",required = true)Integer cid,
            @ApiParam(value = "优惠卷简称",required = false)@RequestParam(value = "name",required = false)String name,
            @ApiParam(value = "优惠价格",required = false)@RequestParam(value = "price",required = false)Double price,
            @ApiParam(value = "数量",required = false)@RequestParam(value = "number",required = false)Integer number,
            @ApiParam(value = "优惠卷生效时间",required = false)@RequestParam(value = "startTime",required = false) Date startTime,
            @ApiParam(value = "优惠卷失效时间",required = false)@RequestParam(value = "endTime",required = false)Date endTime,
            @ApiParam(value = "车位id",required = false)@RequestParam(value = "parkingSpaceId",required = false)Integer parkingSpaceId
    ){
        try {
            Coupons coupons = couponsService.selectById(cid);
            if (coupons == null){
                throw new ErrorException(BaseCode.Null,"没有此优惠卷");
            }
            if (coupons.getStartTime().before(new Date())){
                throw new ErrorException(BaseCode.FailOperation,"优惠卷已经起效");
            }
            coupons.setName(name);
            coupons.setPrice(price);
            coupons.setNumber(number);
            coupons.setStartTime(startTime);
            coupons.setEndTime(endTime);
            coupons.setParkingSpaceId(parkingSpaceId);
            couponsService.update(coupons);
            return ResultUtils.success(new CouponsDTO(coupons));
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @DeleteMapping("/revokeCoupons")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "撤销优惠卷",notes = "已生效优惠卷无法撤销，请联系管理员")
    @ParameterVerify(parameterKey = {"cid"},parameterName = {"优惠卷id"},parameterCode = ParameterCode.CouponsParameter)
    public ResultDTO revokeCoupons(@ApiParam(value = "优惠卷id",required = true)@RequestParam(value = "cid",required = true)Integer cid){
        try {
            couponsService.delete(cid);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @DeleteMapping("/adminRevokeCoupons")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "管理员撤销优惠卷")
    @ParameterVerify(parameterKey = {"cid"},parameterName = {"优惠卷id"},parameterCode = ParameterCode.CouponsParameter)
    public ResultDTO adminRevokeCoupons(@ApiParam(value = "优惠卷id",required = true)@RequestParam(value = "cid",required = true)Integer cid){
        try {
            couponsService.adminDelete(cid);
            return ResultUtils.success();
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchCoupons")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "查询特定车位优惠卷")
    @ParameterVerify(parameterKey = {"pid"},parameterName = {"车位id"},parameterCode = ParameterCode.ParkingSpaceParameter)
    public ResultDTO<PageInfo<CouponsDTO>> SearchCoupons(
            @ApiParam(value = "车位id",required = true)@RequestParam(value = "pid",required = true)Integer pid,
            @ApiParam(value = "是否查询过期优惠卷",required = true)@RequestParam(value = "overdue",required = true)boolean overdue,
            @ApiParam(value = "起始页>=0",required = true)@RequestParam(value = "offset",required = true)Integer offset,
            @ApiParam(value = "每页数据数",required = true)@RequestParam(value = "limit",required = true)Integer limit){
        try {
            PageHelper.startPage(offset,limit);
            List<CouponsDTO> coupons = couponsService.selectByParkingSpaceId(pid,overdue);
            PageInfo<CouponsDTO> pageInfo = new PageInfo<>(coupons);
            return ResultUtils.success(pageInfo);
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }

    @GetMapping("/searchCoupon")
    @Transactional
    @VerifyToken
    @ResponseBody
    @ApiOperation(value = "查询特定优惠卷")
    public ResultDTO<CouponsDTO> SearchCoupon(@ApiParam(value = "优惠卷id",required = true)@RequestParam(value = "cid",required = true)Integer cid){
        try {
            Coupons coupons = couponsService.selectById(cid);
            if (coupons == null){
                return ResultUtils.success();
            }
            return ResultUtils.success(new CouponsDTO(coupons));
        }catch (SystemException e){
            return ResultUtils.error(e.getCode(), e.getMessage(),null);
        }catch (ErrorException e){
            return ResultUtils.error(e.getCode(),e.getMessage()+'\n'+e.getErrorMessage(),null);
        }
    }
}
