package com.restapi.model;

import com.restapi.security.service.PasswordHandler;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Document(collection = "users")
public class User implements UserDetails {

    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String username;
    private String password;
    private int accType; // 1:Child; 2:Parent;
    private boolean enabled;
    private String email;
    private int birthday;
    private String gender;
    private String token;
    private ArrayList<String> relatives = new ArrayList<>();
    private String familyId;
    @Id
    private ObjectId id;

    private String fcmToken;

    private List<Role> authorities;

    //private boolean accountNonExpired;

    public User(String firstName, String lastName, String username, String email, String password, int birthday, String gender, boolean enabled, int accType){
        this.firstName = firstName;
        this.lastName = firstName;
        this.username = username;
        this.password = password;
        this.accType = accType;
        this.enabled = enabled;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Override
    public List<Role> getAuthorities() {
        return this.authorities;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

//    @Override
//    public boolean isEnabled() {
//        return false;
//    }

    public void setUsername(String username) {
        this.username = username;
    }

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }

    public void setAuthorities(List<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void encodePassword() {
        setPassword(PasswordHandler.securePassword(this.password));
    }

    public int getAccType() {
        return accType;
    }

    public void setAccType(int accType) {
        this.accType = accType;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getParentUsernames() {
        return relatives;
    }

    public void setConnections(ArrayList<String> parents) {
        this.relatives = parents;
    }

    public void addConnectedUser(String username) {
        this.relatives.add(username);
    }

    public void removeConnectedUser(String username) {
        this.relatives.remove(username);
    }

    public String generateFamilyId() {
        this.familyId = UUID.randomUUID().toString();
        return this.familyId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

//    public void setAccountNonExpired(boolean accountNonExpired) {
//        this.accountNonExpired = true;
//    }
}