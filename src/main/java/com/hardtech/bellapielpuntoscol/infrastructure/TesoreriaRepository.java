//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesoreriaRepository extends JpaRepository<Tesoreria, TesoreriaId> {
    List<Tesoreria> findAllBySerieAndNumero(String serie, int numero);
}
