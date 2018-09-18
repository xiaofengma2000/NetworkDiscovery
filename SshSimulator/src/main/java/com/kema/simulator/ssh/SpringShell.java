package com.kema.simulator.ssh;

import com.kema.simulator.ssh.command.ConfigCommand;
import com.kema.simulator.ssh.command.ShellAwareCommand;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.Signals;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.*;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.result.DefaultResultHandler;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.nio.channels.ClosedByInterruptException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringShell extends Shell {

    DefaultResultHandler resultHandler2;

    public SpringShell(ApplicationContext ctx, DefaultResultHandler resultHandler) {
        super(resultHandler);
        this.applicationContext = ctx;
        this.resultHandler2 = resultHandler;
        //manual invoke the Shell post construct phase
        try {
            setParameterResolvers(new ArrayList<>(applicationContext.getBeansOfType(ParameterResolver.class).values()));
            gatherMethodTargets();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //initialise shell instance
        //to allow command make changes to Shell instance
        listCommands().values().stream().forEach( mt -> {
            if(mt.getBean() instanceof ShellAwareCommand){
                ((ShellAwareCommand)mt.getBean()).setShell(this);
            }
        });
    }

    public void start(Terminal terminal, InteractiveShellApplicationRunner.JLineInputProvider inputProvider) throws IOException {
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

    /**
     * This class is to provide ability to change Prompt
     */
    public static class MyInputProvider extends InteractiveShellApplicationRunner.JLineInputProvider {

        private final PromptProvider promptProvider;

        public MyInputProvider(LineReader lineReader, PromptProvider promptProvider) {
            super(lineReader, promptProvider);
            this.promptProvider = promptProvider;
        }

    }

}
