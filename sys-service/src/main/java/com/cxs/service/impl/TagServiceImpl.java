package com.cxs.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxs.base.BaseResult;
import com.cxs.config.CommonConfig;
import com.cxs.constant.CachePrefixContent;
import com.cxs.dto.admin.tag.SaveOrUpdateTagDTO;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.model.Tag;
import com.cxs.model.User;
import com.cxs.service.TagService;
import com.cxs.mapper.TagMapper;
import com.cxs.utils.RedisUtil;
import com.cxs.vo.main.ExternalLinkVO;
import com.cxs.vo.tag.TagVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private CommonConfig commonConfig;

    @Override
    public void getTagList(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<TagVO> voList = new ArrayList<>();
            Object value = redisUtil.getValue(CachePrefixContent.INDEX_TAG_LIST);
            if (!ObjectUtils.isEmpty(value)) {
                voList = JSON.parseArray(JSON.toJSONString(value), TagVO.class);
            } else {
                QueryWrapper<Tag> wrapper = new QueryWrapper<>();
                wrapper.orderByAsc("create_time");
                List<Tag> tags = tagMapper.selectList(wrapper);
                voList = CollectionUtils.isEmpty(tags) ? new ArrayList<>(0) :
                        tags.stream().map(t -> {
                            TagVO vo = new TagVO();
                            BeanUtils.copyProperties(t, vo);
                            return vo;
                        }).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(voList)) {
                    redisUtil.set(CachePrefixContent.INDEX_TAG_LIST, voList, commonConfig.getCacheShortTime(), TimeUnit.MINUTES);
                }
            }
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取标签列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取标签列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void getRealTimeTagList(BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            List<TagVO> voList = new ArrayList<>();
            LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(Tag::getCreateTime);
            List<Tag> tags = tagMapper.selectList(wrapper);
            voList = CollectionUtils.isEmpty(tags) ? new ArrayList<>(0) :
                    tags.stream().map(t -> {
                        TagVO vo = new TagVO();
                        BeanUtils.copyProperties(t, vo);
                        return vo;
                    }).collect(Collectors.toList());
            result.setData(voList);
        } catch (Exception e) {
            log.error("获取标签列表失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【获取标签列表接口】【{}ms】 \n入参:{}\n出参:{}", "查询", endTime - startTime, "", result);
        }
    }

    @Override
    public void saveTag(SaveOrUpdateTagDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Tag tag = new Tag();
            BeanUtils.copyProperties(dto, tag);
            tag.setCreateTime(LocalDateTime.now());
            int insert = 0;
            try {
                insert = tagMapper.insert(tag);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",标签名称已存在");
                return;
            }
            if (insert != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",新增标签失败");
            }
        } catch (Exception e) {
            log.error("新增标签失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增或修改标签接口】【{}ms】 \n入参:{}\n出参:{}", "新增", endTime - startTime, dto, result);
        }
    }

    @Override
    public void updateTag(SaveOrUpdateTagDTO dto, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Tag tag = new Tag();
            BeanUtils.copyProperties(dto, tag);
            int update = 0;
            try {
                update = tagMapper.updateById(tag);
            } catch (DuplicateKeyException e) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",标签名称已存在");
                return;
            }
            if (update != 1) {
                result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",修改标签失败");
            }
        } catch (Exception e) {
            log.error("修改标签失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【新增或修改标签接口】【{}ms】 \n入参:{}\n出参:{}", "修改", endTime - startTime, dto, result);
        }
    }

    @Override
    public void deleteTag(Integer id, HttpServletRequest request, BaseResult result) {
        long startTime = System.currentTimeMillis();
        try {
            Tag tag = tagMapper.selectById(id);
            if (ObjectUtils.isEmpty(tag)) {
                result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
                result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg() + ",标签不存在");
            } else {
                int delete = tagMapper.deleteById(id);
                if (delete != 1) {
                    result.setCode(CurrencyErrorEnum.DATABASE_ERROR.getCode());
                    result.setMsg(CurrencyErrorEnum.DATABASE_ERROR.getMsg() + ",删除标签失败");
                }
            }
        } catch (Exception e) {
            log.error("删除标签失败,{}", e);
            result.setCode(CurrencyErrorEnum.OPERA_ERROR.getCode());
            result.setMsg(CurrencyErrorEnum.OPERA_ERROR.getMsg());
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("【{}】【删除标签接口】【{}ms】 \n入参:{}\n出参:{}", "删除", endTime - startTime, id, result);
        }
    }
}




