package com.github.peacetrue.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 角色视图类。
 *
 * @author peace
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO implements Serializable {
    private Long id;
    private String name;
    private List<String> tags;
}
