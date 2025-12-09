package com.sam.vt.signin;

import com.sam.vt.signin.beans.SignResult;
import com.sam.vt.signin.entity.SignInfo;
import com.sam.vt.signin.reop.SignInfoRepository;
import com.sam.vt.utils.Constants;
import com.sam.vt.utils.FmtUtils;
import com.sam.vt.utils.JsonUtil;
import com.sam.vt.utils.RedisHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.sam.vt.utils.Constants.fmtLocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {
    private final SignInfoRepository signInfoRepository;

    public ResponseEntity<String> sign(String userId, LocalDate signDate) {
        List<SignInfo> signInfoList = signInfoRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(signInfoList)) {
            SignInfo signInfo = new SignInfo();
            signInfo.setId(RedisHelper.newKey("signinfo"));
            signInfo.setUserId(userId);
            signInfo.setLastSignDate(fmtLocalDate(signDate));
            signInfo.setTotalSignDays(1L);
            signInfo.setContinuousDays(1);
            this.signInfoRepository.save(signInfo);
            log.info("首次签到={}", JsonUtil.toJsonString(signInfo));
            return ResponseEntity.ok("success");
        }
        SignInfo signInfo = signInfoList.get(0);
        LocalDate yesterday = signDate.minusDays(1);
        LocalDate lastSignDate = LocalDate.parse(signInfo.getLastSignDate(), DateTimeFormatter.ofPattern(Constants.SYS_DEFAULT_DAY_PATTERN));
        int compare = lastSignDate.compareTo(signDate);
        if (compare > 0) {
            // TODO
            return ResponseEntity.ok("补签");
        } else if (compare == 0) {
            // 连续
            log.info("重复签到={}", JsonUtil.toJsonString(signInfo));
            return ResponseEntity.ok("重复签到");
        } else {
            // 非连续签到
            signInfo.setLastSignDate(fmtLocalDate(signDate));
            signInfo.setTotalSignDays(signInfo.getTotalSignDays() + 1);
            if (yesterday.compareTo(lastSignDate) == 0) {
                // 连续签到
                signInfo.setContinuousDays(signInfo.getContinuousDays() + 1);
            } else {
                // 非连续签到
                signInfo.setContinuousDays(1);

            }
            this.signInfoRepository.save(signInfo);
            log.info("正常签到={}", JsonUtil.toJsonString(signInfo));
            return ResponseEntity.ok("重复签到");
        }
    }
}
