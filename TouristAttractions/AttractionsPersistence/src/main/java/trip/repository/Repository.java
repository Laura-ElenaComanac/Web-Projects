package trip.repository;

import model.EntityID;

import java.io.Serializable;

public interface Repository<T extends EntityID<Tid>, Tid extends Serializable> {
    void add(T elem);
    void delete(Integer id);
    void update (T elem, Tid id);
    T findById (Tid id);
    Iterable<T> findAll();
    int size();
}
