package learning.spring.binarytea.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "authorities")
@IdClass(Authority.class)
@Getter
@Setter
public class Authority implements Serializable {
    @Id
    private String username;
    @Id
    private String authority;
}
