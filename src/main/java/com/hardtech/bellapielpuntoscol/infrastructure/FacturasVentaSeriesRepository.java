//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturasVentaSeriesRepository extends JpaRepository<FacturasVentasSeriesOL, String> {
    FacturasVentasSeriesOL findByNumFacturaAndNumSerie(int numFactura, String numSerie);
}
