package com.hlsii.vo;

import com.hlsii.entity.QueryProfile;

/**
 * @author ScXin
 * @date 4/28/2020 12:34 AM
 */
public class ProfileVO extends BaseEntityVO {
    private static final long serialVersionUID = 1L;

    private String profName;

    public ProfileVO() {
        super();
    }

    public ProfileVO(QueryProfile profile) {
        this(profile.getId(), profile.getProfName());
    }

    public ProfileVO(String id, String profName) {
        super(id);
        this.profName = profName;
    }

    public String getProfName() {
        return profName;
    }

    public void setProfName(String profName) {
        this.profName = profName;
    }
}