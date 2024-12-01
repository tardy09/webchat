package com.imzz.webchat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Group对象", description="")
public class MineGrouping implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "群id")
    private String id;

    @ApiModelProperty(value = "群组名")
    private String groupname;

    @ApiModelProperty(value = "群组头像")
    private String avatar;

    @ApiModelProperty(value = "群下面的成员")
    List<Mine> mines;

}
