package dk.nykredit.infra;

import dk.nykredit.infra.exception.CustomException;
import dk.nykredit.infra.model.BaseEntity;
import dk.nykredit.infra.model.Filter;
import dk.nykredit.infra.model.SortOrder;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;


/**
 * Created by rmpestano on 9/7/14. A CRUD template to all services
 */
@Dependent
public abstract class CrudService<T extends BaseEntity> implements Serializable {

    @Inject
    private Crud<T> crud;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Crud<T> crud() {
        return crud;
    }


    public void insert(T entity) {
        if (entity == null) {
            throw new CustomException("Entity cannot be null");
        }

        if (entity.getId() != null) {
            throw new CustomException("Entity must be transient");
        }
        beforeInsert(entity);
        crud().save(entity);
        afterInsert(entity);
    }

    public void remove(T entity) {
        if (entity == null) {
            throw new CustomException("Entity cannot be null");
        }

        if (entity.getId() == null) {
            throw new CustomException("Entity cannot be transient");
        }
        beforeRemove(entity);
        crud().delete(entity);
        afterRemove(entity);
    }


    public void remove(List<T> entities) {
        if (entities == null) {
            throw new CustomException("Entities cannot be null");
        }
        for (T t : entities) {
            this.remove(t);
        }
    }

    public void update(T entity) {
        if (entity == null) {
            throw new CustomException("Entity cannot be null");
        }

        if (entity.getId() == null) {
            throw new CustomException("Entity cannot be transient");
        }
        beforeUpdate(entity);
        crud().update(entity);
        afterUpdate(entity);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listAll() {
        return crud().listAll();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T findById(Serializable id) {
        return crud().get(id);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T findByExample(T example) {
        return crud().example(example).find();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public T findByExample(T example, MatchMode mode) {
        return crud().example(example, mode).find();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> listByExample(T example, MatchMode mode) {
        return crud().example(example, mode).list();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<T> paginate(Filter<T> filter) {
        crud().initCriteria();
        Criteria criteria = crud().criteria(configPagination(filter))
                .getCriteria();
        String sortField = filter.getSortField();
        if (sortField != null) {
            if (filter.getSortOrder().equals(SortOrder.UNSORTED)) {
                filter.setSortOrder(SortOrder.ASCENDING);
            }
            if (filter.getSortOrder().equals(SortOrder.ASCENDING)) {
                criteria.addOrder(Order.asc(sortField));
            } else {
                criteria.addOrder(Order.desc(sortField));
            }
        }
        return crud().criteria(criteria).firstResult(filter.getFirst())
                .maxResult(filter.getPageSize()).list();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public int count(Filter<T> filter) {
        return crud().criteria(configPagination(filter)).count();
    }

    /**
     * usually overriden in concrete service
     *
     * @param filter the filter
     * @return modified criteria
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Criteria configPagination(Filter<T> filter) {
        if (filter.getEntity() != null) {
            return crud().criteria().example(filter.getEntity()).getCriteria();
        } else {
            return crud().getCriteria();
        }

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void beforeInsert(T entity) {

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void afterInsert(T entity) {
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void beforeUpdate(T entity) {

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void afterUpdate(T entity) {
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void beforeRemove(T entity) {

    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void afterRemove(T entity) {
    }

}
