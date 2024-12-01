package com.imzz.webchat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author 邹智敏
 * @since 2018-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MineFriend对象", description="")
public class MineFriend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序列号")
    private String id;

    @ApiModelProperty(value = "好友id")
    private String mineId;

    @ApiModelProperty(value = "我的分组Id")
    private String mineGroupId;


}
