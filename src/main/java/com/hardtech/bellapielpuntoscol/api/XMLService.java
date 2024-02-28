package com.hardtech.bellapielpuntoscol.api;

import com.hardtech.bellapielpuntoscol.context.domain.shared.Doc;
import com.hardtech.bellapielpuntoscol.context.domain.shared.DocPrinted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class XMLService {
    public Map<String, String> readXML(String xml) throws JAXBException {
        Map<String, String> facturaMap = new HashMap<>();
        JAXBContext docPrintedJaxb = JAXBContext.newInstance(DocPrinted.class);
        Unmarshaller unmarshaller = docPrintedJaxb.createUnmarshaller();
        // Get the application's directory
        String appDirectory = System.getProperty("user.dir");

        // Construct the XML file path
        String xmlFilePath = Paths.get(appDirectory, xml).toString();
        log.info("Scanning file: " + xmlFilePath);
        // Create a File object for the XML file
        File xmlFile = new File(xmlFilePath);

        DocPrinted docPrinted = (DocPrinted) unmarshaller.unmarshal(xmlFile);
        log.info("numSerie: "+ docPrinted.getSerie() +" numFactura: "+ docPrinted.getNumero());
        facturaMap.put("numSerie", docPrinted.getSerie());
        facturaMap.put("numFactura", String.valueOf(docPrinted.getNumero()));

        return facturaMap;
    }

    public Map<String, String> readPedidoXML(String xml) throws JAXBException {

        Map<String, String> pedidoMap = new HashMap<>();
        JAXBContext docJaxb = JAXBContext.newInstance(Doc.class);
        Unmarshaller unmarshaller = docJaxb.createUnmarshaller();

        // Get the application's directory
        String appDirectory = System.getProperty("user.dir");
        // Construct the XML file path
        String xmlFilePath = Paths.get(appDirectory, xml).toString();
        log.info("Scanning file: " + xmlFilePath);

        // Create a File object for the XML file
        File xmlFile = new File(xmlFilePath);
        Doc doc = (Doc) unmarshaller.unmarshal(xmlFile);
        log.info("numSerie: "+ doc.getSerie() +" numPedido: "+ doc.getNumero());
        pedidoMap.put("numSerie", doc.getSerie());
        pedidoMap.put("numFactura", String.valueOf(doc.getNumero()));
        return pedidoMap;
    }
}
