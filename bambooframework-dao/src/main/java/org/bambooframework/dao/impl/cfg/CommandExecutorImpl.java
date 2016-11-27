package org.bambooframework.dao.impl.cfg;

import org.bambooframework.dao.impl.interceptor.Command;
import org.bambooframework.dao.impl.interceptor.CommandConfig;
import org.bambooframework.dao.impl.interceptor.CommandExecutor;
import org.bambooframework.dao.impl.interceptor.CommandInterceptor;


public class CommandExecutorImpl implements CommandExecutor {

  private final CommandConfig defaultConfig;
  private final CommandInterceptor first;
  
  public CommandExecutorImpl(CommandConfig defaultConfig, CommandInterceptor first) {
    this.defaultConfig = defaultConfig;
    this.first = first;
  }
  
  public CommandInterceptor getFirst() {
    return first;
  }

  @Override
  public CommandConfig getDefaultConfig() {
    return defaultConfig;
  }
  
  @Override
  public <T> T execute(Command<T> command) {
    return execute(defaultConfig, command);
  }

  @Override
  public <T> T execute(CommandConfig config, Command<T> command) {
    return first.execute(config, command);
  }

}
