package coffee_serv.Resource;

import coffee_serv.Entity.Coffee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import coffee_serv.Repository.CoffeeRepository;

import java.util.List;

@RestController
@RequestMapping("/api/coffee")
public class CoffeeResource {

    @Autowired
    CoffeeRepository coffeeRepository;

    @PostMapping("/create")
    public void create(@RequestBody Coffee coffee){
        coffeeRepository.save(coffee);
    }

    @GetMapping("/all")
    public List<Coffee> getAll(){
        return coffeeRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void del(@PathVariable Long id){
        coffeeRepository.deleteById(id);
    }

    @GetMapping("/{id}")
    public Coffee get(@PathVariable Long id){
        return coffeeRepository.findById(id).get();
    }

    @PutMapping("/update/{id}")
    public void update(@RequestBody Coffee coffee, @PathVariable Long id){
        coffee.setId(id);
        coffeeRepository.save(coffee);
    }
}
