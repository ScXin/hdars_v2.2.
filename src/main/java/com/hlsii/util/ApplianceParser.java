package com.hlsii.util;


import com.hlsii.commdef.Appliance;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * XML Parse for appliance configuration
 *
 */
public class ApplianceParser {
    private static final Logger logger = LoggerFactory.getLogger(ApplianceParser.class);
    private static final String CONFIG_FILE = "appliances.xml";
    // Site configured appliance list
    private static List<Appliance> appliances;

    private ApplianceParser() {}

    static {
        appliances = parseApplianceConfiguration();
    }

    public static List<Appliance> getAppliances() {
        return appliances;
    }

    /**
     * Parse the appliance from the appliances.xml configuration file
     *
     * @return - appliance list
     */
    @SuppressWarnings("unchecked")
    private static List<Appliance> parseApplianceConfiguration() {
        List<Appliance> appliances = new ArrayList<>();
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(ConfigUtil.getConfigFilesDir() +
                    File.separator + CONFIG_FILE));
            // Get the root node (appliances)
            Element root = document.getRootElement();
            // Get each sub-node (appliance) for the root
            List<Element> nodes = root.elements();
            for (Element node : nodes) {
                appliances.add(parseAppliance(node));
            }
        } catch (DocumentException e) {
            logger.warn("Error at parsing {}.", CONFIG_FILE);
        }

        return appliances;
    }

    /**
     * Parse an appliance from a node
     *
     * @param node
     *            - XML node
     * @return - appliance
     */
    private static Appliance parseAppliance(Element node) {
        String applianceId = node.element("identity").getTextTrim();
        String clusterInetport = node.element("cluster_inetport").getTextTrim();
        String mgmtUrl = node.element("mgmt_url").getTextTrim();
        String engineUrl = node.element("engine_url").getTextTrim();
        String etlUrl = node.element("etl_url").getTextTrim();
        String retrievalUrl = node.element("retrieval_url").getTextTrim();
        String dataRetrievalUrl = node.element("data_retrieval_url").getTextTrim();
        return new Appliance(applianceId, clusterInetport, mgmtUrl, engineUrl, etlUrl, retrievalUrl,
                dataRetrievalUrl);
    }
}