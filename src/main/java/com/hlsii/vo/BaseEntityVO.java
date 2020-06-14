package com.hlsii.vo;

/**
 * @author ScXin
 * @date 4/28/2020 12:35 AM
 */
public abstract class BaseEntityVO extends BaseVO {
    private static final long serialVersionUID = 1L;

    private String id;

    public BaseEntityVO() {
        super();
    }

    public BaseEntityVO(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntityVO that = (BaseEntityVO) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
