package com.kema.simulator.ssh.command;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent()
public class ConfigCommand extends ShellAwareCommand{

    @ShellMethod(value = "enter config mode", key = "config")
    public String  config(){
        System.out.println("My Shell is : " + getShell());
        return "config mode!";
    }

}
