package com.kema.simulator.ssh.command;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.command.AbstractCommandSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseShellCommand extends AbstractCommandSupport {
	
	static final Logger logger = LoggerFactory.getLogger(BaseShellCommand.class);
	
    protected BaseShellCommand() {
        this(null);
    }

    protected BaseShellCommand(String command) {
        super(command, null, true);
    }

    @Override
    public void run() {
        String command = getCommand();
        try {
        	out.write("WELCOME\r\n>".getBytes(StandardCharsets.UTF_8));
            if (command == null) {
                try (BufferedReader r = new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8))) {
                    for (;;) {
                        command = r.readLine();
                        if (command == null) {
                            return;
                        }

                        if (!handleCommandLine(command)) {
                            return;
                        }
                    }
                }
            } else {
                handleCommandLine(command);
            }
        } catch (InterruptedIOException e) {
            // Ignore - signaled end
        } catch (Exception e) {
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
        } finally {
            onExit(0);
        }
    }

    /**
     * @param command The command line
     * @return {@code true} if continue accepting command
     * @throws Exception If failed to handle the command line
     */
    protected abstract boolean handleCommandLine(String command) throws Exception;

	@Override
	public void start(Environment env) throws IOException {
		// TODO Auto-generated method stub
		super.start(env);
	}

//
//    protected void pumpStreams() {
//        try {
//            // Use a single thread to correctly sequence the output and error streams.
//            // If any bytes are available from the output stream, send them first, then
//            // check the error stream, or wait until more data is available.
//            for (byte[] buffer = new byte[bufferSize];;) {
//                if (pumpStream(in, shellIn, buffer)) {
//                    continue;
//                }
//                if (pumpStream(shellOut, out, buffer)) {
//                    continue;
//                }
//                if (pumpStream(shellErr, err, buffer)) {
//                    continue;
//                }
//
//                /*
//                 * Make sure we exhausted all data - the shell might be dead
//                 * but some data may still be in transit via pumping
//                 */
//                if ((!shell.isAlive()) && (in.available() <= 0) && (shellOut.available() <= 0) && (shellErr.available() <= 0)) {
//                    callback.onExit(shell.exitValue());
//                    return;
//                }
//
//                // Sleep a bit.  This is not very good, as it consumes CPU, but the
//                // input streams are not selectable for nio, and any other blocking
//                // method would consume at least two threads
//                Thread.sleep(pumpSleepTime);
//            }
//        } catch (Throwable e) {
//            boolean debugEnabled = log.isDebugEnabled();
//            try {
//                shell.destroy();
//            } catch (Throwable err) {
//                log.warn("pumpStreams({}) failed ({}) to destroy shell: {}",
//                         this, e.getClass().getSimpleName(), e.getMessage());
//                if (debugEnabled) {
//                    log.debug("pumpStreams(" + this + ") shell destruction failure details", err);
//                }
//            }
//
//            int exitValue = shell.exitValue();
//            if (debugEnabled) {
//                log.debug(e.getClass().getSimpleName() + " while pumping the streams (exit=" + exitValue + "): " + e.getMessage(), e);
//            }
//            callback.onExit(exitValue, e.getClass().getSimpleName());
//        }
//    }

}
