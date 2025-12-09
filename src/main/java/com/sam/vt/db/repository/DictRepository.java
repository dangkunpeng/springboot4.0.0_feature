package com.sam.vt.db.repository;

import com.sam.vt.db.entity.Dict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sam.vt.utils.SysDefaults.CACHE_NAME;

@Repository
public interface DictRepository extends JpaRepository<Dict, String> {

    @Cacheable(value = {CACHE_NAME, "Dict"}, key = "'mstDict:dictCode:' + #dictCode + ':valid' + #valid", unless = "#result==null")
    List<Dict> getAllByDictCodeAndValid(String dictCode, Integer valid);

}
