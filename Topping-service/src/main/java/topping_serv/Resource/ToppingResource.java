package topping_serv.Resource;

import org.springframework.web.bind.annotation.*;
import topping_serv.Entity.Topping;
import topping_serv.Repository.ToppingRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/topping")
public class ToppingResource {

    @Autowired
    ToppingRepository toppingRepository;

    @PostMapping("/create")
    public void create(@RequestBody Topping topping){
        toppingRepository.save(topping);
    }

    @GetMapping("/all")
    public List<Topping> getAll(){
        return toppingRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void del(@PathVariable Long id){
        toppingRepository.deleteById(id);
    }

    @PutMapping("/update/{id}")
    public void update(@RequestBody Topping topping, @PathVariable Long id){
        topping.setId(id);
        toppingRepository.save(topping);
    }

    @GetMapping("/{id}")
    public Topping get(@PathVariable Long id){
        return  toppingRepository.findById(id).get();
    }
}
