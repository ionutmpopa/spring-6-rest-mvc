package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}