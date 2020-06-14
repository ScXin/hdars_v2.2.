package com.hlsii.util;

import com.hlsii.vo.MidGroup;
import com.hlsii.vo.PVDataTree;
import com.hlsii.vo.SubGroup;
import com.hlsii.vo.TopGroup;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

/**
 * @author ScXin
 * @date 4/27/2020 11:08 PM
 */
public class PVDataTreeUtil {
    private static final String GROUP_NAME_TAG = "value";
    private static final String CONFIGURATION_FILE = "historyDataTree.xml";
    private static final Logger logger = LoggerFactory.getLogger(SiteConfigUtil.class);

    private static PVDataTree pVDataTree = new PVDataTree();
    private PVDataTreeUtil() {}

    static {
        parseDataTree();
    }

    public static PVDataTree getPVDataTree() {
        return pVDataTree;
    }

    /**
     * Parsing the PV data tree from the configuration file
     *
     */
    @SuppressWarnings("unchecked")
    private static void parseDataTree() {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(new File(ConfigUtil.getConfigFilesDir() +
                    File.separator + CONFIGURATION_FILE));
            // Get the root node
            Element root = document.getRootElement();
            // Get each sub-node (top group) for the root
            List<Element> nodes = root.elements("top_group");
            for (Element node : nodes) {
                TopGroup group = parseTopGroup(node);
                if (group == null) {
                    logger.warn("Invalid top group configurartion");
                }
                else {
                    pVDataTree.addTopGroup(group);
                }
            }
        } catch (DocumentException e) {
            logger.warn("Parsing top group error: " + e.getMessage());
        }
    }

    /**
     * Parsing a top group from the node
     *
     * @param node - top group node
     * @return - parsed top group
     */
    @SuppressWarnings("unchecked")
    private static TopGroup parseTopGroup(Element node) {
        String groupName = node.attributeValue(GROUP_NAME_TAG);
        if (StringUtils.isEmpty(groupName)) {
            return null;
        }

        TopGroup topGroup = new TopGroup(groupName);

        for(Element e : (List<Element>)node.elements("mid_group")) {
            MidGroup midGroup = parseMidGroup(groupName, e);
            if (midGroup == null) {
                logger.warn("Invalid mid-group configurartion");
            }
            else {
                topGroup.addMidGroup(midGroup);
            }
        }

        return topGroup;
    }

    /**
     * Parsing a middle group from the node
     *
     * @param topName - top group name
     * @param node - middle group node
     * @return - parsed middle group
     */
    @SuppressWarnings("unchecked")
    private static MidGroup parseMidGroup(String topName, Element node) {
        String groupName = node.attributeValue(GROUP_NAME_TAG);
        if (StringUtils.isEmpty(groupName)) {
            return null;
        }

        MidGroup midGroup = new MidGroup(groupName);

        for(Element e : (List<Element>)node.elements("sub_group")) {
            groupName = e.attributeValue(GROUP_NAME_TAG);
            if (StringUtils.isEmpty(groupName)) {
                logger.warn("No sub-group name in configurartion");
            }
            else {
                String leafGroupId = topName.trim() + "_" + midGroup.getGroupName().trim() + "_" +
                        groupName.trim();
                SubGroup subGroup = new SubGroup(leafGroupId, groupName);
                SubGroup leafGroup = LeafGroupParser.parseLeafGroups(groupName, leafGroupId);
                if (leafGroup != null) {
                    subGroup = leafGroup;
                }
                midGroup.addSubGroup(subGroup);
                pVDataTree.addLeafGroupMapping(subGroup);
            }
        }

        return midGroup;
    }
}
