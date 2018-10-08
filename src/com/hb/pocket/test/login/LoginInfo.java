package com.hb.pocket.test.login;

import com.hb.pocket.test.base.BaseEntity;

/**
 * The login entity.
 * Created by hb on 04/07/2018.
 */
public class LoginInfo extends BaseEntity {

    private boolean isSuccess;

    public LoginInfo() {
        super();
        this.isSuccess = false;
    }

    public LoginInfo(boolean success) {
        super();
        this.isSuccess = success;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    public boolean getSuccess() {
        return isSuccess;
    }

}
