package com.lv.reg.entities;

import com.lv.reg.formBean.UserForm;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String username;
    @NotEmpty
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Date dateCreated;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")})
    private Set<Authority> authorities = new HashSet<>();

    public User(UserForm userForm, Authority authority) {
        this.username = userForm.getUserName();
        this.firstName = userForm.getFirstName();
        this.lastName = userForm.getLastName();
        this.phone = userForm.getPhoneNumber();
        this.dateCreated = Date.valueOf(LocalDate.now());
        this.authorities.add(authority);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encoded = bCryptPasswordEncoder.encode(userForm.getPassword());
        this.password = encoded;
    }


}