package com.imzz.webchat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@ApiModel(value="MineGroup对象", description="")
public class MineGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "我的分组id")
    private String id;

    @ApiModelProperty(value = "我的分组名称")
    private String groupname;

    @ApiModelProperty(value = "我的id")
    private String mineId;

    @ApiModelProperty(value = "我的好友")
    private List<Mine> list;

}
