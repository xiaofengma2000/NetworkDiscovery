package com.kema.management.entities;

import javax.persistence.*;

/**
 * JPA entity to represent a node
 */
@Entity
@Table(name = "NODE_INSTANCE")
public class NodeInfo {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "TYPE")
    String type;

    @Column(name = "USER")
    String user;

    @Column(name = "PASSWORD")
    String password;

    @Column(name = "IP_ADDRESS")
    String ipaddress;

    @Column(name = "PORT")
    String port;

}
