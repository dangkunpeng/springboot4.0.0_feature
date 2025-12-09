package com.sam.vt.signin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sign_info")
public class SignInfo {
    @Id
    private String id;              // 主键ID
    @Column
    private String userId;           // 用户ID
    @Column
    private String lastSignDate;    // 上次签到日期，格式：yyyy-MM-dd
    @Column
    private Long totalSignDays;    // 总签到天数
    @Column
    private Integer continuousDays; // 连续签到天数
    @Column
    private Integer rewardPoints;     // 奖励积分
    @Column
    private Integer totalPoints;     // 奖励积分
}
