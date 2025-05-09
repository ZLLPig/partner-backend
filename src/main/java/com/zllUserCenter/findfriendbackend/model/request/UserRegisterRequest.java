package com.zllUserCenter.findfriendbackend.model.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -3318725794895727352L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
