package com.kema.simulator.ssh.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.sshd.common.util.OsUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.shell.InvertedShell;

/**
 * base class Network Device Shell command
 * 
 * @author xiaofeng
 *
 */
public class NetworkDeviceCommand extends BaseShellCommand {

	@Override
	protected boolean handleCommandLine(String command) throws Exception {
		if(command.equals("exit"))
		{
			return false;
		}
		if(command.equals("config"))
		{
			out.write("kema>".getBytes(StandardCharsets.UTF_8));
			out.write(command.getBytes(StandardCharsets.UTF_8));
			out.write("\r\nkema#config>".getBytes(StandardCharsets.UTF_8));
			out.flush();
			return true;
		}
		else
		{
			out.write("kema>".getBytes(StandardCharsets.UTF_8));
			out.write(command.getBytes(StandardCharsets.UTF_8));
			out.write("\r\nkema>".getBytes(StandardCharsets.UTF_8));
			out.flush();
			return true;
		}
	}

	@Override
	public void start(Environment env) throws IOException {
		//out.write("WELCOME\\r\\nkema>".getBytes(StandardCharsets.UTF_8));
		super.start(env);
	}

}
