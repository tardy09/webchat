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
@ApiModel(value="GroupFriend对象", description="")
public class MineGroupingFriend implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "序列号")
    private String id;

    @ApiModelProperty(value = "群id")
    private String groupId;

    @ApiModelProperty(value = "我的id")
    private String mineId;

}
