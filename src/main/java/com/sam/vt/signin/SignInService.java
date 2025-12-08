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

    public SignResult sign(String userId, LocalDate signDate) {
        SignResult signResult = new SignResult();
        List<SignInfo> signInfoList = signInfoRepository.getByUserId(userId);
        if (CollectionUtils.isEmpty(signInfoList)) {

            SignInfo signInfo = new SignInfo();
            signInfo.setId(RedisHelper.newKey("signinfo"));
            signInfo.setUserId(userId);
            signInfo.setLastSignDate(fmtLocalDate(signDate));
            signInfo.setSignYear(signDate.getYear());
            signInfo.setSignDays("1");
            signInfo.setTotalSignDays(1L);
            signInfo.setContinuousDays(1);
            signInfo.setMonthSignDays(1L);
            this.signInfoRepository.save(signInfo);
            log.info("首次签到={}", JsonUtil.toJsonString(signInfo));
            signResult.setSignDate(signDate);
            signResult.setContinuousDays(1);
            signResult.setSuccess(true);
            signResult.setMessage("success");
            return signResult;
        }
        SignInfo signInfo = signInfoList.get(0);
        LocalDate today = LocalDate.now();
        if (today.compareTo(signDate) == 0) {
            // 重复
            log.info("重复签到={}", JsonUtil.toJsonString(signInfo));
            return SignResult.fail("重复签到");
        }
        LocalDate yesterday = signDate.minusDays(1);
        LocalDate lastSignDate = LocalDate.parse(signInfo.getLastSignDate(), DateTimeFormatter.ofPattern(Constants.SYS_DEFAULT_DAY_PATTERN));
        if (lastSignDate.compareTo(yesterday) == 0) {
            // 连续签到
            signInfo.setLastSignDate(fmtLocalDate(signDate));
            signInfo.setSignYear(signDate.getYear());

            signInfo.setTotalSignDays(signInfo.getTotalSignDays() + 1);
            signInfo.setContinuousDays(signInfo.getContinuousDays() + 1);
            signInfo.setSignDays(signInfo.getSignDays() + ",1");
            if (signDate.getDayOfMonth() == 01) {
                signInfo.setMonthSignDays(1L);
            } else {
                signInfo.setMonthSignDays(signInfo.getMonthSignDays() + 1);
            }
            this.signInfoRepository.save(signInfo);
            signResult.setSignDate(signDate);
            signResult.setContinuousDays(signInfo.getContinuousDays());
            signResult.setSuccess(true);
            signResult.setMessage(FmtUtils.fmtMsg("连续签到{}天", signInfo.getContinuousDays()));
            log.info("连续签到={}", JsonUtil.toJsonString(signInfo));
            return signResult;
        }
        // 非连续签到
        signInfo.setLastSignDate(fmtLocalDate(signDate));
        signInfo.setSignYear(signDate.getYear());
        signInfo.setTotalSignDays(signInfo.getTotalSignDays() + 1);
        signInfo.setContinuousDays(1);
        if (signDate.getDayOfMonth() == 01) {
            signInfo.setMonthSignDays(1L);
        } else {
            signInfo.setMonthSignDays(signInfo.getMonthSignDays() + 1);
        }
        // 补齐中间缺失的签到天数
        StringBuilder signDaysBuilder = new StringBuilder();
        signDaysBuilder.append(signInfo.getSignDays());
        while (StringUtils.compare(fmtLocalDate(signDate),signInfo.getLastSignDate()) > 0) {
            signDaysBuilder.append(",0");
            signDate = signDate.minusDays(1);
        }
        signDaysBuilder.append(",1");
        signInfo.setSignDays(signDaysBuilder.toString());
        signInfo.setSignDays(signInfo.getSignDays() + "," + "1");
        this.signInfoRepository.save(signInfo);
        signResult.setSignDate(signDate);
        signResult.setContinuousDays(signInfo.getContinuousDays());
        signResult.setSuccess(true);
        signResult.setMessage("再次签到");
        log.info("再次签到={}", JsonUtil.toJsonString(signInfo));
        return signResult;
    }

}
