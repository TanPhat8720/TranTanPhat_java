package Tuan2.TranTanPhat.Repositories;

import Tuan2.TranTanPhat.Entities.ItemInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemInvoiceRepository extends JpaRepository<ItemInvoice, Long> { }