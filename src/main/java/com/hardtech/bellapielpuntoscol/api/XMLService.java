package com.hardtech.bellapielpuntoscol.api;

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
        JAXBContext jaxbContext = JAXBContext.newInstance(DocPrinted.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        // Get the application's directory
        String appDirectory = System.getProperty("user.dir");


        // Construct the XML file path
        String xmlFilePath = Paths.get(appDirectory, xml).toString();
        log.info("Scanning file: " + xmlFilePath);
        // Create a File object for the XML file
        File xmlFile = new File(xmlFilePath);

        // Unmarshal the XML file into an instance of DocPrinted class
        DocPrinted docPrinted = (DocPrinted) unmarshaller.unmarshal(xmlFile);

        log.info("numSerie: "+ docPrinted.getSerie() +" numFactura: "+ docPrinted.getNumero());
        facturaMap.put("numSerie", docPrinted.getSerie());
        facturaMap.put("numFactura", String.valueOf(docPrinted.getNumero()));

        return facturaMap;
    }
}
