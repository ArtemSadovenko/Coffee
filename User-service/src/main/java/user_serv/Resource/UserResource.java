package user_serv.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import user_serv.Entity.Coffee;
import user_serv.Entity.Topping;
import user_serv.Entity.User;
import user_serv.Entity.enums.UserRole;
import user_serv.Helper.CoffeeBill;
import user_serv.Helper.CustomPair;
import user_serv.Helper.ToppingBill;
import user_serv.Repository.CoffeeBillRepository;
import user_serv.Repository.ToppingBillRepository;
import user_serv.Repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CoffeeBillRepository coffeeBillRepository;

    @Autowired
    private ToppingBillRepository toppingBillRepository;

    @Autowired
    private WebClient webClient;

    @PostMapping("/create")
    public void create(@RequestBody User user) {
        userRepository.save(user);
    }

    @GetMapping("/all")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PostMapping("/{id}/byCoffee")
    public void byCoffee(@PathVariable Long id, @RequestBody Coffee coffee) {
        User old = userRepository.findById(id).get();
        CoffeeBill coffeeBill = new CoffeeBill();

        Coffee oldCoffee = webClient.get()
                .uri("http://localhost:8082/api/coffee/{id}", coffee.getId())
                .retrieve()
                .bodyToMono(Coffee.class)
                .block();

        oldCoffee.setAmount(oldCoffee.getAmount() - coffee.getAmount());


        coffeeBill.setName(coffee.getName());
        coffeeBill.setAmount(coffee.getAmount());

        coffeeBillRepository.save(coffeeBill);

        old.getCoffees().add(
                coffeeBillRepository.findAll()
                        .stream()
                        .filter(e -> e.getName().equals(coffee.getName()) && e.getAmount() == coffee.getAmount())
                        .findFirst()
                        .get()
        );

        webClient.put()
                .uri("http://localhost:8082/api/coffee/update/{id}", coffee.getId())
                .body(Mono.just(oldCoffee), Coffee.class)
                .retrieve()
                .bodyToMono(Coffee.class)
                .block();

        userRepository.save(old);
    }


    @PostMapping("/{id}/byTopping")
    public void byTopping(@PathVariable Long id, @RequestBody Topping topping) {

        User old = userRepository.findById(id).get();
        ToppingBill toppingBill = new ToppingBill();

        Topping oldTopping = webClient.get()
                .uri("http://localhost:8083/api/topping/{id}", topping.getId())
                .retrieve()
                .bodyToMono(Topping.class)
                .block();

        oldTopping.setAmount(oldTopping.getAmount() -topping.getAmount());

        toppingBill.setName(topping.getName());
        toppingBill.setAmount(topping.getAmount());

        toppingBillRepository.save(toppingBill);

        old.getToppings().add(
                toppingBillRepository.findAll().stream()
                        .filter(e -> e.getAmount() == topping.getAmount() && e.getName().equals(topping.getName()))
                        .findFirst()
                        .get()
        );

        webClient.put()
                .uri("http://localhost:8083/api/topping/update/{id}", topping.getId())
                .body(Mono.just(oldTopping), Topping.class)
                .retrieve()
                .bodyToMono(Topping.class)
                .block();

        userRepository.save(old);
    }

    @PutMapping("/{id}/upCoffee")
    public @ResponseBody
    String upCoffee(@RequestBody Coffee coffee, @PathVariable Long id) {
        if (userRepository.findById(id).get().getUserRole().equals(UserRole.ADMIN)) {
            webClient.put()
                    .uri("http://localhost:8082/api/coffee/update/{id}", coffee.getId())
                    .body(Mono.just(coffee), Coffee.class)
                    .retrieve()
                    .bodyToMono(Coffee.class)
                    .block();
            return "Success";
        }
        return "Only Admin can add amount";
    }

    @PutMapping("/{id}/upTopping")
    public @ResponseBody
    String upTopping(@RequestBody Topping topping, @PathVariable Long id) {
        if (userRepository.findById(id).get().getUserRole().equals(UserRole.ADMIN)) {
            webClient.put()
                    .uri("http://localhost:8083/api/topping/update/{id}", topping.getId())
                    .body(Mono.just(topping), Topping.class)
                    .retrieve()
                    .bodyToMono(Topping.class)
                    .block();
            return "Success";
        }
        return "Only Admin can add amount";
    }

    @GetMapping("/{id}/bill")
    public CustomPair bill(@PathVariable Long id) {
        CustomPair customPair = new CustomPair();
        customPair.setCoffeeBills(
                userRepository.findById(id).get().getCoffees()
        );
        customPair.setToppingBills(
                userRepository.findById(id).get().getToppings()
        );
        return customPair;
    }


}
