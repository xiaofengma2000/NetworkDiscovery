package com.kema.simulator.ssh;

import com.kema.simulator.ssh.command.ConfigCommand;
import org.jline.terminal.Terminal;
import org.jline.utils.Signals;
import org.springframework.shell.*;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.channels.ClosedByInterruptException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyShell extends Shell {

    DefaultResultHandler resultHandler2;

    public MyShell(ResultHandler resultHandler) {
        super(resultHandler);
        this.resultHandler2 = (DefaultResultHandler) resultHandler;
    }

    public void start(Terminal terminal, InputProvider inputProvider) throws IOException {
        resultHandler2.setTerminal(terminal);
        final Map<String, MethodTarget> x = this.listCommands();
        System.out.println(x);

        this.run(inputProvider);
    }


    public Object evaluate(Input input) {
        if (noInput(input)) {
            return NO_INPUT;
        }

        Object result = super.evaluate(input);
        if (result instanceof CommandNotFound) {

            String line = input.words().stream().collect(Collectors.joining(" ")).trim();
            System.out.println("no Command found : " + line);
        }
        return result;
    }

    private boolean noInput(Input input) {
        return input.words().isEmpty()
                || (input.words().size() == 1 && input.words().get(0).trim().isEmpty())
                || (input.words().iterator().next().matches("\\s*//.*"));
    }

}
