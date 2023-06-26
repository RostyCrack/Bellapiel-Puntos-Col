//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturasVentaCamposLibresRepository extends JpaRepository<FacturasVentaCamposLibres, FacturasVentaId> {
    FacturasVentaCamposLibres findByNumFacturaAndNumSerie(int numFactura, String numSerie);

    @Query("SELECT fv FROM FACTURASVENTACAMPOSLIBRES fv WHERE fv.fechaAcumulacionPuntos IS NOT NULL AND fv.fechaAcumulacionPuntos = :minDate")
    List<FacturasVentaCamposLibres> findByAccumulatedAfter(LocalDateTime minDate);
}
