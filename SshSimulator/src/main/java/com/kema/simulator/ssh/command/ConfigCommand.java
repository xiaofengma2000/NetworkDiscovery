package com.kema.simulator.ssh.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent()
public class ConfigCommand {

    @ShellMethod(value = "enter config mode", key = "config")
    public String  config(){
        return "config mode!";
    }

}
