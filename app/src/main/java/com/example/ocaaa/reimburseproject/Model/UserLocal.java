package com.example.ocaaa.reimburseproject.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Asus on 7/23/2017.
 */

public class UserLocal extends RealmObject {
    @PrimaryKey
    private String email;
    private String token;

}
