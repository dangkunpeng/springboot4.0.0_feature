package com.sam.vt.db.repository;

import com.sam.vt.db.entity.DictItem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sam.vt.utils.SysDefaults.CACHE_NAME;

@Repository
public interface DictItemRepository extends JpaRepository<DictItem, String> {
    @Cacheable(value = {CACHE_NAME, "Dict"}, key = "'mstDict:dictId:' + #dictId", unless = "#result==null")
    List<DictItem> findAllByDictId(String dictId);
}
