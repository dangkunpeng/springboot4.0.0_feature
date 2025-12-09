package com.sam.vt.dict;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sam.vt.dict.bean.DictBean;
import com.sam.vt.dict.service.DictService;
import com.sam.vt.enums.EnumValid;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DictApi {

    @Resource
    DictService dictService;

    /**
     * 获取字典 MstData
     * 未删除的数据
     *
     * @param dictCode
     * @return
     */
    public List<DictBean> getMstDict(String dictCode) {
        return getMstDict(dictCode, EnumValid.YES.getCode());
    }

    private List<DictBean> getMstDict(String dictCode, Integer valid) {
        // 使用当前语言查询
        List<DictBean> list = this.dictService.getMstDict(dictCode, valid);
        if (CollectionUtils.isEmpty(list)) {
            // 查不到就查询全部
            list = this.dictService.getMstDict(dictCode, valid);
        }
        // 创建一个新对象,给外部使用,避免缓存里的字典被修改,影响其他代码使用
        return getNewList(list);
    }

    /**
     * 获取字典 MstData
     * 未删除的数据
     *
     * @param dictCode
     * @param itemName
     * @return
     */
    public String getMstDictValue(String dictCode, String itemName) {
        List<DictBean> list = getMstDict(dictCode);
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        for (DictBean dictBean : list) {
            if (StringUtils.equalsIgnoreCase(dictBean.getItemName(), itemName)) {
                return dictBean.getItemCode();
            }
        }
        return "";
    }


    public Map<String, String> getDictMap(String dictCode) {
        return toMap(this.getMstDict(dictCode));
    }


    /**
     * 字典转map
     * key:itemValue
     * value:itemName
     *
     * @param list
     * @return
     */
    public static Map<String, String> toMap(List<DictBean> list) {
        Map<String, String> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (DictBean item : list) {
            map.put(item.getItemCode(), item.getItemName());
        }
        return map;
    }

    /**
     * 字典转map KV反转
     * key:itemName
     * value:itemValue
     *
     * @param list
     * @return
     */
    public static Map<String, String> toMapReversed(List<DictBean> list) {
        Map<String, String> map = Maps.newHashMap();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (DictBean item : list) {
            map.put(item.getItemName(), item.getItemCode());
        }
        return map;
    }

    /**
     * 创建一个新对象,给外部使用,避免缓存里的字典被修改,影响其他代码使用
     *
     * @param list
     * @return
     */
    public static List<DictBean> getNewList(List<DictBean> list) {
        List<DictBean> extList = Lists.newArrayList();
        for (DictBean item : list) {
            extList.add(DictBean.builder()
                    .dictCode(item.getDictCode())
                    .itemCode(item.getItemCode()).itemName(item.getItemName())
                    .build());
        }
        return extList;
    }


    /**
     * 合并两个字典表
     *
     * @param list01
     * @param list02
     * @return
     */
    public static List<DictBean> unionDict(List<DictBean> list01, List<DictBean> list02) {
        List<DictBean> list = Lists.newArrayList();
        Map<String, String> map = toMap(list01);
        list.addAll(list01);
        for (DictBean bean : list02) {
            if (map.containsKey(bean.getItemCode())) {
                continue;
            }
            list.add(bean);
        }
        return list;
    }
}
