package com.hlsii.util;

import com.hlsii.vo.SubGroup;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * @author ScXin
 * @date 4/27/2020 11:39 PM
 */
public class LeafGroupParser {
    private static final Logger logger = LoggerFactory.getLogger(SiteConfigUtil.class);

    private LeafGroupParser() {

    }

    /**
     * Parsing a leaf group from the configuration file
     *
     * @param groupName
     *            - group name
     * @param leafeGroupId
     *            - Configuration file name (not include ext) for the leaf group
     * @return - parsed leaf group
     */
    public static SubGroup parseLeafGroups(String groupName, String leafeGroupId) {
        String configurationFile = leafeGroupId + ".xml";
        SAXReader reader = new SAXReader();
        SubGroup group = null;
        try {
            File groupFile = new File(ConfigUtil.getConfigFilesDir() + File.separator +
                    "group" + File.separator + configurationFile);
            if (!groupFile.exists()) {
                // Configuration file not existed
                return null;
            }
            Document document = reader.read(groupFile);
            // Get the root node (group)
            Element root = document.getRootElement();
            group = parseGroup(root, groupName, leafeGroupId);
        } catch (DocumentException e) {
            logger.warn("Parsing leaf group error: " + e.getMessage());
        }

        return group;
    }

    /**
     * Parse a leaf group from the node
     *
     * @param node
     *            - group node
     * @param groupName
     *            - group name
     * @param groupId
     *            - group Id
     * @return - parsed leaf group
     */
    @SuppressWarnings("unchecked")
    private static SubGroup parseGroup(Element node, String groupName, String groupId) {
        Element unitElem = node.element("unit");
        Element titleElem = node.element("ChartTitle");
        String unit = unitElem == null ? "" : unitElem.getTextTrim();
        String title = titleElem == null ? "" : titleElem.getTextTrim();

        SubGroup group = new SubGroup(groupId, groupName, unit, title);
        for (Element e : (List<Element>) node.elements("channel")) {
            String pvName = e.attributeValue("name");
            String label = e.getTextTrim();
            group.addPV(pvName, label);
        }

        return group;
    }
}
