package Tuan2.TranTanPhat.Repositories;

import Tuan2.TranTanPhat.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends
        JpaRepository<Category, Long> {
}
