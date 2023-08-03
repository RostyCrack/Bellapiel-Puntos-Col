//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBException;
import java.util.Map;

@RestController
public class AccumulationController {
    private final PuntosService puntosService;
    private final XMLService xmlService;

    public AccumulationController(PuntosService puntosService, XMLService xmlService) {
        this.puntosService = puntosService;
        this.xmlService = xmlService;
    }

    @GetMapping({"/puntos-colombia/v1/acumular-pendientes"})
    public String sendAccumulation() {
        this.puntosService.accumulatePending();
        return "Success";
    }

    @GetMapping({"/puntos-colombia/v1/acumular"})
    public String sendAccumulation(String numSerie, int numFactura) {
        return this.puntosService.accumulate(numSerie, numFactura);
    }

    @GetMapping({"/puntos-colombia/v1/cancelar"})
    public String sendDevolution(@RequestParam String numSerie, @RequestParam int numFactura) {
        return this.puntosService.cancellation(numSerie, numFactura);
    }

    @GetMapping({"/puntos-colombia/v1/token"})
    public String sendTokenRequest() {
        return this.puntosService.sendTokenRequest().toString();
    }

    @GetMapping({"/puntos-colombia/v1/readXML"})
    public String readXML() {
        try{
            Map<String, String> facturaMap = this.xmlService.readXML("acumular-puntos.xml");
            String numSerie = facturaMap.get("numSerie");
            int numFactura = Integer.parseInt(facturaMap.get("numFactura"));

            if (numSerie.endsWith("Y")){
                return this.sendDevolution(numSerie, numFactura);
            }
            else{
                return this.sendAccumulation(numSerie, numFactura);
            }
        }catch (JAXBException e){
            return "Error al leer el archivo XML.";
        }

    }
}
