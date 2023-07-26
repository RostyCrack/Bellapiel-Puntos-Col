package com.hardtech.bellapielpuntoscol.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedVentaCabRepository extends JpaRepository<PedVentaCab, PedVentaCabId> {

    PedVentaCab findByNumSerieAndNumPedido(String numSerie, int numPedido);
}
