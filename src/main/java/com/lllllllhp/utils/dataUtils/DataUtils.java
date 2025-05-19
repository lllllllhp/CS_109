package com.lllllllhp.utils.dataUtils;

import com.lllllllhp.data.UserData;

public class DataUtils {
    public static boolean isMember;
    public static UserData userData;

    public static void setUserData(UserData userData) {
        DataUtils.userData = userData;
        isMember = true;
    }

    public static void logOut() {
        userData = null;
        isMember = false;
    }
}
