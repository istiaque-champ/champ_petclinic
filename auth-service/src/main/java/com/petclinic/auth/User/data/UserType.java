package com.petclinic.auth.User.data;

public enum UserType {
    ADMIN(1),
    CUSTOMER(2),
    VET(3);

    public final int value;

    private UserType(int value){
        this.value = value;
    }
}
