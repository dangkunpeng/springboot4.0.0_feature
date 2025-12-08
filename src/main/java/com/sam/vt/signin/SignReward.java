package com.sam.vt.signin;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sign_reward")
public class SignReward {
    private Integer points;        // 积分
    private Integer extraPoints;   // 额外积分
    private String message;        // 奖励信息
}