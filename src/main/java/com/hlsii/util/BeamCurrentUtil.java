package com.hlsii.util;

/**
 * @author ScXin
 * @date 4/27/2020 11:40 PM
 */

import com.hlsii.vo.BeamCurrentPV;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Beam current utility to get all configured pv for the beam current
 *
 */
public class BeamCurrentUtil {
    private static final Logger logger = LoggerFactory.getLogger(SiteConfigUtil.class);
    private static final String VALUE_TAG = "value";
    private static final String BEAM_CURRENT_FILE = "beamcurrent.xml";
    public static final String BEAM_CURRENT = "Beam Current";
    public static final String OPERATION_SCHDULE = "Operation Schedule";
    public static final String OPERATION_STATUS = "Operation Status";
    public static final String LIFETIME = "Lifetime";
    public static final String ENERGY = "Energy";

    private static Map<String, BeamCurrentPV> pvMap = new HashMap<>();

    private BeamCurrentUtil() {}

    public static BeamCurrentPV getPV(String pvName) {
        return pvMap.get(pvName);
    }

    public static List<BeamCurrentPV> getAll() {
        return new ArrayList<>(pvMap.values());
    }

    static {
        parseBeamCurrentConfig();
    }

    public static void main(String[] args) {
        parseBeamCurrentConfig();
    }

    /**
     * Parsing the beam current configured PV from the configuration file
     *
     */
    @SuppressWarnings("unchecked")
    private static void parseBeamCurrentConfig() {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(ConfigUtil.getConfigFilesDir() +
                    File.separator + BEAM_CURRENT_FILE));
            // Get the root node
            Element root = document.getRootElement();
            // Get each PV sub-node for the root
            List<Element> nodes = root.elements("pv");
            for (Element node : nodes) {
                BeamCurrentPV pv = parsePV(node);
                if (pv == null) {
                    logger.warn("Invalid pv configurartion in beam current");
                }
                else {
                    pvMap.put(pv.getPvTitle(), pv);
                }
            }
        } catch (DocumentException e) {
            logger.warn("Parsing beam current configuration error: " + e.getMessage());
        }
    }

    /**
     * Parsing a beam current PV
     *
     * @param node - PV node
     * @return - parsed PV
     */
    private static BeamCurrentPV parsePV(Element node) {
        String pvTitle = node.attributeValue(VALUE_TAG);
        if (StringUtils.isEmpty(pvTitle)) {
            return null;
        }

        BeamCurrentPV pv = new BeamCurrentPV();
        pv.setPvTitle(pvTitle);
        Element nameElem = node.element("name");
        Element unitElem = node.element("unit");
        pv.setPvName(nameElem.attributeValue(VALUE_TAG));
        pv.setUnit(unitElem.attributeValue(VALUE_TAG));
        return pv;
    }
}

