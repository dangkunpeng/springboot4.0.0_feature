package com.sam.vt.db.entity;

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
@Table(name = "dict")
public class Dict {
    @Id
    private String dictId;
    @Column
    private String dictCode;
    @Column
    private String dictName;
    @Column
    private String dictDesc;
    @Column
    private Integer valid;

}
