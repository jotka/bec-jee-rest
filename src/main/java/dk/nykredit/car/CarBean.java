/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.nykredit.car;

import dk.nykredit.infra.Crud;
import dk.nykredit.infra.CrudService;
import dk.nykredit.infra.exception.CustomException;
import dk.nykredit.infra.model.Filter;
import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewAccessScoped
public class CarBean implements Serializable {

    private LazyDataModel<Car> carList;
    private List<Car> filteredValue;// datatable filteredValue attribute
    private Integer id;
    private Car car;
    private Filter<Car> filter = new Filter<>(new Car());

    @Inject
    CarService carService; //car service holds business logic, if entity has no logic you can use Crud or CrudService (has transactins) directly


    /*
     * you can inject car directly, sometimes its useful but remember that you
     * don't have transactions there, use CrudService if you need transactions
     */
    @Inject
    Crud<Car> carCrud;

    @Inject
    CrudService<Car> carCrudService; // you can use generic service which has transactions, see remove car


    public LazyDataModel<Car> getCarList() {
        if (carList == null) {
            // usually in an utility or super class cause this code is always
            // the same
            carList = new LazyDataModel<Car>() {
                @Override
                public List<Car> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                                      Map<String, Object> filters) {
                    dk.nykredit.infra.model.SortOrder order = null;
                    if (sortOrder != null) {
                        order = sortOrder.equals(SortOrder.ASCENDING) ? dk.nykredit.infra.model.SortOrder.ASCENDING
                                : sortOrder.equals(SortOrder.DESCENDING) ? dk.nykredit.infra.model.SortOrder.DESCENDING
                                : dk.nykredit.infra.model.SortOrder.UNSORTED;
                    }
                    filter.setFirst(first).setPageSize(pageSize)
                            .setSortField(sortField).setSortOrder(order)
                            .setParams(filters);
                    List<Car> list = carService.paginate(filter);
                    setRowCount(carService.count(filter));
                    return list;
                }

                @Override
                public int getRowCount() {
                    return super.getRowCount();
                }

                @Override
                public Car getRowData(String key) {
                    return carService.findById(new Integer(key));
                }
            };

        }
        return carList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Car getCar() {
        if (car == null) {
            car = new Car();
        }
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void findCarById(Integer id) {
        if (id == null) {
            throw new CustomException("Provide Car ID to load");
        }
        car = carCrud.get(id);
        if (car == null) {
            throw new CustomException("Car not found with id " + id);
        }
        filter.setEntity(car);
    }

    public List<Car> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Car> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public void remove() {
        if (car != null && car.getId() != null) {
            carCrudService.remove(car);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Car " + car.getModel() + " removed successfully", null));
            clear();
        }
    }

    public void update() {
        String msg;
        if (car.getId() == null) {
            carService.insert(car);
            msg = "Car " + car.getModel() + " created successfully";
        } else {
            carService.update(car);
            msg = "Car " + car.getModel() + " updated successfully";
        }
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        clear();// reload car list
    }

    public void clear() {
        car = new Car();
        filter = new Filter<>(new Car());
        id = null;
    }

    public void onRowSelect(SelectEvent event) {
        setId(((Car) event.getObject()).getId());
        findCarById(getId());
    }

    public void onRowUnselect(UnselectEvent event) {
        car = new Car();
    }

    public Filter<Car> getFilter() {
        return filter;
    }

    public void setFilter(Filter<Car> filter) {
        this.filter = filter;
    }

    public List<String> completeModel(String query) {
        return carService.getModels(query);
    }

}
