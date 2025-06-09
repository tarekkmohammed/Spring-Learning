package model.user;


import lombok.Data;
import model.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "`user`")
@Data
public class User extends BaseEntity<Integer> {
    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;
}
