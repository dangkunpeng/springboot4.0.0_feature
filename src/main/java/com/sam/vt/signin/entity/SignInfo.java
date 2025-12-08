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
    private Integer signYear;         // 签到年份
    @Column
    private String signDays;       // 当月签到天数，逗号分隔的字符串，例如："1,0,1,1,1"
    @Column
    private Long totalSignDays;    // 总签到天数
    @Column
    private Integer continuousDays; // 连续签到天数
    @Column
    private Long monthSignDays;    // 当月签到天数
}
