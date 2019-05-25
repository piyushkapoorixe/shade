package com.piyush.shade;

public class FindFriends
{
    public String profileimage, fullname, status, userid, friendID;

    public FindFriends()
    {

    }

    public FindFriends(String profileimage, String fullname, String status, String userid)
    {
        this.profileimage = profileimage;
        this.fullname = fullname;
        this.status = status;
        this.userid = userid;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setFriendID() {
        friendID = "J5dP30W7nJVuTKFSIdTovHNMDqb2";
    }
}
