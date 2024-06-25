package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.dto.admin.technology.CreateOrUpdateTypeDTO;
import com.cxs.dto.admin.technology.GetFirstTypeListDTO;
import com.cxs.dto.admin.technology.GetSecondTypeListDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.NavLink;
import com.cxs.model.TechnologyType;
import com.cxs.service.TechnologyTypeService;
import com.cxs.mapper.TechnologyTypeMapper;
import com.cxs.utils.RedisUtil;
import com.cxs.vo.admin.technology.AdminTechnologyTypeItemVO;
import com.cxs.vo.admin.technology.AdminTechnologyTypeVO;
import com.cxs.vo.main.NavLinkVO;
import com.cxs.vo.technology.TechnologyTypeItemVO;
import com.cxs.vo.technology.TechnologyTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Slf4j
public class TechnologyTypeServiceImpl extends ServiceImpl<TechnologyTypeMapper, TechnologyType> implements TechnologyTypeService{

    @Autowired
    private TechnologyTypeMapper technologyTypeMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public void getTechnologyTypeList(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<TechnologyTypeVO> voList = new ArrayList<>();
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_TECHNOLOGY_LIST);
            if (!ObjectUtils.isEmpty(value)) {
                voList = JSON.parseArray(JSON.toJSONString(value), TechnologyTypeVO.class);
            } else {
                QueryWrapper<TechnologyType> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("type_del", 0);
                List<TechnologyType> technologyTypeList = technologyTypeMapper.selectList(queryWrapper);
                if (CollectionUtils.isEmpty(technologyTypeList)) {
                    result.setData(new ArrayList<>(0));
                } else {
                    Map<Integer, TechnologyTypeVO> map = new HashMap<>();
                    ListIterator<TechnologyType> iterator = technologyTypeList.listIterator();
                    while (iterator.hasNext()) {
                        TechnologyType next = iterator.next();
                        if (next.getTypeParentId().equals(-1)) {
                            TechnologyTypeVO vo = new TechnologyTypeVO();
                            BeanUtils.copyProperties(next, vo);
                            map.put(next.getId(), vo);
                            iterator.remove();
                        }
                    }
                    for (TechnologyType technologyType : technologyTypeList) {
                        TechnologyTypeVO vo = map.get(technologyType.getTypeParentId());
                        if (!ObjectUtils.isEmpty(vo)) {
                            TechnologyTypeItemVO itemVo = new TechnologyTypeItemVO();
                            BeanUtils.copyProperties(technologyType, itemVo);
                            vo.getChildren().add(itemVo);
                        }
                    }
                    for (Map.Entry<Integer, TechnologyTypeVO> entry : map.entrySet()) {
                        voList.add(entry.getValue());
                    }
                    if (!CollectionUtils.isEmpty(voList)) {
                        redisUtil.set(CachePrefixContent.INDEX_TECHNOLOGY_LIST, voList, commonConfig.getCacheShortTime(), TimeUnit.MINUTES);
                    }
                }
            }
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取技术分类列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取技术分类列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getFirstTypeList(GetFirstTypeListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            QueryWrapper<TechnologyType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type_del", 0)
                    .eq("type_parent_id", -1);
            if (StringUtils.hasLength(dto.getKeyWord())) {
                queryWrapper.like("type_name", dto.getKeyWord());
            }
            List<TechnologyType> technologyTypeList = technologyTypeMapper.selectList(queryWrapper);
            List<TechnologyTypeItemVO> voList = CollectionUtils.isEmpty(technologyTypeList) ? new ArrayList<>(0) :
                    technologyTypeList.stream().map(r -> {
                        TechnologyTypeItemVO vo = new TechnologyTypeItemVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取技术分类一级列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取技术分类一级列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getSecondTypeList(GetSecondTypeListDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            QueryWrapper<TechnologyType> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type_del", 0)
                    .eq("type_parent_id", dto.getNavId());
            if (StringUtils.hasLength(dto.getKeyWord())) {
                queryWrapper.like("type_name", dto.getKeyWord());
            }
            List<TechnologyType> technologyTypeList = technologyTypeMapper.selectList(queryWrapper);
            List<TechnologyTypeItemVO> voList = CollectionUtils.isEmpty(technologyTypeList) ? new ArrayList<>(0) :
                    technologyTypeList.stream().map(r -> {
                        TechnologyTypeItemVO vo = new TechnologyTypeItemVO();
                        BeanUtils.copyProperties(r, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取技术分类二级列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取技术分类二级列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void createType(CreateOrUpdateTypeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            if (!ObjectUtils.isEmpty(dto.getTypeParentId()) && !dto.getTypeParentId().equals(-1)) {
                TechnologyType type = technologyTypeMapper.selectById(dto.getTypeParentId());
                if (ObjectUtils.isEmpty(type) || type.getTypeDel().equals(1)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",一级分类不存在");
                    return;
                }
            }
            TechnologyType type = new TechnologyType();
            BeanUtils.copyProperties(dto, type);
            int insert = 0;
            try {
                insert = technologyTypeMapper.insert(type);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",分类名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",创建技术分类失败");
            }
        } catch (Exception e) {
            log.error("创建技术分类失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【创建技术分类接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateType(CreateOrUpdateTypeDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            if (!ObjectUtils.isEmpty(dto.getTypeParentId()) && !dto.getTypeParentId().equals(-1)) {
                TechnologyType type = technologyTypeMapper.selectById(dto.getTypeParentId());
                if (ObjectUtils.isEmpty(type) || type.getTypeDel().equals(1)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",一级分类不存在");
                    return;
                }
            } else {
                dto.setTypeParentId(-1);
            }
            TechnologyType type = new TechnologyType();
            BeanUtils.copyProperties(dto, type);
            int update = 0;
            try {
                update = technologyTypeMapper.updateById(type);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",分类名称已存在");
                return;
            }
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改技术分类失败");
            }
        } catch (Exception e) {
            log.error("修改技术分类失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【修改技术分类接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void getAdminTechnologyTypeList(HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            QueryWrapper<TechnologyType> queryWrapper = new QueryWrapper<>();
            List<TechnologyType> technologyTypeList = technologyTypeMapper.selectList(queryWrapper);
            if (CollectionUtils.isEmpty(technologyTypeList)) {
                result.setData(new ArrayList<>(0));
            } else {
                Map<Integer, AdminTechnologyTypeVO> map = new HashMap<>();
                ListIterator<TechnologyType> iterator = technologyTypeList.listIterator();
                while (iterator.hasNext()) {
                    TechnologyType next = iterator.next();
                    if (next.getTypeParentId().equals(-1)) {
                        AdminTechnologyTypeVO vo = new AdminTechnologyTypeVO();
                        BeanUtils.copyProperties(next, vo);
                        map.put(next.getId(), vo);
                        iterator.remove();
                    }
                }
                for (TechnologyType technologyType : technologyTypeList) {
                    AdminTechnologyTypeVO vo = map.get(technologyType.getTypeParentId());
                    if (!ObjectUtils.isEmpty(vo)) {
                        AdminTechnologyTypeItemVO itemVo = new AdminTechnologyTypeItemVO();
                        BeanUtils.copyProperties(technologyType, itemVo);
                        vo.getChildren().add(itemVo);
                    }
                }

                List<AdminTechnologyTypeVO> voList = new ArrayList<>();
                for (Map.Entry<Integer, AdminTechnologyTypeVO> entry : map.entrySet()) {
                    voList.add(entry.getValue());
                }

                result.setData(voList);
            }
        } catch (Exception e) {
            log.error("管理员获取技术分类列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员获取技术分类列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void deleteAdminTechnologyType(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            TechnologyType technologyType = technologyTypeMapper.selectById(id);
            if (ObjectUtils.isEmpty(technologyType)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该分类不存在");
            } else if (technologyType.getTypeParentId().equals(-1)) {
                LambdaQueryWrapper<TechnologyType> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TechnologyType::getTypeParentId, technologyType.getId());
                List<TechnologyType> typeList = technologyTypeMapper.selectList(wrapper);
                if (!CollectionUtils.isEmpty(typeList)) {
                    result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",该分类不为空");
                } else {
                    int delete = technologyTypeMapper.deleteById(id);
                    if (delete != 1) {
                        result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                        result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                    }
                }
            } else {
                int delete = technologyTypeMapper.deleteById(id);
                if (delete != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("管理员删除技术分类失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【管理员删除技术分类接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, "", result);
        }
    }
}




