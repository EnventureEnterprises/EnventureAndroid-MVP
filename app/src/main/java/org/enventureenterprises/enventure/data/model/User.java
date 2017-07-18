package org.enventureenterprises.enventure.data.model;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {
    private long karma;

    private  long id;
    private String first_name;
    private String last_name;
    private String username;
    @PrimaryKey
    private String mobile;
    private String email;
    private String objectId;
    private String sessionToken;
    private String gravatarId;
    private String avatar;

    private String bio;

    private  Boolean me;

    public Long submitted;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(final String mobile) {
        this.mobile =mobile;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(final String bio) {
        this.bio = bio;
    }

    public String getObjectId() {
        return this.objectId;
    }

    public void setObjectId(final String objectId) {
        this.objectId = objectId;
    }

    public String getSessionToken() {
        return this.sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getFirstName() {
        return this.first_name;
    }

    public void setFirstName(final String firstName) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return this.last_name;
    }

    public void setLastName(final String lastName) {
        this.last_name = last_name;
    }

    public String getGravatarId() {
        return this.gravatarId;
    }

    public String getAvatarUrl() {
        return this.avatar;
    }
    public void setAvatarUrl(String avatar) {
        this.avatar=avatar;
    }


    public Boolean getMe() {
        return me;
    }

    public void setMe(Boolean me) {
        this.me = me;
    }


    /* public String getAvatarUrl() {
        if (TextUtils.isEmpty(avatarUrl)) {
            String gravatarId = getGravatarId();
            if (TextUtils.isEmpty(gravatarId))
                gravatarId = GravatarUtils.getHash(getUsername());
            avatarUrl = getAvatarUrl(gravatarId);
        }
        return avatarUrl;
    }

    private String getAvatarUrl(String id) {
        if (!TextUtils.isEmpty(id))
            return "https://secure.gravatar.com/avatar/" + id + "?d=404";
        else
            return null;
    }*/

}