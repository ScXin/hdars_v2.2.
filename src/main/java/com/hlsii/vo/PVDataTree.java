package com.hlsii.vo;

/**
 * @author Shangcong Xin
 * @date 4/1/20
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Define the data tree for the PVs in the historical data query
 *
 */
public class PVDataTree extends BaseVO {
    private static final long serialVersionUID = 1L;

    // Top group list
    private List <TopGroup> topGroups = new ArrayList<>();

    // Subgroup mapping via id, used to get the specified subgroup by id.
    private Map<String, SubGroup> leafGroupMap = new HashMap<>();

    public PVDataTree() {
        super();
    }

    public List<TopGroup> getTopGroups() {
        return topGroups;
    }

    public void setTopGroups(List<TopGroup> topGroups) {
        this.topGroups = topGroups;
    }

    public void addTopGroup(TopGroup topGroup) {
        this.topGroups.add(topGroup);
    }

    /**
     * Add the leaf group to the map
     *
     * @param leafGroup - leaf group
     */
    public void addLeafGroupMapping(SubGroup leafGroup) {
        leafGroupMap.put(leafGroup.getId(), leafGroup);
    }

    /**
     * Get the leaf group by id
     *
     * @param groupId - group id
     * @return - leaf group
     */
    public SubGroup getLeafGroup(String groupId) {
        return leafGroupMap.get(groupId);
    }
}
