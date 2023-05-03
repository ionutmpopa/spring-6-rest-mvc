package guru.springframework.spring6restmvc.repository;

import guru.springframework.spring6restmvc.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
