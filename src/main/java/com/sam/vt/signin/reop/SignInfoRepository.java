package com.sam.vt.signin.reop;

import com.sam.vt.signin.entity.SignInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignInfoRepository extends JpaRepository<SignInfo, String> {

    List<SignInfo> getByUserId(String userId);
}
