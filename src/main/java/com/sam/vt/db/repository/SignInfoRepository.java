package com.sam.vt.db.repository;

import com.sam.vt.db.entity.SignInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignInfoRepository extends JpaRepository<SignInfo, String> {

    List<SignInfo> getByUserId(String userId);
}
