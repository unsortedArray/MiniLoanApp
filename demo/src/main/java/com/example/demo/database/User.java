package com.example.demo.database;


import com.example.demo.enums.UserRoles;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity class representing a user.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table
public class User implements UserDetails {
    @Id
    @Column(name = "username")
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoles role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    /**
     * Get the authorities granted to the user.
     *
     * @return Collection of authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles as authorities
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
