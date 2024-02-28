//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.hardtech.bellapielpuntoscol.api;

import com.hardtech.bellapielpuntoscol.context.datasource.DataSourceContextHolder;
import com.hardtech.bellapielpuntoscol.context.datasource.DataSourceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.xml.bind.JAXBException;
import java.util.Map;

@RestController
public class AccumulationController {
    private final PuntosService puntosService;
    private final XMLService xmlService;

    private final DataSourceContextHolder dataSourceContextHolder;

    public AccumulationController(PuntosService puntosService, XMLService xmlService, DataSourceContextHolder dataSourceContextHolder) {
        this.puntosService = puntosService;
        this.xmlService = xmlService;
        this.dataSourceContextHolder = dataSourceContextHolder;
    }

    @GetMapping({"/puntos-colombia/v1/acumular-pendientes"})
    public String sendAccumulation() {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);
        this.puntosService.accumulatePending();
        return "Success";
    }

    @GetMapping({"/puntos-colombia/v1/acumular"})
    public String sendAccumulation(@RequestParam String numSerie, @RequestParam int numFactura) {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);
        return this.puntosService.accumulate(numSerie, numFactura);
    }

    @GetMapping({"/puntos-colombia/v1/cancelar"})
    public String sendDevolution(@RequestParam String numSerie, @RequestParam int numFactura) {

        dataSourceContextHolder.setBranchContext(DataSourceEnum.MNG_BP);
        return this.puntosService.cancellation(numSerie, numFactura);
    }

    @GetMapping({"/puntos-colombia/v1/token"})
    public String sendTokenRequest() {
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);
        return this.puntosService.sendTokenRequest().toString();
    }

    @GetMapping({"/puntos-colombia/v1/readXML"})
    public String readXML() {
        try{
            Map<String, String> facturaMap = this.xmlService.readXML("acumular-puntos.xml");
            String numSerie = facturaMap.get("numSerie");
            int numFactura = Integer.parseInt(facturaMap.get("numFactura"));

            if (numSerie.endsWith("Y")){
                dataSourceContextHolder.setBranchContext(DataSourceEnum.MNG_BP);
                return this.sendDevolution(numSerie, numFactura);
            }
            else{
                dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);
                return this.sendAccumulation(numSerie, numFactura);
            }
        }catch (JAXBException e){
            return "Error al leer el archivo XML.";
        }

    }

    @GetMapping("/prueba")
    public String prueba(){
        dataSourceContextHolder.setBranchContext(DataSourceEnum.ICGFRONT);
        puntosService.getPruebaRepository();
        return "prueba";
    }

    @GetMapping("/prueba2")
    public String prueba2(){
        dataSourceContextHolder.setBranchContext(DataSourceEnum.MNG_BP);
        puntosService.getPruebaRepository();
        return "prueba2";
    }
}
