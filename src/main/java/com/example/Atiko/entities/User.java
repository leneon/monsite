package com.example.Atiko.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;
  
  @Column(name = "reset_token")
  private String resetToken;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private Profile profile;


  public String getResetToken() {
    return resetToken;
  }

  public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
  }

  @NotBlank
  @Size(max = 120)
  private String password;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;
  
  @Column(nullable = false)
  @ColumnDefault("true") // Valeur par défaut de true
  private Boolean status;

    public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

    // Getters and Setters for createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User() {
    this.status = true;
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.status = true;
 
  }
  public User(Long id) {
    this.id = id;
    this.status = true;

  }

  public User(LocalDateTime date, String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.createdAt = date;
    this.password = password;
    this.status = true;

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
 
  // Method to get the first role as a string
  public String getFirstRoleAsString() {
    return roles.stream()
                 .findFirst()  // Find the first role in the set
                 .map(role -> role.getName().name()) // Convert the ERole to string
                 .orElse("No Role Found");  // Fallback if no role is found
}

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", createdAt="
        + createdAt + ", roles=" + roles.toString() + "]";
  }

  public Profile getProfile() {
    return profile;
  }

public void setProfile(Profile profile) {
    this.profile = profile;
}
}