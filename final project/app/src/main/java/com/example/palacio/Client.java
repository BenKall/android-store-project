package com.example.palacio;

public class Client {

    private static User myuser;

    private static String uid="";

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        Client.uid = uid;
    }

    public static User getMyuser()
    {
        return myuser;
    }

    public static void setMyuser(User myuser)
    {
        Client.myuser = myuser;
    }

}
