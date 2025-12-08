package com.sam.vt.signin.beans;

import com.sam.vt.signin.SignReward;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignResult {
    private boolean success;
    private String message;
    private Integer continuousDays;
    private SignReward reward;
    private LocalDate signDate;
    
    public static SignResult success(int continuousDays, SignReward reward, LocalDate signDate) {
        return new SignResult(true, "签到成功", continuousDays, reward, signDate);
    }
    
    public static SignResult fail(String message) {
        return new SignResult(false, message, null, null, null);
    }
}


