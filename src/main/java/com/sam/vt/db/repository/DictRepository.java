package com.sam.vt.db.repository;

import com.sam.vt.db.entity.Dict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sam.vt.utils.SysDefaults.CACHE_NAME;

@Repository
public interface DictRepository extends JpaRepository<Dict, String> {

    List<Dict> getAllByDictCodeAndValid(String dictCode, Integer valid);

}
