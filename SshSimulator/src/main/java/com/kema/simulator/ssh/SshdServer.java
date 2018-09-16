package com.kema.simulator.ssh;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.command.Command;
import org.springframework.beans.factory.annotation.Autowired;


public class SshdServer {

	SshServer sshd = SshServer.setUpDefaultServer();

	String template;

	Factory<Command> cmdFactory;
	
	public SshdServer(String template, Factory<Command> cmdFactory)
	{
		this.template = template;
		this.cmdFactory = cmdFactory;
		init();
	}

	private void init() {
//		sshd.setShellFactory(NetworkDeviceCommand::new);
		sshd.setShellFactory(cmdFactory);
	}

	public SshServer getSshd() {
		return sshd;
	}

}
