package org.paasta.servicebroker.webide.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "web_ide_service_list")
public class JpaServiceList {

    @Id
    @Column(name = "no")
    private Integer no;

    @Column(name = "web_ide_service")
    private String webIdeService;

    public JpaServiceList() {
    }

    public JpaServiceList(JpaServiceList jpaServiceList ) {
        this.no = jpaServiceList.getNo();
        this.webIdeService = jpaServiceList.getWebIdeService();
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getWebIdeService() {
        return webIdeService;
    }

    public void setWebIdeService(String webIdeService) {
        this.webIdeService = webIdeService;
    }
}