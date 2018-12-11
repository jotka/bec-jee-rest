package dk.nykredit.infra;

import dk.nykredit.car.Car;
import dk.nykredit.car.CarService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class InitAppBean {

    @Inject
    CarService carService;

    @PostConstruct
    public void init() {
        if (carService.crud().countAll() == 0) {
            for (int i = 1; i <= 10; i++) {
                Car c = new Car().name("name " + i).model("model " + i).price((double) (i * 100));
                carService.insert(c);
            }
        }
    }
}
