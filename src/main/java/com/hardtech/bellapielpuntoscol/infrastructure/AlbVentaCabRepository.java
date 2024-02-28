//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbVentaCabRepository extends JpaRepository<AlbVentaCab, AlbVentaLinId> {

    AlbVentaCab findByNumSerieAndNumFactura(String numSerie, int numFactura);

    AlbVentaCab findByNumSerieAndNumAlbaran(String numSerie, int numAlbaran);

}
