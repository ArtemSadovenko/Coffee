package user_serv.Repository;

import user_serv.Helper.ToppingBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToppingBillRepository extends JpaRepository<ToppingBill, Long> {
}
