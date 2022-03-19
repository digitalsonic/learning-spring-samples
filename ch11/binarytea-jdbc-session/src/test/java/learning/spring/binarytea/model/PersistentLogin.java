package learning.spring.binarytea.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "persistent_logins")
@Getter
@Setter
public class PersistentLogin {
    @Id
    private String series;
    private String username;
    private String token;
    private Date lastUsed;
}
