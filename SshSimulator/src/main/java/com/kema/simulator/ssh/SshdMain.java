package com.kema.simulator.ssh;

import com.kema.simulator.ssh.command.CmdConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.standard.StandardAPIAutoConfiguration;
import org.springframework.shell.standard.commands.StandardCommandsAutoConfiguration;


//        exclude = {SpringShellAutoConfiguration.class})
//StandardCommandsAutoConfiguration.class,StandardAPIAutoConfiguration.class

@SpringBootApplication(scanBasePackageClasses= {CmdConfiguration.class, SshdConfiguration.class},
        exclude = {SpringShellAutoConfiguration.class})
public class SshdMain {
	public static void main(String[] args) {
        SpringApplication.run(SshdMain.class, args);
    }

}
