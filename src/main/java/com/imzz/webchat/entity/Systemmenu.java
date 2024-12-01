package com.imzz.webchat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Systemmenu implements Serializable {

    private String systemmenuId;

    private String systemmenuName;

    private String systemmenuPermission;

    private String systemmenuAddress;

    private String systemmenuStatus;

    private String systemmenuParentId;
}