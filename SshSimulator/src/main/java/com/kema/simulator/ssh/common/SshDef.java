package com.kema.simulator.ssh.common;

import javax.persistence.*;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "SSH_INSTANCE")
public class SshDef {

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public long getDisconnectTimeout() {
        return disconnectTimeout;
    }

    public void setDisconnectTimeout(long disconnectTimeout) {
        this.disconnectTimeout = disconnectTimeout;
    }

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

    private @Id
    @GeneratedValue
    Long id;

    @Column(name = "TYPE")
    String type="Cisco9K";

    @Column(name = "PORT")
    int port = 22;

    @Column(name = "IDLE_TIMEOUT")
    long idleTimeout = TimeUnit.SECONDS.toMillis(600);;

    @Column(name = "CONNECT_TIMEOUT")
    long disconnectTimeout = TimeUnit.SECONDS.toMillis(600);;

}
