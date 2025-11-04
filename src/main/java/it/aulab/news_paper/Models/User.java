package it.aulab.news_paper.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "users")

public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private String username;

    @Column (nullable = false)
    private String email;

    @Column (nullable = false)
    private String password;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable (
        name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles = new ArrayList<>(); 
}


