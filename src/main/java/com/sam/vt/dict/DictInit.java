package com.sam.vt.dict;

import com.google.common.collect.Lists;
import com.sam.vt.db.entity.Dict;
import com.sam.vt.db.entity.DictItem;
import com.sam.vt.db.repository.DictItemRepository;
import com.sam.vt.db.repository.DictRepository;
import com.sam.vt.enums.EnumDict;
import com.sam.vt.enums.EnumValid;
import com.sam.vt.utils.FmtUtils;
import com.sam.vt.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DictInit implements ApplicationRunner {
    private static final String ITEM_DESC = "连续登录{}天，奖励{}积分";
    private final DictRepository dictRepository;
    private final DictItemRepository dictItemRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Dict> dictList = dictRepository.getAllByDictCodeAndValid(EnumDict.SIGN_REWARD.name(), EnumValid.YES.getCode());
        // 如果已经初始化过，就不再初始化
        if (!CollectionUtils.isEmpty(dictList)) {
            log.info("字典数据已初始化，无需重复初始化");
            return;
        }
        Dict dict = Dict.builder()
                .dictId(RedisHelper.newKey("dict"))
                .dictCode(EnumDict.SIGN_REWARD.name())
                .dictName("签到奖励")
                .dictDesc("签到奖励配置")
                .valid(EnumValid.YES.getCode())
                .build();
        List<DictItem> dictItemList = Lists.newArrayList();
        for (int i = 1; i <= 7; i++) {
            dictItemList.add(DictItem.builder()
                    .itemId(RedisHelper.newKey("dictItem"))
                    .dictId(dict.getDictId())
                    .itemCode(String.valueOf(i))
                    .itemName(String.valueOf(i * 10))
                    .itemDesc(FmtUtils.fmtMsg(ITEM_DESC, i, i * 10))
                    .valid(EnumValid.YES.getCode())
                    .build());
        }
        log.info("初始化字典数据: {}, {}", dict, dictItemList);
        this.dictRepository.save(dict);
        this.dictItemRepository.saveAll(dictItemList);
    }
}
