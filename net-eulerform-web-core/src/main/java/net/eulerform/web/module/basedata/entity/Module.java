package net.eulerform.web.module.basedata.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import net.eulerform.web.core.base.entity.UUIDEntity;

/**
 * CODE_TABLE<br>
 * 用来存储JS字典数据或系统配置参数
 * 
 * @author cFrost
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SYS_MODULE")
public class Module extends UUIDEntity<Module> {

    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "SHOW_ORDER", nullable = false)
    private Integer showOrder;
    @Column(name = "REQUIRE_AUTHORITY")
    private String requireAuthority;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "MODULE_ID")
    @OrderBy("showOrder ASC")
    public Set<Page> pages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取数据在页面的显示顺序,只对JS字典类数据有效
     * 
     * @return 显示顺序序号
     */
    public Integer getShowOrder() {
        return showOrder;
    }

    /**
     * 设置数据再页面的显示顺序,只对JS字典类数据有效
     * 
     * @param showOrder
     *            显示顺序序号
     */
    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public Set<Page> getPages() {
        return pages;
    }

    public void setPages(Set<Page> pages) {
        this.pages = pages;
    }

    public String getRequireAuthority() {
        return requireAuthority;
    }

    public void setRequireAuthority(String requireAuthority) {
        this.requireAuthority = requireAuthority;
    }
}
