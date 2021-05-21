package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

//@MappedSuperclass
public class EntityID<ID extends Serializable> implements Serializable{
    private ID id;
    //@Id
    //@GeneratedValue(generator="increment")
    //@GenericGenerator(name="increment", strategy = "increment")
    public ID getId(){
        return id;
    }
    public void setId(ID id){
        this.id = id;
    }
}