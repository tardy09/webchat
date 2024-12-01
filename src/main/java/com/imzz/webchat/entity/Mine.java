package com.imzz.webchat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
@ToString
@ApiModel(value="Mine对象", description="")
public class Mine implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "我的ID")
    private String id;

    @ApiModelProperty(value = "我的昵称")
    private String username;

    @ApiModelProperty(value = "我的账号")
    private String usernumber;

    @ApiModelProperty(value = "我的密码")
    private String userpassword;

    @ApiModelProperty(value = "在线状态 online：在线、hide：隐身")
    private String status;

    @ApiModelProperty(value = "我的签名")
    private String sign;

    @ApiModelProperty(value = "我的头像")
    private String avatar;

    @ApiModelProperty(value = "聊天类型")
    private String type;

    @ApiModelProperty(value = "聊天内容")
    private String content;

    @ApiModelProperty(value = "活动次数")
    private Integer action;

    @ApiModelProperty(value = "扩展字段")
    private String extand;

    @ApiModelProperty(value = "发送者ID")
    private String fromid;

    @ApiModelProperty(value = "发送消息的时间")
    private Long timestamp;

    @ApiModelProperty(value = "验证码")
    private String vercode;

    public Mine(){

    }

    public Mine(String id){
        this.id = id;
    }
}
