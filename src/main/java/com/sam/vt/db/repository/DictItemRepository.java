package com.sam.vt.db.repository;

import com.sam.vt.db.entity.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictItemRepository extends JpaRepository<DictItem, String> {
    List<DictItem> findAllByDictId(String dictId);
}
