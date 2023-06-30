//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccumulationController {
    private final PuntosService puntosService;

    public AccumulationController(PuntosService puntosService) {
        this.puntosService = puntosService;
    }

    @GetMapping({"/puntos-colombia/v1/acumular-pendientes"})
    public String sendAccumulation() {
        this.puntosService.accumulatePending();
        return "Success";
    }

    @GetMapping({"/puntos-colombia/v1/acumular"})
    public String sendAccumulation(@RequestParam String numSerie, @RequestParam int numFactura) {
        return this.puntosService.accumulate(numSerie, numFactura);
    }

    @GetMapping({"/puntos-colombia/v1/cancelar"})
    public String sendDevolution(@RequestParam String numSerie, @RequestParam int numFactura) {
        return this.puntosService.cancellation(numSerie, numFactura);
    }
}
