package learning.spring.binarytea.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
