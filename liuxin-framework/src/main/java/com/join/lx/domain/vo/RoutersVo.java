package com.join.lx.domain.vo;

import com.join.lx.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutersVo {

    List<Menu>  menus;
}
