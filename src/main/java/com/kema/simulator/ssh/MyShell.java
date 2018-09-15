package com.kema.simulator.ssh;

import com.kema.simulator.ssh.command.ConfigCommand;
import org.jline.terminal.Terminal;
import org.springframework.shell.InputProvider;
import org.springframework.shell.MethodTarget;
import org.springframework.shell.ResultHandler;
import org.springframework.shell.Shell;
import org.springframework.shell.result.DefaultResultHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

public class MyShell extends Shell {

    DefaultResultHandler resultHandler2;

    public MyShell(ResultHandler resultHandler) {
        super(resultHandler);
        this.resultHandler2 = (DefaultResultHandler) resultHandler;
//        methodTargets.put("config", new MethodTarget(Method.ConfigCommand.class));
    }

    public void start(Terminal terminal, InputProvider inputProvider) throws IOException {
        resultHandler2.setTerminal(terminal);
        final Map<String, MethodTarget> x = this.listCommands();
        System.out.println(x);

        this.run(inputProvider);

    }

}
