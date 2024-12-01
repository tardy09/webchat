package com.imzz.webchat.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Systemrole implements Serializable {
    private String systemroleId;

    private String systemroleName;

    private String systemroleStatus;

    private String systemroleCreateDate;

    private String systemroleDesc;
}