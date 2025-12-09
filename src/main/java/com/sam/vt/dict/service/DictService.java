package com.sam.vt.dict.service;

import com.google.common.collect.Lists;
import com.sam.vt.db.entity.Dict;
import com.sam.vt.db.entity.DictItem;
import com.sam.vt.db.repository.DictItemRepository;
import com.sam.vt.db.repository.DictRepository;
import com.sam.vt.dict.bean.DictBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sam.vt.utils.SysDefaults.CACHE_NAME;

/**
 * 描述： 字典表管理service
 * 本服务主要是为了提供缓存功能，不建议直接使用，建议使用DictApi
 *
 * @author kunpeng.dang
 * @version 1.0
 * @since 20211201
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DictService {

    private final DictRepository dictRepository;
    private final DictItemRepository dictItemRepository;

    /**
     * 查询MstData里的字典,使用缓存加快速度
     *
     * @param dictCode
     * @return
     */
    @Cacheable(value = {CACHE_NAME, "Dict"}, key = "'mstDict-dictCode' + #dictCode", unless = "#result==null")
    public List<DictBean> getMstDict(String dictCode, Integer valid) {
        List<DictBean> result = Lists.newArrayList();
        try {
            // 查询：字典大类
            List<Dict> dictList = this.dictRepository.getAllByDictCodeAndValid(dictCode, valid);
            Dict dict = dictList.get(0);
            // 查询：字典明细
            List<DictItem> dictItems = this.dictItemRepository.findAllByDictId(dict.getDictId());
            for (DictItem item : dictItems) {
                result.add(DictBean.builder()
                        .dictCode(dict.getDictCode())
                        .itemCode(item.getItemCode()).itemName(item.getItemName())
                        .build());
            }
        } catch (Exception e) {
            log.info("ERROR ={}", e.getMessage());
        }
        log.debug("getMstDict {} records for dictCode ({}) ", result.size(), dictCode);
        return result;
    }
}
