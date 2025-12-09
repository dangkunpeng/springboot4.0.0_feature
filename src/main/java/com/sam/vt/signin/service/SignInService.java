package com.sam.vt.signin.service;

import com.sam.vt.db.entity.SignInfo;
import com.sam.vt.db.repository.SignInfoRepository;
import com.sam.vt.dict.DictApi;
import com.sam.vt.utils.JsonUtil;
import com.sam.vt.utils.RedisHelper;
import com.sam.vt.utils.SysDefaults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.sam.vt.utils.SysDefaults.fmtLocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignInService {
    private final DictApi dictApi;

    private final SignInfoRepository signInfoRepository;

    public ResponseEntity<SignInfo> sign(String userId, LocalDate signDate) {
        List<SignInfo> signInfoList = signInfoRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(signInfoList)) {
            SignInfo signInfo = new SignInfo();
            signInfo.setId(RedisHelper.newKey("signinfo"));
            signInfo.setUserId(userId);
            signInfo.setLastSignDate(fmtLocalDate(signDate));
            signInfo.setTotalSignDays(1L);
            signInfo.setContinuousDays(1);
            signInfo.setRewardPoints(getRewardByRules(1));
            signInfo.setTotalPoints(signInfo.getRewardPoints());
            this.signInfoRepository.save(signInfo);
            log.info("首次签到={}", JsonUtil.toJsonString(signInfo));
            return ResponseEntity.ok(signInfo);
        }
        SignInfo signInfo = signInfoList.get(0);
        LocalDate yesterday = signDate.minusDays(1);
        LocalDate lastSignDate = LocalDate.parse(signInfo.getLastSignDate(), DateTimeFormatter.ofPattern(SysDefaults.SYS_DEFAULT_DAY_PATTERN));
        int compare = lastSignDate.compareTo(signDate);
        if (compare > 0) {
            // TODO
            return ResponseEntity.ok(signInfo);
        } else if (compare == 0) {
            // 连续
            log.info("重复签到={}", JsonUtil.toJsonString(signInfo));
            return ResponseEntity.ok(signInfo);
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
            signInfo.setRewardPoints(getRewardByRules(signInfo.getContinuousDays()));
            signInfo.setTotalPoints(signInfo.getRewardPoints() + signInfo.getTotalPoints());
            this.signInfoRepository.save(signInfo);
            log.info("正常签到={}", JsonUtil.toJsonString(signInfo));
            return ResponseEntity.ok(signInfo);
        }
    }

    public Integer getRewardByRules(Integer continuousDays) {
        Map<String, String> rewardMap = this.dictApi.getDictMap("SIGN_REWARD");
        return Integer.parseInt(rewardMap.getOrDefault(String.valueOf(continuousDays), rewardMap.get("7")));
    }
}
