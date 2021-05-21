package model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table( name = "agencyUsers" )
public class AgencyUser implements Identifiable<Integer>{
    @Id
    private int ID;
    private String userName;
    private String password;

    public AgencyUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public AgencyUser() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgencyUser that = (AgencyUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public String toString() {
        return "AgencyUser{" +
                "name='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public Integer getId() {
        return this.ID;
    }

    @Override
    public void setId(Integer id) {
        this.ID = id;
    }
}
