package com.join.lx.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
    private Long id;

    //标签名
    private String name;

    //备注
    private String remark;
}
