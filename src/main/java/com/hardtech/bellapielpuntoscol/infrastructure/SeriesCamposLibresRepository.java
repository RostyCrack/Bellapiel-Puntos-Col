//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesCamposLibresRepository extends JpaRepository<SeriesCamposLibres, String> {
    Optional<SeriesCamposLibres> findById(String id);
}
