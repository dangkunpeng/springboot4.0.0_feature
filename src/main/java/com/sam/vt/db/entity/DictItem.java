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
@Table(name = "dict_item")
public class DictItem {

    @Id
    private String itemId;
    @Column
    private String dictId;
    @Column
    private String itemCode;
    @Column
    private String itemName;
    @Column
    private String itemDesc;
    @Column
    private Integer valid;
}
