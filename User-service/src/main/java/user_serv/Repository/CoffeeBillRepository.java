package user_serv.Repository;

import user_serv.Helper.CoffeeBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoffeeBillRepository extends JpaRepository<CoffeeBill, Long> {
}
