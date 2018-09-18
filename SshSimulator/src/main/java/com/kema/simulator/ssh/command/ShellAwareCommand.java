package com.kema.simulator.ssh.command;

import com.kema.simulator.ssh.SpringShell;

public abstract class ShellAwareCommand {

    public SpringShell getShell() {
        return shell;
    }

    public void setShell(SpringShell shell) {
        this.shell = shell;
    }

    SpringShell shell;

}
