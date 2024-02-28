package com.hardtech.bellapielpuntoscol.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedVentaLinRepository extends JpaRepository<PedVentaLin, PedVentaLinId>{

    List<PedVentaLin> findDistinctByNumSerieAndNumPedido(String numSerie, int numPedido);
}
