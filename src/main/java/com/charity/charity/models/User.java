package com.charity.charity.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
//inherit the UserDetails class and implement all its methods
//this is necessary for linking the user and item tables so that the author of the item is saved in the item table when creating a record
// in all implemented methods, you need to replace afls with true
//and also in the method public Collection<? extends GrantedAuthority> getAuthorities() - configure return user role instead of NULL

    //enabled field - required (required by the system) - we will write  "0" for block user
    //role - created an enum (separate file)

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotEmpty(message = "{form_errors.all_fields.not_empty}")
    @Size(min = 2, max = 20, message = "{form_errors.reg.size_string}")
    //username = email
    private String firstname;

    @NotEmpty(message = "{form_errors.all_fields.not_empty}")
    @Size(min = 2, max = 20, message = "{form_errors.reg.size_string}")
    //username = email
    private String surname;

    @NotEmpty(message = "{form_errors.all_fields.not_empty}")
    @Size(min = 2, max = 20, message = "{form_errors.reg.size_string}")
    //username = email
    private String phone;

    @NotEmpty(message = "{form_errors.all_fields.not_empty}")
    @Size(min = 6, max = 100, message = "{form_errors.reg.size_string}")
    //username = email
    private String password;

    @NotEmpty(message = "{form_errors.all_fields.not_empty}")
    @Email(message = "{form_errors.reg.error_email}")
    //username = email
    private String username;

    private boolean enabled;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    public User() {
    }

    public User(String firstname, String surname, String username, String password, String phone, boolean enabled, Set<Role> role) {
        this.firstname = firstname;
        this.surname = surname;
        this.username = username; // email
        this.password = password;
        this.phone = phone;
        this.enabled = enabled;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRole(); // return role
        //This method made in this class
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getId() {
        return id;
    }
}
