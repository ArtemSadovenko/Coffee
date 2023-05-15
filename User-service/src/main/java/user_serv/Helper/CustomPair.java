package user_serv.Helper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CustomPair {
    List<CoffeeBill> coffeeBills = new ArrayList<>();
    List<ToppingBill> toppingBills = new ArrayList<>();
}
