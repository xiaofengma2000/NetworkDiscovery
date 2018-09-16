package com.kema.simulator.ssh;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.jline.InteractiveShellApplicationRunner.JLineInputProvider;
import org.springframework.shell.result.DefaultResultHandler;

public class BaseSpringShellCommand extends AbstractCommandSupport {
	
	static final Logger logger = LoggerFactory.getLogger(BaseSpringShellCommand.class);

    protected MyShell shell;

    public BaseSpringShellCommand(MyShell shell) {
        this(null, shell);
    }

    public BaseSpringShellCommand(String command, MyShell shell)
    {
        super(command, null, true);
        this.shell = shell;
    }

    public PromptProvider promptProvider() {
		return () -> new AttributedString("shell:>", AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
	}

    @Override
    public void run() {
    	try {
    		Thread.currentThread().setName("SSHD--TEST");
            //supported terminal type by JLine in Infocmp class:
            //"dumb", "ansi", "xterm", "xterm-256color","windows", "screen", "screen-256color"
            TerminalBuilder builder = TerminalBuilder.builder().system(false).streams(in, out).type("xterm");
            Terminal terminal = builder.build();
            LineReader linkReader = LineReaderBuilder.builder().terminal(terminal).build();
            JLineInputProvider inputProvider = new JLineInputProvider(linkReader, promptProvider());

//            shell.getResultHandler();
//            shell.run(inputProvider);

            shell.start(terminal, inputProvider);
            shell.listCommands();
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Failed (" + e.getClass().getSimpleName() + ") to handle '" + command + "': " + e.getMessage();
            try {
                OutputStream stderr = getErrorStream();
                stderr.write(message.getBytes(StandardCharsets.US_ASCII));
            } catch (IOException ioe) {
                logger.warn("Failed ({}) to write error message={}: {}",
                         e.getClass().getSimpleName(), message, ioe.getMessage());
            } finally {
                onExit(-1, message);
            }
		}
    }

    @Override
    public void start(Environment env) throws IOException {
        super.start(env);
    }

//    @Override
//    public void run() {
//
//        try {
//            Thread.currentThread().setName("SSHD--TEST");
//
//            TerminalBuilder builder = TerminalBuilder.builder().system(false).streams(in, out).type("auto");
//            Terminal terminal = builder.build();
////			PosixPtyTerminal terminal2 = new PosixPtyTerminal("Name", "TypeLinux",
////					ExecPty.current(), in, out, StandardCharsets.UTF_8.toString());
//
//            LineReader linkReader = LineReaderBuilder.builder().terminal(terminal).build();
//            DefaultResultHandler resultHandler = new DefaultResultHandler();
//            resultHandler.setTerminal(terminal);
//            Shell shell = new Shell(resultHandler);
//            shell.gatherMethodTargets();
//
//            JLineInputProvider inputProvider = new JLineInputProvider(linkReader, promptProvider());
//            shell.run(inputProvider);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            String message = "Failed (" + e.getClass().getSimpleName() + ") to handle '" + command + "': " + e.getMessage();
//            try {
//                OutputStream stderr = getErrorStream();
//                stderr.write(message.getBytes(StandardCharsets.US_ASCII));
//            } catch (IOException ioe) {
//                logger.warn("Failed ({}) to write error message={}: {}",
//                        e.getClass().getSimpleName(), message, ioe.getMessage());
//            } finally {
//                onExit(-1, message);
//            }
//        }
//    }

}
