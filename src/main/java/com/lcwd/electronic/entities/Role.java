package com.lcwd.electronic.entities;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private String roleId;
    private String roleName;

    //If need bidirectional role wise user ke liye
//    @ManyToMany
//    @JoinColumn(name = "userId")
//    private User user;
}
