package Tuan2.TranTanPhat.Repositories;

import Tuan2.TranTanPhat.Entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> { }
