package com.airlock.iam.scim.jpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Entity
@Table(name = "medusa_user")
public class MedusaUser {
    @Id
    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "surname")
    private String surname;

    @Column(name = "givenname")
    private String givenName;

//    @Column(name = "external_id")
//    private String externalId;

    @Column(name = "locked", nullable = false)
    private boolean locked;

    @Column(name = "pwd_hash")
    private String password;

    // FIXME needs a converter
//    @Column(name = "roles")
//    private Set<String> roles;

    @Column(name = "street")
    private String street;

    @Column(name = "streetnumber")
    private String streetnumber;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "town")
    private String town;

    @Column(name = "country")
    private String country;

    @Column(name = "email")
    private String email;

    @Column(name = "language")
    private String language;

    @Column(name = "cert_x509_data")
    private String certX509Data;

    @Column(name = "rowinsertdate", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "rowupdatedate", nullable = false)
    private Instant updatedAt = Instant.now();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Boolean getActive() {
        return !locked;
    }

    public void setActive(Optional<Boolean> active) {
        this.locked = !active.orElse(true);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetnumber() {
        return streetnumber;
    }

    public void setStreetnumber(String streetnumber) {
        this.streetnumber = streetnumber;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCertX509Data() {
        return certX509Data;
    }

    public void setCertX509Data(String certX509Data) {
        this.certX509Data = certX509Data;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
